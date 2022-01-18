package com.example.oneread.Common;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class SharedPrefs {
    private static SharedPrefs instance;
    private static SharedPreferences sharedPreferences;

    private SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(Common.shareRef, Context.MODE_PRIVATE);
    }

    public static SharedPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefs(context);
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        if (defaultValue instanceof String) {
            return (T) sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(sharedPreferences.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(sharedPreferences.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(sharedPreferences.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(sharedPreferences.getLong(key, (Long) defaultValue));
        }
        return null;
    }

    public <T> T get(String key, Class<T> tClass, T defaultValue) {
        return (T) new Gson().fromJson(sharedPreferences.getString(key, (String) defaultValue), tClass);
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            editor.putString(key, new Gson().toJson(data));
        }
        editor.apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}