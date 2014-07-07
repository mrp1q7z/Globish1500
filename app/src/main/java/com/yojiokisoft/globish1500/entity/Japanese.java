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

import java.util.Map;

/**
 * 日本語意味マスタのエンティティ
 * Created by taoka on 14/05/14.
 */
public class Japanese extends BaseEntity implements Convertable {
    /**
     * 英単語ID
     */
    public final static String ENGLISH_ID = "english_id";
    @DatabaseField(columnName = ENGLISH_ID, canBeNull = false)
    public Integer english_id;

    /**
     * 日本語
     */
    public final static String JAPANESE = "japanese";
    @DatabaseField(columnName = JAPANESE, canBeNull = false)
    public String japanese;

    /**
     * 品詞（enum PartOfSpeechのID）
     */
    public final static String PART_OF_SPEECH = "part_of_speech";
    @DatabaseField(columnName = PART_OF_SPEECH, canBeNull = false)
    public Short part_of_speech;

    /**
     * エンティティを生成します
     *
     * @param map
     * @return
     */
    public static Japanese create(Map<String, Object> map) {
        Japanese entity = new Japanese();
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
        this.english_id = (Integer) map.get("english_id");
        this.japanese = (String) map.get("japanese");
        this.part_of_speech = (Short) map.get("part_of_speech");
    }
}
