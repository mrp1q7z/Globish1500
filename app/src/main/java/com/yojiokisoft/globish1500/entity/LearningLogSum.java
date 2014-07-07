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

/**
 * Created by taoka on 14/06/05.
 */
public class LearningLogSum {
    /**
     * 学習日(yyyymmdd)
     */
    public String learn_date;

    /**
     * 単語の件数
     */
    public int words_count;

    @Override
    public String toString() {
        return "learn_date=" + learn_date + ", words_count=" + words_count;
    }
}
