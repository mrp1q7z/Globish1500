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
 * 英単語マスタのエンティティ
 * Created by taoka on 14/05/13.
 */
@DatabaseTable
public class English extends BaseEntity implements Convertable {
    /**
     * 英語
     */
    public final static String ENGLISH = "english";
    @DatabaseField(columnName = ENGLISH, canBeNull = false)
    public String english;

    /**
     * 発音記号
     */
    public final static String PHONETIC_SYMBOL = "phonetic_symbol";
    @DatabaseField(columnName = PHONETIC_SYMBOL, canBeNull = false)
    public String phonetic_symbol;

    /**
     * エンティティを生成します
     *
     * @param map
     * @return
     */
    public static English create(Map<String, Object> map) {
        English entity = new English();
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
        this.english = (String) map.get("english");
        this.phonetic_symbol = (String) map.get("phonetic_symbol");
    }
}
