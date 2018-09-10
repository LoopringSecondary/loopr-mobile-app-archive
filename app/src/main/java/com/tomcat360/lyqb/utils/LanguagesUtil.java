package com.tomcat360.lyqb.utils;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 标题:    LanguagesUtil
 * 版本:    V-1.0.0
 */
public class LanguagesUtil {

    public static void changeLanguage(Context context, String type) {

        Configuration configuration = context.getResources().getConfiguration();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        switch (type) {
            case "zh":
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case "en":
                configuration.locale = Locale.ENGLISH;
                break;
        }
        context.getResources().updateConfiguration(configuration, displayMetrics);
    }

    /**
     * 获得当前系统的版本
     */
    public static int getLanguage(Context context) {

        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.contains("en")) {
            return 1;
        } else {
            return 2;
        }
    }
}
