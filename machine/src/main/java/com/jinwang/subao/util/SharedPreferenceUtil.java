package com.jinwang.subao.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by michael on 15/7/30.
 * 管理程序中少量的持久数据存储
 */
public class SharedPreferenceUtil {
    public static final String APP_SP_NAME = "SUBAP_SP_NAME";

    private static SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences(APP_SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存整形数据
     * @param context   应用上下文
     * @param key       键
     * @param value     值
     */
    public static void saveData(Context context, String key, int value)
    {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 保存布尔数据
     * @param context   应用上下文
     * @param key       键
     * @param value     值
     */
    public static void saveData(Context context, String key, boolean value)
    {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 保存字符串数据
     * @param context   应用上下文
     * @param key       键
     * @param value     值
     */
    public static void saveData(Context context, String key, String value)
    {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(key, value).apply();
    }

    /**
     * 保存符点型数据
     * @param context   应用上下文
     * @param key       键
     * @param value     值
     */
    public static void saveData(Context context, String key, float value)
    {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putFloat(key, value).apply();
    }

    /**
     * 保存字符串集合数据
     * @param context   应用上下文
     * @param key       键
     * @param value     值
     */
    public static void saveData(Context context, String key, Set<String> value)
    {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putStringSet(key, value).apply();
    }

    /**
     * 保存长整型数据
     * @param context   应用上下文
     * @param key       键
     * @param value     值
     */
    public static void saveData(Context context, String key, long value)
    {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putLong(key, value).apply();
    }

    /**
     * 获取布尔数据
     * @param context   应用上下文
     * @param key       键
     */
    public static boolean getBooleanData(Context context, String key)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(key, false);
    }

    /**
     * 获取整型数据
     * @param context   应用上下文
     * @param key       键
     */
    public static int getIntData(Context context, String key)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(key, 0);
    }

    /**
     * 获取字符串数据
     * @param context   应用上下文
     * @param key       键
     */
    public static String getStringData(Context context, String key)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(key, null);
    }

    /**
     * 获取字符串集合数据
     * @param context   应用上下文
     * @param key       键
     */
    public static Set<String> getStringSetData(Context context, String key)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getStringSet(key, null);
    }

    /**
     * 获取浮点数据
     * @param context   应用上下文
     * @param key       键
     */
    public static float getFloatData(Context context, String key)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getFloat(key, 0);
    }

    /**
     * 获取长整型数据
     * @param context   应用上下文
     * @param key       键
     */
    public static long getLongData(Context context, String key)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getLong(key, 0);
    }
}
