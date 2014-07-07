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
package com.yojiokisoft.globish1500.activity;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.yojiokisoft.globish1500.R;
import com.yojiokisoft.globish1500.dao.SettingDao;
import com.yojiokisoft.globish1500.utils.MyConst;
import com.yojiokisoft.globish1500.utils.MyMail;
import com.yojiokisoft.globish1500.utils.MyResource;

/**
 * 設定アクティビティ
 */
public class SettingsActivity extends PreferenceActivity {
    private final static String BR = System.getProperty("line.separator");
    private SettingDao mSettingDao;

    /**
     * アクティビティの初期化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingDao = SettingDao.getInstance();
        addPreferencesFromResource(R.xml.settings);

        setSummary();

        // お問い合わせの設定値をクリア
        // 既に選択中のものを選択してもイベントが発生しないから
        clearPreference(MyConst.PK_INQUIRY);

        // 設定値が変更された時のイベントリスナーを登録
        CheckBoxPreference prefVibrator = (CheckBoxPreference) getPreferenceScreen()
                .findPreference(MyConst.PK_AUTO_SPEAK);
        prefVibrator.setOnPreferenceChangeListener(mAutoSpeakChanged);

        ListPreference prefInquiry = (ListPreference) getPreferenceScreen().findPreference(MyConst.PK_INQUIRY);
        prefInquiry.setOnPreferenceChangeListener(mInquiryChanged);
    }

    /**
     * 設定値のクリア.
     *
     * @param key キー
     */
    private void clearPreference(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 文字列をBRまででカットする.
     *
     * @param str 対象の文字列
     * @return BRまでの文字列
     */
    private String indexOfBr(String str) {
        int find = str.indexOf(BR);
        if (find == -1) {
            return str;
        }
        return str.substring(0, find);
    }

    /**
     * サマリーに現在の設定値をセットする.
     */
    private void setSummary() {
        String[] prefKeys = {MyConst.PK_AUTO_SPEAK, MyConst.PK_VERSION, MyConst.PK_INQUIRY};
        Preference pref;
        String summary;
        String nowVal;
        for (int i = 0; i < prefKeys.length; i++) {
            pref = getPreferenceScreen().findPreference(prefKeys[i]);
            summary = indexOfBr(pref.getSummary().toString());
            nowVal = getNowValue(prefKeys[i]);
            if (nowVal != null) {
                summary += BR + getString(R.string.now_setting) + nowVal;
            }
            pref.setSummary(getSummarySpannableString(summary));
        }
    }

    /**
     * 現在値の取得.
     *
     * @param key キー
     * @return 現在値
     */
    private String getNowValue(String key) {
        if (MyConst.PK_AUTO_SPEAK.equals(key)) {
            return mSettingDao.getAutoSpeakString();
        } else if (MyConst.PK_VERSION.equals(key)) {
            PackageInfo packageInfo = MyResource.getPackageInfo();
            return "Version " + packageInfo.versionName;
        }
        return null;
    }

    /**
     * 文字列をサマリー用のマークアップ可能な文字列に変換
     *
     * @param summary 文字列
     * @return サマリー用の文字列
     */
    private SpannableString getSummarySpannableString(String summary) {
        SpannableString span;
        span = new SpannableString(summary);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_settingColor)), 0,
                span.length(), 0);
        return span;
    }

    /**
     * 自動で発音するが変更された.
     */
    private final OnPreferenceChangeListener mAutoSpeakChanged = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            CheckBoxPreference pref = (CheckBoxPreference) preference;
            String summary = indexOfBr(pref.getSummary().toString());
            summary += BR + getString(R.string.now_setting) + mSettingDao.autoSpeakVal2Key((Boolean) newValue);
            pref.setSummary(getSummarySpannableString(summary));
            return true;
        }
    };

    /**
     * 問い合わせが変更された.
     */
    private final OnPreferenceChangeListener mInquiryChanged = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String inquiry = (String) newValue;
            if (!"".equals(inquiry)) {
                String inquiryKey = mSettingDao.inquiryVal2Key(inquiry);
                String subject = "[" + inquiryKey + "]" + getString(R.string.app_name);
                // メール送信
                MyMail.Builder.newInstance(getApplicationContext())
                        .setTo(getString(R.string.developer_email))
                        .setSubject(subject)
                        .send();
            }

            return false; // データの変更はしない
        }
    };
}
