#-*- coding: utf-8 -*-

from bs4 import BeautifulSoup

def get_voa_words():
    f = open('words.htm')
    html = f.readlines()
    f.close()

    soup = BeautifulSoup(''.join(html))

    dic = {}
    li_all = soup.findAll('li')
    for line in li_all:
        word = line.b.string
        part_of_speech = line.tt.string
        dic[word] = part_of_speech

    return dic

def get_globish_words():
    f = open('words.txt')
    lines = f.readlines()
    f.close()

    dic = {}
    for line in lines:
        word = line[:line.find('\t')]
        dic[word] = word

    return dic

def main():
    voa_words = get_voa_words()
    globish_words = get_globish_words()

    for word in globish_words:
        if not voa_words.has_key(word):
            print word + ' がVOAに見つかりません'

if __name__ == '__main__':
	main()