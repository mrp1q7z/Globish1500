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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yojiokisoft.globish1500.App;
import com.yojiokisoft.globish1500.R;
import com.yojiokisoft.globish1500.dao.EnglishDao;
import com.yojiokisoft.globish1500.dao.LearningLogDao;
import com.yojiokisoft.globish1500.entity.LearningLogSum;
import com.yojiokisoft.globish1500.utils.MyConst;
import com.yojiokisoft.globish1500.utils.MyDate;
import com.yojiokisoft.globish1500.utils.MyDialog;
import com.yojiokisoft.globish1500.utils.MySpeech;

import java.util.List;

/**
 * 開始アクティビティ
 */
public class StartActivity extends ActionBarActivity {
    private Activity mActivity;
    private Button mNewLearnButton;
    private Button mNotMemorizeButton;
    private ListView mLearnList;
    private boolean mOmitMemorized; // 覚えたものは除く
    private EnglishDao mEnglishDao;
    private LearningLogDao mLearningLogDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_start);

        mOmitMemorized = true;

        mNewLearnButton = (Button) findViewById(R.id.learn_button);
        mNewLearnButton.setOnClickListener(mNewLearnButtonClicked);

        mNotMemorizeButton = (Button) findViewById(R.id.not_memorize_button);
        mNotMemorizeButton.setOnClickListener(mNotMemorizeButtonClicked);

        mEnglishDao = new EnglishDao();
        mLearningLogDao = new LearningLogDao();

        List<LearningLogSum> list = mLearningLogDao.queryForMemorizedGroupByLearnDate("0");
        BaseAdapter adapter = new LearnListAdapter(this, list);

        mLearnList = (ListView) findViewById(R.id.learn_list);
        mLearnList.setAdapter(adapter);
        mLearnList.setOnItemClickListener(mLearnListItemClicked);

        MySpeech.getInstance().checkTtsDataInstall(mCheckTtsDataInstall);
    }

    private MySpeech.OnCheckTtsDataInstall mCheckTtsDataInstall = new MySpeech.OnCheckTtsDataInstall() {
        @Override
        public void onNotInstall() {
            MyDialog.Builder.newInstance(mActivity)
                    .setMessage("音声データがインストールされていません。テキスト読み上げの設定より音声データのインストールをしてください")
                    .setPositiveLabel("インストール")
                    .setPositiveClickListener(mInstallTtsData)
                    .setNegativeLabel("キャンセル")
                    .show();
        }
    };

    private DialogInterface.OnClickListener mInstallTtsData = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        printCount();
    }

    private int getNotMemorizeCount() {
        int cnt;
        if (mOmitMemorized) {
            cnt = mLearningLogDao.getCountByMemorized("0");
        } else {
            cnt = mLearningLogDao.getCount();
        }
        return cnt;
    }

    /**
     * @return まだ学習してない単語の件数
     */
    private int getNotLearnCount() {
        return mEnglishDao.getCount() - mLearningLogDao.getCount();
    }

    private void printCount() {
        String label = String.format(getString(R.string.new_learn), getNotLearnCount());
        mNewLearnButton.setText(label);

        int cnt = getNotMemorizeCount();
        label = String.format(getString(R.string.not_memorize), cnt);
        mNotMemorizeButton.setText(label);

        List<LearningLogSum> list;
        if (mOmitMemorized) {
            list = mLearningLogDao.queryForMemorizedGroupByLearnDate("0");
        } else {
            list = mLearningLogDao.queryForAllGroupByLearnDate();
        }
        ((LearnListAdapter) mLearnList.getAdapter()).setItems(list);
        mLearnList.invalidateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_omit_memorized) {
            mOmitMemorized = !mOmitMemorized;
            printCount();
            if (mOmitMemorized) {
                item.setIcon(R.drawable.ic_action_filter_on);
            } else {
                item.setIcon(R.drawable.ic_action_filter_off);
            }
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

    /**
     * 新たに覚えるボタンのクリックリスナー
     */
    private View.OnClickListener mNewLearnButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getNotLearnCount() <= 0) {
                return;
            }
            Intent intent = new Intent(App.getInstance().getAppContext(), MainActivity.class);
            intent.putExtra(MyConst.EN_LEARNING_MODE, true);
            intent.putExtra(MyConst.EN_OMIT_MEMORIZED, mOmitMemorized);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    /**
     * まだ覚えていないものボタンのクリックリスナー
     */
    private View.OnClickListener mNotMemorizeButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getNotMemorizeCount() <= 0) {
                return;
            }
            Intent intent = new Intent(App.getInstance().getAppContext(), MainActivity.class);
            intent.putExtra(MyConst.EN_LEARNING_MODE, false);
            intent.putExtra(MyConst.EN_OMIT_MEMORIZED, mOmitMemorized);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    /**
     * 学習履歴リストのクリックリスナー
     */
    private AdapterView.OnItemClickListener mLearnListItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            LearningLogSum item = (LearningLogSum) listView.getItemAtPosition(position);

            Intent intent = new Intent(App.getInstance().getAppContext(), MainActivity.class);
            intent.putExtra(MyConst.EN_LEARNING_MODE, false);
            intent.putExtra(MyConst.EN_OMIT_MEMORIZED, mOmitMemorized);
            intent.putExtra(MyConst.EN_LEARN_DATE, item.learn_date);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    /**
     * 学習履歴一覧アダプター
     */
    public class LearnListAdapter extends BaseAdapter {
        private Activity mActivity;
        private List<LearningLogSum> mItems;

        /**
         * コンストラクタ.
         *
         * @param activity アクティビティ
         * @param items    アイテムリスト
         */
        public LearnListAdapter(Activity activity, List<LearningLogSum> items) {
            super();
            mActivity = activity;
            mItems = items;
        }

        /**
         * アイテムのセット.
         *
         * @param items アイテムリスト
         */
        public void setItems(List<LearningLogSum> items) {
            mItems = items;
        }

        /**
         * @see BaseAdapter#getCount()
         */
        @Override
        public int getCount() {
            if (mItems == null) {
                return 0;
            }
            return mItems.size();
        }

        /**
         * @see BaseAdapter#getItem(int)
         */
        @Override
        public Object getItem(int pos) {
            if (mItems == null) {
                return null;
            }
            return mItems.get(pos);
        }

        /**
         * @see BaseAdapter#getItemId(int)
         */
        @Override
        public long getItemId(int pos) {
            return pos;
        }

        /**
         * @see BaseAdapter#getView(int, View, ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewWrapper wrapper;

            if (view == null) {
                view = mActivity.getLayoutInflater().inflate(R.layout.row_learn_list, null);
                wrapper = new ViewWrapper(view);
                view.setTag(wrapper);
            } else {
                wrapper = (ViewWrapper) view.getTag();
            }

            LearningLogSum item = mItems.get(position);
            wrapper.title.setText(getNDaysAgo(item.learn_date) + "学習したもの");
            wrapper.count.setText("(" + item.words_count + ")");

            return view;
        }

        private String getNDaysAgo(String date) {
            int n = MyDate.differenceDays(MyDate.getNowDate(), date);
            if (n == 0) {
                return "今日";
            } else {
                return n + "日前に";
            }
        }

        /**
         * ビューを扱いやすくするためのラッパー.
         */
        private class ViewWrapper {
            public final TextView title;
            public final TextView count;

            ViewWrapper(View view) {
                this.title = (TextView) view.findViewById(R.id.title_text);
                this.count = (TextView) view.findViewById(R.id.count_text);
            }
        }
    }
}
