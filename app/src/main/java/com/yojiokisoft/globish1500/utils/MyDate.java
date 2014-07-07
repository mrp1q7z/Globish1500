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
package com.yojiokisoft.globish1500.utils;

import android.text.format.DateFormat;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by taoka on 14/06/05.
 */
public class MyDate {
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD = "yyyy/MM/dd";

    public static String getNowDate() {
        return getNowDate(YYYYMMDD);
    }

    public static String getNowDate(String format) {
        String now;
        // ※時間を取得する場合、HHの代わりにkkにすること
        now = DateFormat.format(format, Calendar.getInstance()).toString();

        /*
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        now = sdf.format(cal.getTime());

        Time t = new Time();
        t.setToNow();
        now = t.format("%Y%m%d");
        */

        return now;
    }

    public static Date dateStringToDate(String dateString) {
        int len = dateString.length();
        SimpleDateFormat sdf;

        if (len == 8) {
            sdf = new SimpleDateFormat(YYYYMMDD);
        } else if (len == 10) {
            sdf = new SimpleDateFormat(YYYY_MM_DD);
        } else {
            return null;
        }

        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int differenceDays(String date1, String date2) {
        Date d1 = dateStringToDate(date1);
        Date d2 = dateStringToDate(date2);
        return differenceDays(d1, d2);
    }

    /**
     * 2つの日付の差を求めます。
     * java.util.Date 型の日付 date1 - date2 が何日かを返します。
     * <p/>
     * 計算方法は以下となります。
     * 1.最初に2つの日付を long 値に変換します。
     * 　※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの
     * 　経過ミリ秒数となります。
     * 2.次にその差を求めます。
     * 3.上記の計算で出た数量を 1 日の時間で割ることで
     * 　日付の差を求めることができます。
     * 　※1 日 ( 24 時間) は、86,400,000 ミリ秒です。
     *
     * @param date1 日付 java.util.Date
     * @param date2 日付 java.util.Date
     * @return 2つの日付の差
     */
    public static int differenceDays(Date date1, Date date2) {
        long datetime1 = date1.getTime();
        long datetime2 = date2.getTime();
        long one_date_time = 1000 * 60 * 60 * 24;
        long diffDays = (datetime1 - datetime2) / one_date_time;
        return (int) diffDays;
    }
}
