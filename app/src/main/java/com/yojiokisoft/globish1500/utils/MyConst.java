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

import android.os.Environment;

import com.yojiokisoft.globish1500.App;

import java.io.File;

/**
 * 定数クラス.
 */
public class MyConst {
    /**
     * 設定キー：自動で発音
     */
    public static final String PK_AUTO_SPEAK = "AutoSpeak";

    /**
     * 設定キー：バージョン
     */
    public static final String PK_VERSION = "Version";

    /**
     * 設定キー：お問い合わせ
     */
    public static final String PK_INQUIRY = "Inquiry";

    /**
     * ExtraName:学習モード
     */
    public static final String EN_LEARNING_MODE = "LearningMode";

    /**
     * ExtraName:学習日
     */
    public static final String EN_LEARN_DATE = "LearnDate";

    /**
     * ExtraName:覚えたものは除く
     */
    public static final String EN_OMIT_MEMORIZED = "OmitLearned";

    /**
     * バグファイル名(キャッチした)
     */
    public static final String BUG_CAUGHT_FILE = "bug_caught.txt";

    /**
     * バグファイル名(キャッチされなかった)
     */
    public static final String BUG_UNCAUGHT_FILE = "bug_uncaught.txt";

    /**
     * キャッチしたバグファイルのフルパス
     */
    public static String getCaughtBugFilePath() {
        return MyFile.pathCombine(App.getInstance().getAppDataPath(), BUG_CAUGHT_FILE);
    }

    /**
     * キャッチされなかったバグファイルのフルパス
     */
    public static String getUncaughtBugFilePath() {
        return MyFile.pathCombine(App.getInstance().getAppDataPath(), BUG_UNCAUGHT_FILE);
    }

    /**
     * SQLiteのDB名
     */
    public static final String DATABASE_FILE = "globish1500.db";

    public static final String DATABASE_PATH = File.separator + "Globish1500" + File.separator;

    /**
     * SQLiteのDB名のフルパス
     */
    public static String getDatabasePath() {
//        File sdDir = Environment.getExternalStorageDirectory();
//        File file = new File(sdDir.getAbsoluteFile() + DATABASE_PATH + DATABASE_FILE);
//        return file.getAbsolutePath();

        return App.getInstance().getDatabasePath(DATABASE_FILE).getAbsolutePath();
    }
}
