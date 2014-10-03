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

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yojiokisoft.globish1500.App;
import com.yojiokisoft.globish1500.R;
import com.yojiokisoft.globish1500.dao.SettingDao;
import com.yojiokisoft.globish1500.entity.English;
import com.yojiokisoft.globish1500.entity.UsageExample;
import com.yojiokisoft.globish1500.utils.MySpeech;

/**
 * 英語フラグメント
 * Created by taoka on 14/05/09.
 */
public class EnglishFragment extends Fragment {
    private OnEventListener mListener;

    public static interface OnEventListener {
        public void onEnglishPageChanged();

        public void onSpoke();
    }

    private TextView mEnglishText;
    private TextView mPhoneticSymbolText;
    private TextView mExampleText;
    private Button mPrevButton;
    private Button mNextButton;
    private Handler mHandler = new Handler();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof OnEventListener)) {
            throw new ClassCastException("ActivityがOnPageChangedListenerを実装していません");
        }
        mListener = ((OnEventListener) activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_english, container, false);

        mEnglishText = (TextView) view.findViewById(R.id.english_text);
        mPhoneticSymbolText = (TextView) view.findViewById(R.id.phonetic_symbol);
        mExampleText = (TextView) view.findViewById(R.id.example);
        mPrevButton = (Button) view.findViewById(R.id.prev_button);
        mNextButton = (Button) view.findViewById(R.id.next_button);

        mPrevButton.setOnClickListener(mPrevButtonClicked);
        mNextButton.setOnClickListener(mNextButtonClicked);

        ImageButton speakButton = (ImageButton) view.findViewById(R.id.speak_button);
        speakButton.setOnClickListener(mSpeakButtonClicked);

        Typeface typeface = Typeface.createFromAsset(App.getInstance().getAppContext().getAssets(), "DoulosSIL-R.ttf");
        mPhoneticSymbolText.setTypeface(typeface);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        printAndSpeechCard();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mSpokeRunnable);
    }

    private View.OnClickListener mPrevButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            App.getInstance().getCardDao().prevCard();
            printAndSpeechCard();
            mListener.onEnglishPageChanged();
        }
    };

    private View.OnClickListener mNextButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            App.getInstance().getCardDao().nextCard();
            printAndSpeechCard();
            mListener.onEnglishPageChanged();
        }
    };

    private View.OnClickListener mSpeakButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MySpeech.getInstance().speak(mEnglishText.getText().toString(), null);
        }
    };

    public void printAndSpeechCard() {
        English english = App.getInstance().getCardDao().getEnglish();
        if (english == null) {
            return;
        }
        printCard(english);
        speechCard(english);
    }

    private void printCard(English english) {
        mEnglishText.setText(english.english);
        mPhoneticSymbolText.setText(english.phonetic_symbol);
        mEnglishText.setBackgroundColor(getResources().getColor(R.color.dark_backColor));
        mPhoneticSymbolText.setBackgroundColor(getResources().getColor(R.color.dark_backColor));

        if (App.getInstance().getCardDao().isPrevCard()) {
            mPrevButton.setVisibility(View.VISIBLE);
        } else {
            mPrevButton.setVisibility(View.INVISIBLE);
        }

        if (App.getInstance().getCardDao().isNextCard()) {
            mNextButton.setVisibility(View.VISIBLE);
        } else {
            mNextButton.setVisibility(View.INVISIBLE);
        }

        UsageExample example = App.getInstance().getCardDao().getExample();
        if (example == null) {
            return;
        }
        mExampleText.setText(example.usage_example_en);
    }

    private void speechCard(English english) {
        if (SettingDao.getInstance().getAutoSpeak()) {
            MySpeech.getInstance().speak(english.english, mSpoke);
        } else {
            mHandler.postDelayed(mSpokeRunnable, 1000);
        }
    }

    private MySpeech.OnSpeakEndListener mSpoke = new MySpeech.OnSpeakEndListener() {
        @Override
        public void onSpoke() {
            mListener.onSpoke();
        }
    };

    private final Runnable mSpokeRunnable = new Runnable() {
        @Override
        public void run() {
            mListener.onSpoke();
        }
    };
}
