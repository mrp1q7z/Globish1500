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
 * 使用例のエンティティ
 * Created by taoka on 14/05/13.
 */
@DatabaseTable
public class UsageExample extends BaseEntity implements Convertable {
    /**
     * 英単語ID
     */
    public final static String ENGLISH_ID = "english_id";
    @DatabaseField(columnName = ENGLISH_ID, canBeNull = false)
    public Integer english_id;

    /**
     * 日本語マスタのID
     */
    public final static String JAPANESE_ID = "japanese_id";
    @DatabaseField(columnName = JAPANESE_ID, canBeNull = false)
    public Integer japanese_id;

    /**
     * 使用例（英語）
     */
    public final static String USAGE_EXAMPLE_EN = "usage_example_en";
    @DatabaseField(columnName = USAGE_EXAMPLE_EN, canBeNull = false)
    public String usage_example_en;

    /**
     * 使用例（日本語）
     */
    public final static String USAGE_EXAMPLE_JP = "usage_example_jp";
    @DatabaseField(columnName = USAGE_EXAMPLE_JP, canBeNull = false)
    public String usage_example_jp;

    /**
     * エンティティを生成します
     *
     * @param map
     * @return
     */
    public static UsageExample create(Map<String, Object> map) {
        UsageExample entity = new UsageExample();
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
        this.japanese_id = (Integer) map.get("japanese_id");
        this.usage_example_en = (String) map.get("usage_example_en");
        this.usage_example_jp = (String) map.get("usage_example_jp");
    }
}
