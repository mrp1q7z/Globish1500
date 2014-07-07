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

import android.speech.tts.TextToSpeech;

import com.yojiokisoft.globish1500.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by taoka on 14/05/30.
 */
public class MySpeech {
    private OnSpeakEndListener mListener;
    private OnCheckTtsDataInstall mCheckListener;

    public interface OnSpeakEndListener {
        public void onSpoke();
    }

    public interface OnCheckTtsDataInstall {
        public void onNotInstall();
    }

    private static class SpeakQueue {
        public String text;
        public OnSpeakEndListener listener;

        public SpeakQueue(String text, OnSpeakEndListener listener) {
            this.text = text;
            this.listener = listener;
        }
    }

    private static MySpeech mInstance = null;
    private TextToSpeech mTts = null;
    private boolean mTtsAvailable = false;
    private List<SpeakQueue> mSpeakQueue = null;

    private MySpeech() {
    }

    public static synchronized MySpeech getInstance() {
        if (mInstance == null) {
            mInstance = new MySpeech();
        }
        return mInstance;
    }

    public void init() {
        if (mTts == null) {
            mTts = new TextToSpeech(App.getInstance().getAppContext(), mTtsInitialized);
        }
    }

    public boolean speak(String text, OnSpeakEndListener listener) {
        if (!mTtsAvailable) {
            if (mSpeakQueue == null) {
                mSpeakQueue = new ArrayList<SpeakQueue>();
            }
            mSpeakQueue.add(new SpeakQueue(text, listener));
            return false;
        }
        if (text == null) {
            return false;
        }
        if (mTts.isSpeaking()) {
            mTts.stop();
        }

        mListener = listener;
        if (listener == null) {
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "endSpeech");
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, param);
        }
        return true;
    }

    public void shutdown() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
            mTts = null;
        }
        mTtsAvailable = false;
        mSpeakQueue = null;
    }

    private TextToSpeech.OnInitListener mTtsInitialized = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (TextToSpeech.SUCCESS != status) {
                return;
            }

            Locale locale = Locale.ENGLISH;
            int ret = mTts.isLanguageAvailable(locale);
            if (ret < TextToSpeech.LANG_AVAILABLE) {
                if (ret == TextToSpeech.LANG_MISSING_DATA) {
                    MyLog.d("音声データがインストールされていません。テキスト読み上げの設定より音声データのインストールをしてください");
                    if (mCheckListener != null) {
                        mCheckListener.onNotInstall();
                    }
                }
                return;
            }

            mTts.setLanguage(locale);
            mTts.setOnUtteranceCompletedListener(mTtsUtteranceCompleted);
            mTtsAvailable = true;

            if (mSpeakQueue != null) {
                SpeakQueue queue = mSpeakQueue.get(0);
                mSpeakQueue.remove(0);
                if (mSpeakQueue.size() <= 0) {
                    mSpeakQueue = null;
                }
                speak(queue.text, queue.listener);
            }
        }
    };

    private TextToSpeech.OnUtteranceCompletedListener mTtsUtteranceCompleted = new TextToSpeech.OnUtteranceCompletedListener() {
        @Override
        public void onUtteranceCompleted(String utteranceId) {
            if (mListener == null) {
                return;
            }
            mListener.onSpoke();
        }
    };

    public void checkTtsDataInstall(OnCheckTtsDataInstall listener) {
        mCheckListener = listener;
    }
}
