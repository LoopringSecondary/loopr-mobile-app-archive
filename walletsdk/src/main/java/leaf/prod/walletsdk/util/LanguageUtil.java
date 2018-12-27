package leaf.prod.walletsdk.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import leaf.prod.walletsdk.manager.LoginDataManager;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.model.UserConfig;

/**
 * 标题:    LanguageUtil
 * 版本:    V-1.0.0
 */
public class LanguageUtil {

    public static void changeLanguage(Context context, Language language) {
        Configuration configuration = context.getResources().getConfiguration();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        UserConfig userConfig = LoginDataManager.getInstance(context).getLocalUser();
        switch (language) {
            case zh_CN:
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                SPUtils.put(context, "language", 2);
                userConfig.setLanguage(Language.zh_CN.getText());
                break;
            case zh_Hant:
                configuration.locale = Locale.TRADITIONAL_CHINESE;
                SPUtils.put(context, "language", 3);
                userConfig.setLanguage(Language.zh_Hant.getText());
                break;
            case en_US:
                configuration.locale = Locale.US;
                SPUtils.put(context, "language", 1);
                userConfig.setLanguage(Language.en_US.getText());
                break;
        }
        LoginDataManager.getInstance(context).updateRemote(userConfig);
        context.getResources().updateConfiguration(configuration, displayMetrics);
    }

    /**
     * 获得当前系统的版本
     */
    public static Language getLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.toString();
        if (language.startsWith("zh_TW") || language.startsWith("zh_HK") || language.startsWith("zh_MO")) {
            return Language.zh_Hant;
        } else if (language.startsWith("zh")) {
            return Language.zh_CN;
        } else {
            return Language.en_US;
        }
    }

    public static Language getSettingLanguage(Context context) {
        LoginDataManager manager = LoginDataManager.getInstance(context);
        if (manager.getLocalUser() != null && !StringUtils.isEmpty(manager.getLocalUser().getLanguage())) {
            String language = manager.getLocalUser().getLanguage();
            return Language.getLanguage(language);
        }
        return getLanguage(context);
    }
}
