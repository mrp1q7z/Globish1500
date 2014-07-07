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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.yojiokisoft.globish1500.entity.LearningLog;
import com.yojiokisoft.globish1500.entity.LearningLogSum;
import com.yojiokisoft.globish1500.utils.MyLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 学習履歴のDAO
 * Created by taoka on 14/05/27.
 */
public class LearningLogDao {
    private Dao<LearningLog, Integer> mLearningLogDao = null;

    public LearningLogDao() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            mLearningLogDao = helper.getDao(LearningLog.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LearningLog> queryForAll() {
        try {
            return mLearningLogDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LearningLog> queryForLearnDate(String learnDate) {
        try {
            return mLearningLogDao.queryForEq(LearningLog.LEARN_DATE, learnDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LearningLog> queryForMemorized(String memorized) {
        try {
            List<LearningLog> list = mLearningLogDao.queryForEq(LearningLog.MEMORIZED, memorized);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LearningLog> queryForLearnDateAndMemorized(String learnDate, String memorized) {
        try {
            return mLearningLogDao.queryBuilder().where()
                    .eq(LearningLog.LEARN_DATE, learnDate)
                    .and().eq(LearningLog.MEMORIZED, memorized)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<LearningLogSum> convertLearningLogSum(GenericRawResults<String[]> rawResults) {
        try {
            List<LearningLogSum> list = new ArrayList<LearningLogSum>();
            for (String[] resultArray : rawResults) {
                LearningLogSum row = new LearningLogSum();
                row.learn_date = resultArray[0];
                row.words_count = Integer.parseInt(resultArray[1]);
                MyLog.d(row.toString());
                list.add(row);
            }
            rawResults.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LearningLogSum> queryForAllGroupByLearnDate() {
        try {
            String sql = "select learn_date, count(*) from learninglog group by learn_date order by learn_date desc";
            return convertLearningLogSum(mLearningLogDao.queryRaw(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LearningLogSum> queryForMemorizedGroupByLearnDate(String memorized) {
        try {
            String sql = "select learn_date, count(*) from learninglog where memorized = ? group by learn_date order by learn_date desc";
            return convertLearningLogSum(mLearningLogDao.queryRaw(sql, memorized));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCountByMemorized(String memorized) {
        try {
            return (int) mLearningLogDao.queryBuilder().where()
                    .eq(LearningLog.MEMORIZED, memorized).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCount() {
        try {
            return (int) mLearningLogDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int createIfNotExists(LearningLog learningLog) {
        try {
            int cnt = mLearningLogDao.queryForEq(LearningLog.ENGLISH_ID, learningLog.english_id).size();
            int ret = 0;
            if (cnt <= 0) {
                ret = mLearningLogDao.create(learningLog);
            }
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int update(LearningLog learningLog) {
        try {
            return mLearningLogDao.update(learningLog);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
