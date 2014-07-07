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

/**
 * Created by taoka on 14/05/13.
 */
public abstract class BaseEntity implements Convertable {
    /**
     * ID
     */
    public final static String ID = "id";
    @DatabaseField(id = true, columnName = ID)
    public Integer id;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof BaseEntity)) {
            return false;
        }

        BaseEntity baseEntity = (BaseEntity) o;

        if (baseEntity.id == this.id) {
            return true;
        }

        if (baseEntity.id == null) {
            return this.id == null;
        }

        return baseEntity.id.equals(this.id);
    }
}
