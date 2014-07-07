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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.yojiokisoft.globish1500.App;
import com.yojiokisoft.globish1500.R;
import com.yojiokisoft.globish1500.utils.AdCatalogUtils;
import com.yojiokisoft.globish1500.utils.MyConst;
import com.yojiokisoft.globish1500.utils.MyLog;

/**
 * メイン（カード）アクティビティ
 */
public class MainActivity extends ActionBarActivity implements AdListener,
        EnglishFragment.OnEventListener, JapaneseFragment.OnEventListener {
    private AdView adViewBanner;
    private boolean mLearningMode;
    private String mLearnDate;
    private boolean mOmitMemorized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getExtras();
        queryData();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.up_container, new EnglishFragment(), "english")
                .add(R.id.down_container, new JapaneseFragment(), "japanese")
                .commit();

        AdRequest adRequest = AdCatalogUtils.createAdRequest();
        adViewBanner = (AdView) findViewById(R.id.adViewBanner);
        adViewBanner.setAdListener(this);
        adViewBanner.loadAd(adRequest);
    }

    private void queryData() {
        reloadData();
        App.getInstance().getCardDao().shuffle();
    }

    private void reloadData() {
        if (mLearnDate == null) {
            if (mLearningMode) {
                App.getInstance().getCardDao().queryForNotLearning();
            } else {
                App.getInstance().getCardDao().queryForMemorized(mOmitMemorized);
            }
        } else {
            App.getInstance().getCardDao().queryForLearnDate(mLearnDate, mOmitMemorized);
        }
    }

    private void getExtras() {
        mLearningMode = true;
        mLearnDate = null;
        mOmitMemorized = true;

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        if (extras.containsKey(MyConst.EN_LEARNING_MODE)) {
            try {
                mLearningMode = extras.getBoolean(MyConst.EN_LEARNING_MODE);
            } catch (ClassCastException e) {
                MyLog.d("Could not cast extra to expected type, the field is left to its default value", e);
            }
        }
        if (extras.containsKey(MyConst.EN_LEARN_DATE)) {
            try {
                mLearnDate = extras.getString(MyConst.EN_LEARN_DATE);
                MyLog.d("mLearnDate=" + mLearnDate);
            } catch (ClassCastException e) {
                MyLog.d("Could not cast extra to expected type, the field is left to its default value", e);
            }
        }
        if (extras.containsKey(MyConst.EN_OMIT_MEMORIZED)) {
            try {
                mOmitMemorized = extras.getBoolean(MyConst.EN_OMIT_MEMORIZED);
                MyLog.d("mOmitMemorized=" + mOmitMemorized);
            } catch (ClassCastException e) {
                MyLog.d("Could not cast extra to expected type, the field is left to its default value", e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (adViewBanner != null) {
            adViewBanner.destroy();
        }
        super.onDestroy();
    }

    private void setMenuIcon(MenuItem item) {
        if (mOmitMemorized) {
            item.setIcon(R.drawable.ic_action_filter_on);
        } else {
            item.setIcon(R.drawable.ic_action_filter_off);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        MenuItem item = menu.findItem(R.id.action_omit_memorized);
        setMenuIcon(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_omit_memorized) {
            mOmitMemorized = !mOmitMemorized;
            setMenuIcon(item);
            queryData();
            invalidateFragment();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(App.getInstance().getAppContext(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(App.getInstance().getAppContext(), UsageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveAd(Ad ad) {
        Log.d("Banners_class", "I received an ad");
    }

    @Override
    public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {
        Log.d("Banners_class", "I failed to receive an ad");
    }

    @Override
    public void onPresentScreen(Ad ad) {
        Log.d("Banners_class", "Presenting screen");
    }

    @Override
    public void onDismissScreen(Ad ad) {
        Log.d("Banners_class", "Dismissing screen");
    }

    @Override
    public void onLeaveApplication(Ad ad) {
        Log.d("Banners_class", "Leaving application");
    }

    @Override
    public void onEnglishPageChanged() {
        FragmentManager fm = getSupportFragmentManager();
        JapaneseFragment fragment = (JapaneseFragment) fm.findFragmentByTag("japanese");
        fragment.printCard();
    }

    @Override
    public void onSpoke() {
        if (mLearningMode) {
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        JapaneseFragment fragment = (JapaneseFragment) fm.findFragmentByTag("japanese");
        fragment.openCurtain(1000);
    }

    public boolean getLearningMode() {
        return mLearningMode;
    }

    private void invalidateFragment() {
        if (App.getInstance().getCardDao().getCount() <= 0) {
            finish();
        }

        FragmentManager fm = getSupportFragmentManager();

        EnglishFragment englishFragment = (EnglishFragment) fm.findFragmentByTag("english");
        englishFragment.printAndSpeechCard();

        JapaneseFragment japaneseFragment = (JapaneseFragment) fm.findFragmentByTag("japanese");
        japaneseFragment.printCard();
    }

    @Override
    public void onJapanesePageChanged() {
        FragmentManager fm = getSupportFragmentManager();
        EnglishFragment fragment = (EnglishFragment) fm.findFragmentByTag("english");
        fragment.printAndSpeechCard();
    }

    @Override
    public void onExit() {
        finish();
    }
}