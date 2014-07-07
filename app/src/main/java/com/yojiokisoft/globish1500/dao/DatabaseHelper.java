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
package com.yojiokisoft.globish1500.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yojiokisoft.globish1500.App;
import com.yojiokisoft.globish1500.entity.CardOrder;
import com.yojiokisoft.globish1500.entity.Category;
import com.yojiokisoft.globish1500.entity.CategoryRelation;
import com.yojiokisoft.globish1500.entity.English;
import com.yojiokisoft.globish1500.entity.Japanese;
import com.yojiokisoft.globish1500.entity.LearningLog;
import com.yojiokisoft.globish1500.entity.UsageExample;
import com.yojiokisoft.globish1500.utils.MyConst;
import com.yojiokisoft.globish1500.utils.MyFile;
import com.yojiokisoft.globish1500.utils.MyLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * DBヘルパー
 * Created by taoka on 14/05/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper mInstance = null;

    private DatabaseHelper(Context context) {
        super(context, MyConst.getDatabasePath(), null, DATABASE_VERSION);
    }

    /**
     * インスタンスの取得.
     *
     * @return DatabaseHelper
     */
    public static synchronized DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(App.getInstance().getAppContext());
        }
        return mInstance;
    }

    public static void init() {
        createPreInstallDatabaseIfNotExists();
    }

    public static void destroy() {
        mInstance = null;
    }

    /**
     * DB がなければプリインストールの DB を asset よりコピーして作成する
     */
    public static void createPreInstallDatabaseIfNotExists() {
        File file = new File(MyConst.getDatabasePath());
        if (file.exists()) {
            return;
        }

        try {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    throw new IOException("mkdirs error : " + file.getPath());
                }
            }
            if (!file.createNewFile()) {
                throw new IOException("createNewFile error : " + file.getPath());
            }
            copyDatabaseFromAsset();
        } catch (IOException e) {
            MyLog.writeStackTrace(MyConst.BUG_CAUGHT_FILE, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * asset に格納した DB をデフォルトの DB パスにコピーする
     */
    private static void copyDatabaseFromAsset() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = App.getInstance().getAppContext().getAssets().open(MyConst.DATABASE_FILE);
            out = new FileOutputStream(MyConst.getDatabasePath());

            byte[] buffer = new byte[1024];
            int size;
            while ((size = in.read(buffer)) > 0) {
                out.write(buffer, 0, size);
            }

            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            MyLog.writeStackTrace(MyConst.BUG_CAUGHT_FILE, e);
            throw new RuntimeException(e);
        } finally {
            MyFile.closeQuietly(out);
            MyFile.closeQuietly(in);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CardOrder.class);
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, CategoryRelation.class);
            TableUtils.createTable(connectionSource, English.class);
            TableUtils.createTable(connectionSource, Japanese.class);
            TableUtils.createTable(connectionSource, UsageExample.class);
            TableUtils.createTable(connectionSource, LearningLog.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
