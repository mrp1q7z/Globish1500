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
 * カテゴリーマスタのエンティティ
 * Created by taoka on 14/05/13.
 */
@DatabaseTable
public class Category extends BaseEntity implements Convertable {
    /**
     * カテゴリー名
     */
    public final static String NAME = "name";
    @DatabaseField(columnName = NAME, canBeNull = false)
    public String name;

    /**
     * エンティティを生成します
     *
     * @param map
     * @return
     */
    public static Category create(Map<String, Object> map) {
        Category entity = new Category();
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
        this.name = (String) map.get("name");
    }
}
