/*
 * Copyright (C) 2013 YojiokiSoft
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ファイルに関するユーティリティ集.
 */
public class MyFile {
	/**
	 * 指定パス内で指定拡張子を持つファイル名の一覧を返す。
	 * サブフォルダは検索しません。
	 * extensionがnullの場合はすべてのファイルを返す。
	 * 見つからない場合は長さ０のリストを返す.
	 * 
	 * @param path ファイルパス
	 * @param extension 拡張子
	 * @return ファイル名一覧
	 */
	public static List<String> getFileList(String path, String extension) {
		List<String> list = new ArrayList<String>();
		File filePath = new File(path);
		final File[] files;
		if (extension == null) {
			files = filePath.listFiles();
		} else {
			files = filePath.listFiles(getFileExtensionFilter(extension));
		}
		if (files == null) {
			return list;
		}
		for (File file : files) {
			list.add(file.getName());
		}
		return list;
	}

	/**
	 * 拡張子によるフィルタリング、FilenameFilterの実装.
	 * 
	 * @param extension 拡張子
	 * @return FilenameFilter
	 */
	public static FilenameFilter getFileExtensionFilter(String extension) {
		final String _extension = extension;
		return new FilenameFilter() {
			public boolean accept(File file, String name) {
				boolean ret = name.endsWith(_extension);
				return ret;
			}
		};
	}

	/**
	 * パスとファイル名を統合したフルパスを返す.
	 * 
	 * @param dirPath パス
	 * @param fileName ファイル名
	 * @return 統合したパス
	 */
	public static String pathCombine(String dirPath, String fileName) {
		return new File(dirPath, fileName).getPath();
	}

	/**
	 * InputStreamを閉じ、nullの場合には閉じるのを回避した上で、SQLExceptionの発生を隠蔽します.
	 * 
	 * @param input 閉じる対象。nullでも、既にclose済みであっても構わない
	 */
	public static void closeQuietly(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * OputputStreamを閉じ、nullの場合には閉じるのを回避した上で、SQLExceptionの発生を隠蔽します.
	 * 
	 * @param output 閉じる対象。nullでも、既にclose済みであっても構わない
	 */
	public static void closeQuietly(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * BufferedReaderを閉じ、nullの場合には閉じるのを回避した上で、SQLExceptionの発生を隠蔽します.
	 *
	 * @param br 閉じる対象。nullでも、既にclose済みであっても構わない
	 */
	public static void closeQuietly(BufferedReader br) {
		try {
			if (br != null) {
				br.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}
}
