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
package com.yojiokisoft.globish1500.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Map;

/**
 * 学習履歴のエンティティ
 * Created by taoka on 14/06/04.
 */
@DatabaseTable
public class LearningLog extends BaseEntity implements Convertable {
    /**
     * 学習日(yyyymmdd)
     */
    public final static String LEARN_DATE = "learn_date";
    @DatabaseField(columnName = LEARN_DATE, canBeNull = false)
    public String learn_date;

    /**
     * 英語ID
     */
    public final static String ENGLISH_ID = "english_id";
    @DatabaseField(columnName = ENGLISH_ID, canBeNull = false)
    public Integer english_id;

    /**
     * 覚えたフラグ
     */
    public final static String MEMORIZED = "memorized";
    @DatabaseField(columnName = MEMORIZED, canBeNull = false, defaultValue = "0")
    public String memorized;

    /**
     * エンティティを生成します
     *
     * @param map
     * @return
     */
    public static LearningLog create(Map<String, Object> map) {
        LearningLog entity = new LearningLog();
        entity.set(map);
        return entity;
    }

    /**
     * Mapの値をエンティティにセットします
     *
     * @param map
     */
    @Override
    public void set(Map<String, Object> map) {
        this.id = (Integer) map.get("id");
        this.learn_date = (String) map.get("learn_date");
        this.english_id = (Integer) map.get("english_id");
        this.memorized = (String) map.get("memorized");
    }

    @Override
    public String toString() {
        return "id=" + id + ", learn_date=" + learn_date + ", english_id=" + english_id + ", memorized=" + memorized;
    }
}
