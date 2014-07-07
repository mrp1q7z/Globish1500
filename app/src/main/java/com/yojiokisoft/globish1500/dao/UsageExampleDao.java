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
import com.yojiokisoft.globish1500.entity.UsageExample;

import java.sql.SQLException;
import java.util.List;

/**
 * 使用例のDAO
 * Created by taoka on 14/05/27.
 */
public class UsageExampleDao {
    private Dao<UsageExample, Integer> mUsageExampleDao = null;

    public UsageExampleDao() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            mUsageExampleDao = helper.getDao(UsageExample.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * english_idとjapanese_idによる検索
     *
     * @param english_id  英語マスタのID
     * @param japanese_id 日本語マスタのID
     * @return 使用例のリスト
     */
    public List<UsageExample> queryForEnglishIdAndJapaneseId(int english_id, int japanese_id) {
        try {
            return mUsageExampleDao.queryBuilder().where()
                    .eq(UsageExample.ENGLISH_ID, english_id)
                    .and().eq(UsageExample.JAPANESE_ID, japanese_id)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
