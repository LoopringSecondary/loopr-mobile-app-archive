package com.tomcat360.lyqb.utils;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.lyqb.walletsdk.model.Language;

/**
 * 标题:    LanguageUtil
 * 版本:    V-1.0.0
 */
public class LanguageUtil {

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
    public static Language getLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.contains("en")) {
            return Language.en_US;
        } else {
            return Language.zh_CN;
        }
    }
}
