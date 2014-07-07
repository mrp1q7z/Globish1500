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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

import com.yojiokisoft.globish1500.App;
import com.yojiokisoft.globish1500.BuildConfig;

/**
 * デバッグログクラス
 */
public class MyLog {
    /**
     * デバッグログへ出力
     *
     * @param msg デバッグメッセージ
     */
    public static void writeDebug(String msg) {
//        String path = Environment.getExternalStorageDirectory().getPath();
//        File file = new File(path + MyConst.DATABASE_PATH + "debug.txt");
        File file = new File(App.getInstance().getAppDataPath() + "debug.txt");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.JAPANESE);
        String timeStamp = dateFormat.format(new Date());
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            pw.println(timeStamp + " " + msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * StackTraceをファイルに書き込む.
     *
     * @param fileName ファイル名
     * @param ex       エラー
     * @throws java.io.FileNotFoundException
     */
    public static void writeStackTrace(String fileName, Throwable ex) {
        File file = new File(fileName);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(file));
            ex.printStackTrace(pw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * メッセージの最後に呼び出し元情報を付加したデバッグメッセージを出力する.
     *
     * @param message
     */
    public static void d(String message) {
        if (BuildConfig.DEBUG) {
            StackTraceElement s = getCallerInfo();
            Log.d(getShortClassName(s.getClassName()), message + getCallerInfo(s));
        }
    }

    /**
     * メッセージの最後に呼び出し元情報を付加したデバッグメッセージを出力する.
     *
     * @param tag
     * @param message
     */
    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            StackTraceElement s = getCallerInfo();
            Log.d(tag, message + getCallerInfo(s));
        }
    }

    /**
     * メッセージの最後に呼び出し元情報を付加したデバッグメッセージを出力する.
     *
     * @param message
     * @param throwable
     */
    public static void d(String message, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            StackTraceElement s = getCallerInfo();
            Log.d(getShortClassName(s.getClassName()), message + getCallerInfo(s), throwable);
        }
    }

    /**
     * メッセージの最後に呼び出し元情報を付加したデバッグメッセージを出力する.
     *
     * @param tag
     * @param message
     * @param throwable
     */
    public static void d(String tag, String message, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            StackTraceElement s = getCallerInfo();
            Log.d(tag, message + getCallerInfo(s), throwable);
        }
    }

    /**
     * 呼び出し元情報の取得
     * この関数(getCallerInfo) : [0]
     * この関数を呼んだ親(d)   : [1]
     * 親を呼んだ関数          : [2] <-- これのクラス名等を取得
     *
     * @return StackTraceElement
     */
    private static StackTraceElement getCallerInfo() {
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        return stackTraceElements[2];
    }

    /**
     * 呼び出し元情報の取得
     *
     * @param s StackTraceElement
     * @return 以下のような呼び出し元情報
     * at ClassName#MethodName(FileName:LineNumber)
     * at MainActivity#onStart(MainActivity.java:110)
     */
    private static String getCallerInfo(StackTraceElement s) {
        String caller = " at " + getShortClassName(s.getClassName()) + "#" + s.getMethodName()
                + "(" + s.getFileName() + ":" + s.getLineNumber() + ")";
        return caller;
    }

    /**
     * FQCNのクラス名からクラス名のみを取得する.
     * 例：com.yojiokisoft.yumekanow.activity.MainActivity -> MainActivity
     *
     * @param fqcnClassName FQCNクラス名
     * @return クラス名
     */
    private static String getShortClassName(String fqcnClassName) {
        String className = fqcnClassName.substring(fqcnClassName.lastIndexOf(".") + 1);
        return className;
    }
}
