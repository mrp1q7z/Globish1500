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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yojiokisoft.globish1500.App;
import com.yojiokisoft.globish1500.R;
import com.yojiokisoft.globish1500.utils.MyConst;

/**
 * 設定情報のDAO
 */
public class SettingDao {
    private static SettingDao mInstance = null;
    private static SharedPreferences mSharedPref = null;
    private static Context mContext;
    private static String[] mInquiryKey;
    private static String[] mInquiryVal;

    /**
     * コンストラクタは公開しない
     * インスタンスを取得する場合は、getInstanceを使用する.
     */
    private SettingDao() {
    }

    /**
     * インスタンスの取得.
     *
     * @return SettingDao
     */
    public static SettingDao getInstance() {
        if (mInstance == null) {
            mInstance = new SettingDao();
            mContext = App.getInstance().getAppContext();
            mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            mInquiryKey = mContext.getResources().getStringArray(R.array.inquiry_key);
            mInquiryVal = mContext.getResources().getStringArray(R.array.inquiry_val);
        }
        return mInstance;
    }

    /**
     * 自動で発音するを値からキーに変換
     * 例：true -> オン
     *
     * @param isChecked ブール値
     * @return 文字列
     */
    public String autoSpeakVal2Key(boolean isChecked) {
        String key;

        if (isChecked) {
            key = mContext.getString(R.string.auto_speak_on);
        } else {
            key = mContext.getString(R.string.auto_speak_off);
        }
        return key;
    }

    /**
     * お問い合わせを値からキーに変換
     * 例：questions -> ご質問
     *
     * @param val 値
     * @return キー値
     */
    public String inquiryVal2Key(String val) {
        String key = null;

        for (int i = 0; i < mInquiryVal.length; i++) {
            if (mInquiryVal[i].equals(val)) {
                key = mInquiryKey[i];
                break;
            }
        }
        return key;
    }

    /**
     * @return 自動で発音するのON/OFF状態
     */
    public boolean getAutoSpeak() {
        return mSharedPref.getBoolean(MyConst.PK_AUTO_SPEAK, true);
    }

    /**
     * @return 自動で発音するのON/OFF状態を文字列で
     */
    public String getAutoSpeakString() {
        return autoSpeakVal2Key(mSharedPref.getBoolean(MyConst.PK_AUTO_SPEAK, true));
    }
}
