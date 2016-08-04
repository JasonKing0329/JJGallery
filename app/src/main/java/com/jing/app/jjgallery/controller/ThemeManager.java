package com.jing.app.jjgallery.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jing.app.jjgallery.R;

public class ThemeManager {

	private final String TAG = "ThemeManager";

	private static ThemeManager instance;

	public static final String THEME_PREF_KEY = "pref_theme";
	private String[] themeKeys = new String[]{"normal", "dark", "light"};
	
	private int[] themeValues = new int[] {R.style.JJThemeNormal, R.style.JJThemeDark
			, R.style.JJThemeLight};
	private int[] themeDrawables = new int[] {R.drawable.theme_normal, R.drawable.theme_dark
			, R.drawable.theme_light};
	private int[] themeBasicColors = new int[] {R.color.actionbar_bk_normal, R.color.actionbar_bk_dark
			, R.color.actionbar_bk_light};
	private int[] themeWallBkColors = new int[] {R.color.actionbar_bk_wallgalerry, R.color.actionbar_bk_wallgalerry
			, R.color.actionbar_bk_wallgalerry_light};
	private int[] themeDefaultFolder = new int[] {R.drawable.ic_folder_sub, R.drawable.ic_folder_sub
			, R.drawable.ic_folder_sub};
	private int[] themeGdbRSTextColorNormal = new int[] {R.color.gdb_record_text_normal_light, R.color.gdb_record_text_normal_dark
			, R.color.gdb_record_text_normal_light};
	private int[] themeGdbRSTextColorBareback = new int[] {R.color.gdb_record_text_bareback_light, R.color.gdb_record_text_bareback_dark
			, R.color.gdb_record_text_bareback_light};
	
	private ThemeManager() {

	}

	public static ThemeManager getInstance() {
		if (instance == null) {
			instance = new ThemeManager();
		}
		return instance;
	}
	
	public String[] getThemes() {
		return themeKeys;
	}

	public int[] getThemesDrawables() {
		return themeDrawables;
	}
	
	public int getTheme(String key, int where) {
		int valueArray[] = null;
		int theme = 0;
		valueArray = themeValues;
		for (int i = 0; i < themeKeys.length; i ++) {
			if (key.equals(themeKeys[i])) {
				theme = valueArray[i];
				break;
			}
		}
		return theme;
	}

	public int getTheme(int keyIndex) {
		return themeValues[keyIndex];
	}
	
	public static void init(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_PREF_KEY, null);
		if (value == null) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(THEME_PREF_KEY, context.getResources().getStringArray(R.array.theme_key)[2]);
			editor.commit();
		}
	}

	public void saveTheme(Context context, String key) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(THEME_PREF_KEY, key);
		editor.commit();
	}
	
	public int getDefaultTheme(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = null;
		int valueArray[] = null;
		int theme = 0;
		value = preferences.getString(THEME_PREF_KEY, themeKeys[0]);
		valueArray = themeValues;

		for (int i = 0; i < themeKeys.length; i ++) {
			if (value.equals(themeKeys[i])) {
				theme = valueArray[i];
				break;
			}
		}
		return theme;
	}

	public boolean isDarkTheme(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_PREF_KEY, themeKeys[0]);
		if (themeKeys[1].equals(value)) {
			return true;
		}
		return false;
	}

	public boolean isNormalTheme(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_PREF_KEY, themeKeys[0]);
		if (themeKeys[0].equals(value)) {
			return true;
		}
		return false;
	}


	public int getBasicColorResId(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_PREF_KEY, themeKeys[0]);
		int color = themeBasicColors[0];
		for (int i = 0; i < themeKeys.length; i ++) {
			if (value.equals(themeKeys[i])) {
				color = themeBasicColors[i];
				break;
			}
		}
		return color;
	}
	
	public int getBasicColor(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_PREF_KEY, themeKeys[0]);
		int color = themeBasicColors[0];
		for (int i = 0; i < themeKeys.length; i ++) {
			if (value.equals(themeKeys[i])) {
				color = themeBasicColors[i];
				break;
			}
		}
		return context.getResources().getColor(color);
	}
	
	public int getDefFolderResId(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_PREF_KEY, themeKeys[0]);
		Log.d(TAG, "getDefFolderResId value = " + value);
		int resId = themeDefaultFolder[0];
		for (int i = 0; i < themeKeys.length; i ++) {
			if (value.equals(themeKeys[i])) {
				resId = themeDefaultFolder[i];
				Log.d(TAG, "getDefFolderResId i = " + i);
				break;
			}
		}
		return resId;
	}

	public int getWallActionbarColorId(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_PREF_KEY, themeKeys[0]);
		int color = themeWallBkColors[0];
		for (int i = 0; i < themeKeys.length; i ++) {
			if (value.equals(themeKeys[i])) {
				color = themeWallBkColors[i];
				break;
			}
		}
		return color;
	}

	/**
	 * gdb star page, the text color of record's name
	 * @param bareback
	 * @return
     */
	public int getGdbSRTextColorId(Context context, boolean bareback) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_PREF_KEY, themeKeys[0]);
		int[] array = themeGdbRSTextColorNormal;
		if (bareback) {
			array = themeGdbRSTextColorBareback;
		}
		int color = array[0];
		for (int i = 0; i < themeKeys.length; i ++) {
			if (value.equals(themeKeys[i])) {
				color = array[i];
				break;
			}
		}
		return color;
	}

}
