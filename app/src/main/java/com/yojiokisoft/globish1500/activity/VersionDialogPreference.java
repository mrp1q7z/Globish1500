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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yojiokisoft.globish1500.App;
import com.yojiokisoft.globish1500.R;
import com.yojiokisoft.globish1500.utils.MyResource;

/**
 * 設定画面のバージョンダイアログ
 */
public class VersionDialogPreference extends DialogPreference {
	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 * @param attrs
	 */
	public VersionDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * カスタムコンテンツビューの作成.
	 */
	@Override
	protected View onCreateDialogView() {
		PackageInfo packageInfo = MyResource.getPackageInfo();
		String version = App.getInstance().getString(R.string.app_name) + " Version " + packageInfo.versionName;

		TextView textView = new TextView(this.getContext());
		textView.setText(version);
		textView.setPadding(10, 10, 10, 10);
		return textView;
	}
}
