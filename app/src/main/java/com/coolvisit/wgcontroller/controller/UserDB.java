package com.coolvisit.wgcontroller.controller;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.coolvisit.wgcontroller.ControllerApp;

public class UserDB {
	public static final String PREFERENCES_USER = "user_pref";
	public static final String KEY_CARD_ID = "KEY_CARD_ID";
	public static final String KEY_DEV_ID = "KEY_DEV_ID";
	public static final String KEY_DOOR = "KEY_DOOR";
	public static final String KEY_IP = "KEY_IP";
	public static final String KEY_PORT = "KEY_PORT";
	public static final String KEY_OPEN_API = "KEY_OPEN_API";

	private static SharedPreferences sSharedPrefs = ControllerApp.getInstance().getSharedPreferences(PREFERENCES_USER, 0);
	private static Editor sEditor = sSharedPrefs.edit();


	public static void setValue(String key,String value)
	{
		sEditor.putString(key,value).commit();
	}
	
	public static String getValue(String key,String s1)
	{
		return sSharedPrefs.getString(key, s1);
	}


	public static void clean()
	{
		sSharedPrefs.edit().clear();
	}
}
