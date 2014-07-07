/*
 * Copyright (C) 2014 4jiokiSoft
 *
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.yojiokisoft.globish1500.dao;

import com.yojiokisoft.globish1500.entity.Card;
import com.yojiokisoft.globish1500.entity.CardOrder;
import com.yojiokisoft.globish1500.entity.English;
import com.yojiokisoft.globish1500.entity.Japanese;
import com.yojiokisoft.globish1500.entity.LearningLog;
import com.yojiokisoft.globish1500.entity.UsageExample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * カードのDAO
 * Created by taoka on 14/05/16.
 */
public class CardDao {
    private static CardDao mCardDao = null;
    private CardOrderDao mCardOrderDao;
    private EnglishDao mEnglishDao;
    private JapaneseDao mJapaneseDao;
    //    private UsageExampleDao mUsageExampleDao; 例文の表示は未実装
    private LearningLogDao mLearningLogDao;
    private List<LearningLog> mList;
    private int mIndex;
    private Card mCard;
    private int mCardIndex = -1;

    public static CardDao getInstance() {
        if (mCardDao == null) {
            mCardDao = new CardDao();
        }
        return mCardDao;
    }

    private CardDao() {
        mCardOrderDao = new CardOrderDao();
        mEnglishDao = new EnglishDao();
        mJapaneseDao = new JapaneseDao();
//        mUsageExampleDao = new UsageExampleDao();
        mLearningLogDao = new LearningLogDao();
    }

    private void init() {
        mList = null;
        mIndex = 0;
        mCardIndex = -1;
    }

    /**
     * 学習してないもの
     */
    public void queryForNotLearning() {
        init();

        List<CardOrder> list = mCardOrderDao.queryForNotLearning();
        if (list == null) {
            return;
        }

        mList = new ArrayList<LearningLog>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CardOrder cardOrder = list.get(i);
            LearningLog learningLog = new LearningLog();
            learningLog.id = -1;
            learningLog.english_id = cardOrder.english_id;
            learningLog.learn_date = null;
            learningLog.memorized = null;
            mList.add(learningLog);
        }
    }

    /**
     * 学習済みのもの（覚えたもの＋まだ覚えてないもの）
     */
    public void queryForLearned() {
        init();
        mList = mLearningLogDao.queryForAll();
    }

    /**
     * （学習済みで）まだ覚えてないもの
     */
    public void queryForMemorized(boolean onlyNotMemorize) {
        init();
        if (onlyNotMemorize) {
            mList = mLearningLogDao.queryForMemorized("0");
        } else {
            queryForLearned();
        }
    }

    public void shuffle() {
        Collections.shuffle(mList);
    }

    /**
     * （学習済みで）まだ覚えてないものを学習日ごとに
     */
    public void queryForLearnDate(String learnDate, boolean onlyNotMemorize) {
        init();
        if (onlyNotMemorize) {
            mList = mLearningLogDao.queryForLearnDateAndMemorized(learnDate, "0");
        } else {
            mList = mLearningLogDao.queryForLearnDate(learnDate);
        }
    }

    private Card getCard(int index) {
        if (index == mCardIndex) {
            return mCard;
        }
        if (mList == null || mList.size() == 0) {
            return null;
        }
        Card card = new Card();
        LearningLog learningLog = mList.get(mIndex);
        card.english = mEnglishDao.queryForId(learningLog.english_id);
        List<Japanese> list = mJapaneseDao.queryForEnglishId(learningLog.english_id);
        if (list != null) {
            card.japaneseList = new ArrayList<Card.JapaneseList>();
            for (Japanese japanese : list) {
                Card.JapaneseList japaneseList = new Card.JapaneseList();
                japaneseList.japanese = japanese;
                japaneseList.usageExampleList = null; // 例文の表示は未実装
//                japaneseList.usageExampleList = mUsageExampleDao.queryForEnglishIdAndJapaneseId(
//                        japaneseList.japanese.english_id, japaneseList.japanese.id);
                japaneseList.memorized = learningLog.memorized;
                card.japaneseList.add(japaneseList);
            }
        }

        mCard = card;
        mCardIndex = index;
        return card;
    }

    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public English getEnglish() {
        Card card = getCard(mIndex);
        if (card == null) {
            return null;
        }
        return card.english;
    }

    public LearningLog getLearningLog() {
        if (mList == null || mList.get(mIndex).id == -1) {
            return null;
        }
        return mList.get(mIndex);
    }

    public boolean isPrevCard() {
        if (mList == null || mList.size() == 0) {
            return false;
        }
        if ((mIndex - 1) < 0) {
            return false;
        }
        return true;
    }

    public boolean prevCard() {
        if (mList == null || mList.size() == 0) {
            return false;
        }
        mIndex--;
        if (mIndex < 0) {
            mIndex = 0;
            return false;
        }
        return true;
    }

    public boolean isNextCard() {
        if (mList == null || mList.size() == 0) {
            return false;
        }
        if ((mIndex + 1) >= mList.size()) {
            return false;
        }
        return true;
    }

    public boolean nextCard() {
        if (mList == null || mList.size() == 0) {
            return false;
        }
        mIndex++;
        if (mIndex >= mList.size()) {
            mIndex = mList.size() - 1;
            return false;
        }
        return true;
    }

    public void deleteCard() {
        if (mList == null || mList.size() == 0) {
            return;
        }
        if (mIndex < 0 || mList.size() <= mIndex) {
            return;
        }
        mList.remove(mIndex);
        if (mList.size() <= mIndex) {
            mIndex = mList.size() - 1;
        }
        mCardIndex = -1;
    }

    public Japanese getJapanese() {
        Card card = getCard(mIndex);
        if (card == null) {
            return null;
        }
        if (card.japaneseList == null || card.japaneseList.size() <= 0) {
            return null;
        }
        return card.japaneseList.get(0).japanese;
    }

    public boolean isMemorized() {
        Card card = getCard(mIndex);
        if (card == null) {
            return false;
        }
        if (card.japaneseList == null || card.japaneseList.size() <= 0) {
            return false;
        }
        return ("1".equals(card.japaneseList.get(0).memorized));
    }

    public void setMemorized(boolean memorized) {
        Card card = getCard(mIndex);
        if (card == null) {
            return;
        }
        if (card.japaneseList == null || card.japaneseList.size() <= 0) {
            return;
        }
        card.japaneseList.get(0).memorized = (memorized ? "1" : "0");
    }

    public UsageExample getExample() {
        Card card = getCard(mIndex);
        if (card == null) {
            return null;
        }
        if (card.japaneseList == null || card.japaneseList.size() <= 0) {
            return null;
        }
        List<UsageExample> list = card.japaneseList.get(0).usageExampleList;
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }
}
