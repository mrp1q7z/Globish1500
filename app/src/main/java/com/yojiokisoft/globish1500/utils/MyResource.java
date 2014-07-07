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

import java.io.FileNotFoundException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;

import com.yojiokisoft.globish1500.App;

/**
 * リソース関連のユーティリティ
 */
public class MyResource {
    /**
     * リソース名からリソースIDを得る.
     *
     * @param name リソース名
     * @return リソースID
     */
    public static int getResourceIdByName(String name) {
        App app = App.getInstance();
        String packageName = app.getPackageName();
        return app.getResources().getIdentifier(name, "drawable", packageName);
    }

    /**
     * @return パッケージ情報
     */
    public static PackageInfo getPackageInfo() {
        App app = App.getInstance();
        PackageInfo packageInfo = null;
        try {
            packageInfo = app.getPackageManager()
                    .getPackageInfo(app.getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            MyLog.writeStackTrace(MyConst.BUG_CAUGHT_FILE, e);
        }
        return packageInfo;
    }

    /**
     * dipをpxに変換
     *
     * @param dip
     * @return px
     */
    public static int dip2Px(int dip) {
        float density = App.getInstance().getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    /**
     * pxをdipに変換
     *
     * @param px
     * @return dip
     */
    public static int px2Dip(int px) {
        float density = App.getInstance().getResources().getDisplayMetrics().density;
        int dip = (int) (px / density + 0.5f);
        return dip;
    }

    /**
     * spをpxに変換
     *
     * @param sp
     * @return px
     */
    public static int sp2Px(int sp) {
        DisplayMetrics matrics = App.getInstance().getResources().getDisplayMetrics();
        int px = (int) (sp * matrics.scaledDensity + 0.5f);
        return px;
    }

    /**
     * @return ステータスバーの高さ
     */
    public static int getStatusBarHeight() {
        return dip2Px(25);
    }
}
