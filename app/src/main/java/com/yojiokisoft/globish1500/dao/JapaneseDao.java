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
import com.yojiokisoft.globish1500.entity.Japanese;

import java.sql.SQLException;
import java.util.List;

/**
 * 日本語マスタのDAO
 * Created by taoka on 14/05/26.
 */
public class JapaneseDao {
    private Dao<Japanese, Integer> mJapaneseDao = null;

    public JapaneseDao() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            mJapaneseDao = helper.getDao(Japanese.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Japanese> queryForEnglishId(int english_id) {
        try {
            return mJapaneseDao.queryForEq(Japanese.ENGLISH_ID, english_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
