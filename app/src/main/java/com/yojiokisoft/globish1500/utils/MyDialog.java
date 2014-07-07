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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * ダイアログ支援ユーティリティ.
 */
public class MyDialog {
    /**
     * コンストラクタ.
     *
     * @param builder MyDialog.Builderオブジェクト
     */
    public MyDialog(Builder builder) {
        AlertDialog.Builder alert = new AlertDialog.Builder(builder.mContext);
        if (builder.mTitle != null) {
            alert.setTitle(builder.mTitle);
        }
        if (builder.mMessage != null) {
            alert.setMessage(builder.mMessage);
        }
        if (builder.mPositiveLabel != null) {
            alert.setPositiveButton(builder.mPositiveLabel, builder.mPositiveClickListener);
        }
        if (builder.mNegativeLabel != null) {
            alert.setNegativeButton(builder.mNegativeLabel, builder.mNegativeClickListener);
        }
        alert.show();
    }

    /**
     * ビルダークラス
     */
    public static class Builder {
        private Context mContext;
        private String mTitle = null;
        private String mMessage = null;
        private String mPositiveLabel = null;
        private String mNegativeLabel = null;
        private DialogInterface.OnClickListener mPositiveClickListener = null;
        private DialogInterface.OnClickListener mNegativeClickListener = null;

        /**
         * コンストラクタは外部に公開しません。
         * このインスタンスを得るにはnewInstanceメソッドを使ってください.
         */
        private Builder() {
            ;
        }

        /**
         * 新しいインスタンスを得る.
         *
         * @param context
         * @return
         */
        public static Builder newInstance(Context context) {
            Builder builder = new Builder();
            builder.mContext = context;
            return builder;
        }

        /**
         * タイトルをセットする.
         *
         * @param title
         * @return builderオブジェクト
         */
        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        /**
         * メッセージをセットする.
         *
         * @param message
         * @return builderオブジェクト
         */
        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        /**
         * ポジティブボタンのラベルをセットする.
         *
         * @param label
         * @return buiderオブジェクト
         */
        public Builder setPositiveLabel(String label) {
            this.mPositiveLabel = label;
            return this;
        }

        /**
         * ネガティブボタンのラベルをセットする.
         *
         * @param label
         * @return buiderオブジェクト
         */
        public Builder setNegativeLabel(String label) {
            this.mNegativeLabel = label;
            return this;
        }

        /**
         * ポジティブボタンのクリックリスナーをセットする.
         *
         * @param listener
         * @return buiderオブジェクト
         */
        public Builder setPositiveClickListener(DialogInterface.OnClickListener listener) {
            this.mPositiveClickListener = listener;
            return this;
        }

        /**
         * ネガティブボタンのクリックリスナーをセットする.
         *
         * @param listener
         * @return buiderオブジェクト
         */
        public Builder setNegativeClickListener(DialogInterface.OnClickListener listener) {
            this.mNegativeClickListener = listener;
            return this;
        }

        /**
         * ダイアログを表示する.
         *
         * @return MyDialogオブジェクト
         */
        public MyDialog show() {
            return new MyDialog(this);
        }
    }
}
