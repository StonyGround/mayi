package com.mayizaixian.myzx.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/2/16.
 */
public class CacheUtils {

    private static SharedPreferences sp;

    /**
     * 获取软件保存的参数
     *
     * @param context 上下文
     * @param key     取出参数的key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, false);// 默认返回false
    }

    /**
     * 保存boolean类型的软件参数
     *
     * @param context
     * @param key
     * @param values  要保存的值
     */
    public static void setBoolean(Context context, String key, boolean values) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, values).commit();
    }

    /**
     * 缓存数据
     *
     * @param context
     * @param key
     * @param values
     */
    public static void setString(Context context, String key, String values) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, values).commit();
    }

    /**
     * 得到缓存的数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, "");// 默认返回false
    }

    /**
     * 缓存数据
     *
     * @param context
     * @param key
     * @param values
     */
    public static void setLong(Context context, String key, Long values) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putLong(key, values).commit();
    }

    /**
     * 得到缓存的数据
     *
     * @param context
     * @param key
     * @return
     */
    public static Long getLong(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getLong(key, 0);// 默认返回false
    }

    public static void delString(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key);
    }
}
