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
import com.yojiokisoft.globish1500.entity.CardOrder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * カード並び順のDAO
 * Created by taoka on 14/05/16.
 */
public class CardOrderDao {
    private Dao<CardOrder, Integer> mCardOrderDao = null;

    public CardOrderDao() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        try {
            mCardOrderDao = helper.getDao(CardOrder.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CardOrder> queryForAll() {
        try {
            return mCardOrderDao.query(mCardOrderDao.queryBuilder().orderBy(CardOrder.ID, true).prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CardOrder> queryForNotLearning() {
        try {
            List<CardOrder> list = new ArrayList<CardOrder>();
            String sql = "select id, english_id from cardorder a where not exists (select 1 from learninglog b where a.english_id = b.english_id)";
            GenericRawResults<String[]> rawResults = mCardOrderDao.queryRaw(sql);
            for (String[] resultArray : rawResults) {
                CardOrder row = new CardOrder();
                row.id = Integer.parseInt(resultArray[0]);
                row.english_id = Integer.parseInt(resultArray[1]);
                list.add(row);
            }
            rawResults.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
