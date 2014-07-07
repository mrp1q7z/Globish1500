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
 * 品詞
 * Created by taoka on 14/05/14.
 */
public enum PartOfSpeech {
    M1(-1, "?", "【不】"),
    P1(1, "NOUN", "名詞"),
    P2(2, "PRONOUN", "代名詞"),
    P3(3, "VERB", "動詞"),
    P4(4, "ADJECTIVE", "形容詞"),
    P5(5, "ADVERB", "副詞"),
    P6(6, "PREPOSITION", "前置詞"),
    P7(7, "CONJUNCTION", "接続詞"),
    P8(8, "INTERJECTION", "感動詞");

    private int id;
    private String enName;
    private String jpName;

    PartOfSpeech(int id, String enName, String jpName) {
        this.id = id;
        this.enName = enName;
        this.jpName = jpName;
    }

    public int getId() {
        return id;
    }

    public String getEnglishName() {
        return enName;
    }

    public String getJapaneseName() {
        return jpName;
    }

    public static PartOfSpeech getEnum(int id) {
        for (PartOfSpeech e : PartOfSpeech.values()) {
            if (id == e.getId()) {
                return e;
            }
        }
        return null;
    }
}
