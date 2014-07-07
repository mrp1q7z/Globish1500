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
package com.yojiokisoft.globish1500;

import android.app.Application;
import android.content.Context;

import com.yojiokisoft.globish1500.dao.CardDao;
import com.yojiokisoft.globish1500.dao.DatabaseHelper;
import com.yojiokisoft.globish1500.utils.MySpeech;

/**
 * Created by taoka on 14/05/15.
 */
public class App extends Application {
    // 唯一のアプリケーションインスタンス
    private static App sInstance;

    // アプリケーションコンテキスト
    private static Context sAppContext;

    // カードDAO
    private static CardDao sCardDao;

    /**
     * コンストラクタ
     */
    public App() {
        sInstance = this;
    }

    /**
     * @return 唯一のアプリケーションインスタンス
     */
    public static App getInstance() {
        if (sAppContext == null) {
            sAppContext = sInstance.getApplicationContext();
        }
        return sInstance;
    }

    /**
     * @return アプリケーションコンテキスト
     */
    public Context getAppContext() {
        return sAppContext;
    }

    /**
     * @return アプリケーションデータの保存領域のパス
     */
    public String getAppDataPath() {
        return sAppContext.getFilesDir().toString();
    }

    /**
     * @return アプリケーションコンテキスト
     */
    public CardDao getCardDao() {
        return sCardDao;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MySpeech.getInstance().init();
        DatabaseHelper.init();

        sCardDao = CardDao.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        MySpeech.getInstance().shutdown();
        DatabaseHelper.destroy();
    }
}
