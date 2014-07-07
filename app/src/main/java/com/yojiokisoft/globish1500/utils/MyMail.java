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

import android.content.Context;
import android.content.Intent;

/**
 * メールユーティリティ
 */
public final class MyMail {
    /**
     * コンストラクタ.
     *
     * @param builder MyMail.Builderオブジェクト
     */
    public MyMail(Builder builder) {
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        // 宛先を指定
        if (builder.mTo != null) {
            intent.putExtra(Intent.EXTRA_EMAIL, builder.mTo);
        }
        // CCを指定
        if (builder.mCc != null) {
            intent.putExtra(Intent.EXTRA_CC, builder.mCc);
        }
        // BCCを指定
        if (builder.mBcc != null) {
            intent.putExtra(Intent.EXTRA_BCC, builder.mBcc);
        }
        // 件名を指定
        if (builder.mSubject != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, builder.mSubject);
        }
        // 本文を指定
        if (builder.mBody != null) {
            intent.putExtra(Intent.EXTRA_TEXT, builder.mBody);
        }

        // Intentを発行
        Intent it = Intent.createChooser(intent, "Choose Email Client");
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        builder.mContext.startActivity(it);
    }

    /**
     * ビルダークラス
     */
    public static class Builder {
        private Context mContext;
        private String[] mTo = null;
        private String[] mCc = null;
        private String[] mBcc = null;
        private String mSubject = null;
        private String mBody = null;

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
         * 宛先をセットする.
         *
         * @param to
         * @return builderオブジェクト
         */
        public Builder setTo(final String to) {
            this.mTo = new String[]{to};
            return this;
        }

        /**
         * CCをセットする.
         *
         * @param cc
         * @return builderオブジェクト
         */
        public Builder setCc(String cc) {
            this.mCc = new String[]{cc};
            return this;
        }

        /**
         * BCCをセットする.
         *
         * @param bcc
         * @return builderオブジェクト
         */
        public Builder setBcc(String bcc) {
            this.mBcc = new String[]{bcc};
            return this;
        }

        /**
         * 表題をセットする.
         *
         * @param subject
         * @return builderオブジェクト
         */
        public Builder setSubject(String subject) {
            this.mSubject = subject;
            return this;
        }

        /**
         * 本文をセットする.
         *
         * @param body
         * @return builderオブジェクト
         */
        public Builder setBody(String body) {
            this.mBody = body;
            return this;
        }

        /**
         * メールを送信する.
         * 実際の送信はIntent.ACTION_SENDの連携先のアプリが行う.
         *
         * @return MyMailオブジェクト
         */
        public MyMail send() {
            return new MyMail(this);
        }
    }
}
