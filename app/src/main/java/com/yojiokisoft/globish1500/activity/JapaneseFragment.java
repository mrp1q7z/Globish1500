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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yojiokisoft.globish1500.App;
import com.yojiokisoft.globish1500.R;
import com.yojiokisoft.globish1500.dao.CardDao;
import com.yojiokisoft.globish1500.dao.LearningLogDao;
import com.yojiokisoft.globish1500.entity.Japanese;
import com.yojiokisoft.globish1500.entity.LearningLog;
import com.yojiokisoft.globish1500.entity.PartOfSpeech;
import com.yojiokisoft.globish1500.entity.UsageExample;
import com.yojiokisoft.globish1500.utils.MyLog;
import com.yojiokisoft.globish1500.utils.MyDate;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 日本語フラグメント
 * Created by taoka on 14/05/09.
 */
public class JapaneseFragment extends Fragment {
    private OnEventListener mListener;

    public static interface OnEventListener {
        public void onJapanesePageChanged();

        public void onExit();
    }

    private TextView mJapaneseText;
    private TextView mPartOfSpeechText;
    private TextView mExampleText;
    private LinearLayout mMainLayout;
    private LinearLayout mCurtain;
    private LinearLayout mCurtain2;
    private Button mMemorizedButton;
    private boolean mLearningMode;
    private LearningLogDao mLearningLogDao;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private boolean mActiveFlag;

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
        View view = inflater.inflate(R.layout.fragment_japanese, container, false);

        mJapaneseText = (TextView) view.findViewById(R.id.japanese);
        mPartOfSpeechText = (TextView) view.findViewById(R.id.part_of_speech);
        mExampleText = (TextView) view.findViewById(R.id.example);
        mMemorizedButton = (Button) view.findViewById(R.id.memorized_button);
        mMainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
        mCurtain = (LinearLayout) view.findViewById(R.id.curtain);
        mCurtain2 = (LinearLayout) view.findViewById(R.id.curtain2);

        mMemorizedButton.setOnClickListener(mMemorizedButtonClicked);
        mCurtain.setOnClickListener(mCurtainClicked);
        mLearningLogDao = new LearningLogDao();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            mLearningMode = ((MainActivity) getActivity()).getLearningMode();
        } else {
            mLearningMode = false;
        }

        if (mLearningMode) {
            mMemorizedButton.setVisibility(View.INVISIBLE);
        } else {
            mMemorizedButton.setVisibility(View.VISIBLE);
        }

        printCard();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActiveFlag = true;
    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onPause() {
        mActiveFlag = false;
        cancelTimer();
        super.onPause();
    }

    public void printCard() {
        mCurtain.setVisibility(View.VISIBLE);
        mCurtain2.setVisibility(View.VISIBLE);

        Japanese japanese = App.getInstance().getCardDao().getJapanese();
        if (japanese == null) {
            return;
        }
        mJapaneseText.setText(japanese.japanese);
        PartOfSpeech partOfSpeech = PartOfSpeech.getEnum(japanese.part_of_speech);
        mPartOfSpeechText.setText(partOfSpeech.getJapaneseName());
        String text = (App.getInstance().getCardDao().isMemorized() ?
                getString(R.string.cancel_memorized) : getString(R.string.memorized));
        mMemorizedButton.setText(text);

        UsageExample example = App.getInstance().getCardDao().getExample();
        if (example != null) {
            mExampleText.setText(example.usage_example_jp);
        }

        cancelTimer();
    }

    private View.OnClickListener mMemorizedButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CardDao card = App.getInstance().getCardDao();
            LearningLog learningLog = card.getLearningLog();
            if (learningLog == null) {
                return;
            }
            learningLog.memorized = (App.getInstance().getCardDao().isMemorized() ? "0" : "1");
            int ret = mLearningLogDao.update(learningLog);
            MyLog.d("update: memorized_flg=" + learningLog.memorized + ", ret=" + ret);

            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(1000);
            animation.setAnimationListener(mMemorizedAnimationListener);
            mMainLayout.startAnimation(animation);
        }
    };

    private Animation.AnimationListener mMemorizedAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            CardDao card = App.getInstance().getCardDao();
            if (card.isMemorized()) {
                card.setMemorized(false);
            } else {
                card.deleteCard();
            }
            printCard();
            if (card.getCount() <= 0) {
                mListener.onExit();
            } else {
                mListener.onJapanesePageChanged();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public void openCurtain(long delay) {
        // onPauseしたあとに呼び出されるとエラーが発生するので
        if (!mActiveFlag) {
            return;
        }
        if (mTimer == null) {
            mTimer = new Timer(true);
            mTimer.schedule(new CurtainTimerTask(), delay);
        }
    }

    private View.OnClickListener mCurtainClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelTimer();
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out);
            animation.setAnimationListener(mCurtainAnimationListener);
            mCurtain.startAnimation(animation);
            mCurtain2.setVisibility(View.INVISIBLE);

            // 学習中なら学習履歴に保存
            if (!mLearningMode) {
                return;
            }
            Japanese japanese = App.getInstance().getCardDao().getJapanese();
            if (japanese == null) {
                Toast.makeText(getActivity(), "学習履歴の保存に失敗", Toast.LENGTH_LONG).show();
                return;
            }
            mJapaneseText.setText(japanese.japanese);
            LearningLog learningLog = new LearningLog();
            learningLog.learn_date = MyDate.getNowDate();
            learningLog.english_id = japanese.english_id;
            learningLog.memorized = "0";
            int ret = mLearningLogDao.createIfNotExists(learningLog);
            if (ret != 1) {
                Toast.makeText(getActivity(), "学習履歴の保存に失敗: ret=" + ret, Toast.LENGTH_LONG).show();
            }
        }
    };

    private Animation.AnimationListener mCurtainAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mCurtain.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    class CurtainTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    mCurtainClicked.onClick(null);
                }
            });
        }
    }
}
