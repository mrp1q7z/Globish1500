#-*- coding: utf-8 -*-

import sqlite3
import time
import urllib
from urllib2 import urlopen, HTTPError, Request
from collections import namedtuple
from bs4 import BeautifulSoup
import re

TAG_RE = re.compile(r'<[^>]+>')
Word = namedtuple('Word', 'english, japanese')
English = namedtuple('English', 'id, english, phonetic_symbol')
Japanese = namedtuple('Japanese', 'id, english_id, japanese, part_of_speech')
UsageExample = namedtuple('UsageExample', 'id, english_id, japanese_id, usage_example_en, usage_example_jp')
Alc = namedtuple('Alc', 'english, phonetic_symbol, part_of_speech')

def parse_part_of_speech(soup):
	find_list = soup.findAll('span', 'wordclass', limit=1)
	part_of_speech = None
	for line in find_list:
		part_of_speech = line.text

	if part_of_speech == u'【名】':
		return 1 # 名詞
	elif part_of_speech == u'【組織】':
		return 1
	elif part_of_speech == u'【代】':
		return 2 # 代名詞
	elif part_of_speech == u'【動】':
		return 3 # 動詞
	elif part_of_speech == u'【他動】':
		return 3
	elif part_of_speech == u'【自動】':
		return 3
	elif part_of_speech == u'【助】':
		return 3
	elif part_of_speech == u'【句動】':
		return 3
	elif part_of_speech == u'【形】':
		return 4 # 形容詞
	elif part_of_speech == u'【副】':
		return 5 # 副詞
	elif part_of_speech == u'【前】':
		return 6 # 前置詞
	elif part_of_speech == u'【接】':
		return 7 # 接続詞
	elif part_of_speech == u'【間】':
		return 8 # 感動詞
	elif part_of_speech == u'【略】':
		return -2
	elif part_of_speech == u'【反】':
		return -3
	elif part_of_speech == u'【対】':
		return -4
	elif part_of_speech == u'【類】':
		return -5
	elif part_of_speech == u'【同】':
		return -6

	return -99

def parse_phonetic_symbol(soup):
    find_list = soup.findAll('span', 'pron')
    data = None
    for line in find_list:
        data = remove_tags(line.text)

    if data == None:
    	return '?'
    return data.rstrip(u'、')

def remove_tags(text):
    return TAG_RE.sub('', text)

def main():
	#insert_words()
	#insert_example()
	update_part_of_speech_by_goo()

def parse_examples(soup):
    examples = soup.find('ul', 'examples')
    if examples == None:
    	return '?'

    for x in examples:
    	first_ex = x
    	break

    return first_ex.text.replace('\n', ' ')

def get_example_html(word):
	ok_flag = False

	while (ok_flag == False):
		try:
			response = urlopen('http://sentence.yourdictionary.com/' + word)
			html = response.read()
			response.close()
			ok_flag = True
		except HTTPError, e:
			print word + ' / HTTPError code:', e.code
			time.sleep(3)

	return html

def insert_example():
	usage_example_insert_sql = "insert into usageexample (id, english_id, japanese_id, usage_example_en, usage_example_jp) values(?, ?, ?, ?, ?)"

	conn = sqlite3.connect('../assets/globish1500.db')
	conn.text_factory = str
	cur = conn.cursor()

	c = conn.execute('select a.id, a.english from english a where not exists (select 1 from usageexample b where a.id = b.english_id)')
	for row in c.fetchall():
		english = English(row[0], row[1], None)
		html = get_example_html(english.english)
		soup = BeautifulSoup(html.decode('utf-8'))
		ex = parse_examples(soup)
		usage_example = UsageExample(english.id, english.id, english.id, ex, '?')
		print usage_example
		conn.execute(usage_example_insert_sql, list(usage_example))
		conn.commit()

	conn.close()

def insert_words():
	f = open('words.txt')
	all_data = f.readlines()
	f.close()

	conn = sqlite3.connect('../assets/globish1500.db')
	conn.text_factory = str
	cur = conn.cursor()

	english_insert_sql = "insert into english (id, english, phonetic_symbol) values(?, ?, ?)"
	japanese_insert_sql = "insert into japanese (id, english_id, japanese, part_of_speech) values(?, ?, ?, ?)"

	id = 0
	for line_data in all_data:
		id += 1
		c = conn.execute("select * from english where id = ?", [id])
		if (len(c.fetchall()) >= 1):
			print 'id=' + str(id) + ' skiped'
			continue

		word = parse_word(line_data)
		alc = get_alc(word)

		english = parse_english(id, word, alc.phonetic_symbol)
		print 'English(id=' + str(english.id) + ', english=' + english.english + ', phonetic_symbol=' + english.phonetic_symbol.encode('utf-8') + ')'

		japanese = parse_japanese(id, word, alc.part_of_speech)
		print 'Japanese(id=' + str(japanese.id) + ', english_id=' + str(japanese.english_id) + ', japanese=' + japanese.japanese + ', part_of_speech=' + str(japanese.part_of_speech) + ')'

		conn.execute(english_insert_sql, list(english))
		conn.execute(japanese_insert_sql, list(japanese))
		conn.commit()


	conn.close()

