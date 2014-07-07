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
package com.yojiokisoft.globish1500.exception;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.yojiokisoft.globish1500.R;
import com.yojiokisoft.globish1500.utils.MyConst;
import com.yojiokisoft.globish1500.utils.MyDialog;
import com.yojiokisoft.globish1500.utils.MyFile;
import com.yojiokisoft.globish1500.utils.MyLog;
import com.yojiokisoft.globish1500.utils.MyResource;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * キャッチされない例外を処理する
 */
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static File sBugReportFile = null;
    private static String sVersionName;
    private static final Thread.UncaughtExceptionHandler sDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    /**
     * キャッチされない例外が発生したときに呼び出される.
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        MyLog.writeStackTrace(MyConst.getUncaughtBugFilePath(), ex);
        sDefaultHandler.uncaughtException(thread, ex);
    }

    /**
     * 障害報告ダイアログのOKクリックリスナー
     */
    private static DialogInterface.OnClickListener mBugDialogOkClicked = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            postBugReportInBackground();
        }
    };

    /**
     * 障害報告ダイアログのキャンセルクリックリスナー
     */
    private static DialogInterface.OnClickListener mBugDialogCancelClicked = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            sBugReportFile.delete();
        }
    };

    /**
     * バグレポートの内容をメールで送信する.
     *
     * @param activity アプリケーションコンテキストはダメ、Activityであること！
     */
    public static void sendBugReport(Activity activity) {
        sBugReportFile = new File(MyConst.getUncaughtBugFilePath());
        if (sBugReportFile == null || !sBugReportFile.exists()) {
            return;
        }

        setVersionName();

        MyDialog.Builder.newInstance(activity)
                .setTitle(activity.getString(R.string.err_dialog_title))
                .setMessage(activity.getString(R.string.err_dialog_msg))
                .setPositiveLabel(activity.getString(R.string.send))
                .setPositiveClickListener(mBugDialogOkClicked)
                .setNegativeLabel(activity.getString(R.string.cancel))
                .setNegativeClickListener(mBugDialogCancelClicked)
                .show();
    }

    /**
     * バグレポートの内容をメールで送信する.
     *
     * @param activity アプリケーションコンテキストはダメ、Activityであること！
     * @param ex
     */
    public static void sendBugReport(Activity activity, Throwable ex) {
        MyLog.writeStackTrace(MyConst.getCaughtBugFilePath(), ex);

        sBugReportFile = new File(MyConst.getCaughtBugFilePath());
        if (sBugReportFile == null || !sBugReportFile.exists()) {
            return;
        }

        setVersionName();

        MyDialog.Builder.newInstance(activity)
                .setTitle(activity.getString(R.string.err_dialog_title))
                .setMessage(activity.getString(R.string.err_dialog_msg2))
                .setPositiveLabel(activity.getString(R.string.send))
                .setPositiveClickListener(mBugDialogOkClicked)
                .setNegativeLabel(activity.getString(R.string.cancel))
                .setNegativeClickListener(mBugDialogCancelClicked)
                .show();
    }

    /**
     * バージョン名のセット
     */
    private static void setVersionName() {
        if (sVersionName != null) {
            return;
        }
        PackageInfo packInfo = MyResource.getPackageInfo();
        sVersionName = packInfo.versionName;
    }

    /**
     * バグレポートをバックグラウンドで送信.
     */
    private static void postBugReportInBackground() {
        new Thread(new Runnable() {
            public void run() {
                postBugReport();
            }
        }).start();
    }

    /**
     * バグレポートをサーバーにポスト.
     */
    private static void postBugReport() {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String bug = getFileBody(sBugReportFile);
        nvps.add(new BasicNameValuePair("dev", Build.DEVICE));
        nvps.add(new BasicNameValuePair("mod", Build.MODEL));
        nvps.add(new BasicNameValuePair("sdk", Build.VERSION.SDK));
        nvps.add(new BasicNameValuePair("ver", sVersionName));
        nvps.add(new BasicNameValuePair("bug", bug));
        try {
            HttpPost httpPost = new HttpPost("http://mrp-bug-report.appspot.com/bug");
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sBugReportFile.delete();
    }

    /**
     * ファイルの内容を読み込む.
     *
     * @param file
     * @return
     */
    private static String getFileBody(File file) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            MyFile.closeQuietly(br);
        }
        return sb.toString();
    }
}