def parse_word(line_data):
	array_data = line_data.split('\t')
	word = Word(array_data[0], array_data[1].rstrip())
	return word

def parse_english(id, word, phonetic_symbol):
	english = English(id, word.english, phonetic_symbol)
	return english

def parse_japanese(id, word, part_of_speech):
	japanese = Japanese(id, id, word.japanese, part_of_speech)
	return japanese

def get_alc_html(word):
	ok_flag = False

	while (ok_flag == False):
		try:
			response = urlopen('http://eow.alc.co.jp/search?q=' + word)
			html = response.read()
			response.close()
			ok_flag = True
		except HTTPError, e:
			print 'HTTPError code:', e.code
			time.sleep(3)

	return html

def get_alc(word):
	html = get_alc_html(word.english)
	soup = BeautifulSoup(html.decode('utf-8'))
	phonetic_symbol = parse_phonetic_symbol(soup)
	part_of_speech = parse_part_of_speech(soup)

	alc = Alc(word, phonetic_symbol, part_of_speech)
	return alc

#class MyOpener(urllib.FancyURLopener):
#    version = 'Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:30.0) Gecko/20100101 Firefox/30.0'

def update_part_of_speech_by_goo():
	japanese_update_sql = "update japanese set part_of_speech = ? where english_id = ?"

	conn = sqlite3.connect('../assets/globish1500.db')
	conn.text_factory = str
	cur = conn.cursor()

	c = conn.execute('select a.id, a.english from english a inner join japanese b on a.id = b.english_id where b.part_of_speech = -99')
	for row in c.fetchall():
		english = English(row[0], row[1], None)
		part_of_speech = get_parse_part_of_speech_from_goo(english)
		print 'id=' + str(english.id) + ', part_of_speech=' + str(part_of_speech)
		conn.execute(japanese_update_sql, (part_of_speech, english.id))
		conn.commit()

	conn.close()

def get_goo_html(word):
	ok_flag = False

	while (ok_flag == False):
		try:
			url = 'http://dictionary.goo.ne.jp/srch/ej/' + word + '/m0u/'
			print url
			#myopener = MyOpener()
			#page = myopener.open(url)
			#html = page.read()
			#page.close()

			req = Request(url)
			req.add_header("User-agent", 'Mozilla/5.0')
			response = urlopen(req)
			html = response.read()
			response.close()
			ok_flag = True
		except HTTPError, e:
			print 'HTTPError code:', e.code
			time.sleep(3)

	return html

def get_parse_part_of_speech_from_goo(word):
	html = get_goo_html(word.english)
	soup = BeautifulSoup(html.decode('utf-8'))
	part_of_speech = parse_part_of_speech_goo(soup)

	return part_of_speech

def parse_part_of_speech_goo(soup):
	find_list = soup.find('dl', 'allList')

	part_of_speech = None
	if find_list == None:
		find_list = soup.find('hinshi')
		part_of_speech = find_list.string.encode('utf-8')
	else:
		for line in find_list.find_all('dd'):
			part_of_speech = re.compile('\[.+?\]').search(line.encode('utf-8'))
			if part_of_speech != None:
				part_of_speech = part_of_speech.group(0)
				break

	print part_of_speech

	if part_of_speech == '[名]':
		return 1 # 名詞
	elif part_of_speech == '[代]':
		return 2 # 代名詞
	elif part_of_speech == '[動]':
		return 3 # 動詞
	elif part_of_speech == '[助]':
		return 3
	elif part_of_speech == '[形]':
		return 4 # 形容詞
	elif part_of_speech == '[副]':
		return 5 # 副詞
	elif part_of_speech == '[前]':
		return 6 # 前置詞
	elif part_of_speech == '[接]':
		return 7 # 接続詞
	elif part_of_speech == '[間]':
		return 8 # 感動詞
	elif part_of_speech == '[感]':
		return 8

	print 'unknown part_of_speech:' + part_of_speech
	return -99

if __name__ == '__main__':
	main()