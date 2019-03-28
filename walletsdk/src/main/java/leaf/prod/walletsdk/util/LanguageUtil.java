package leaf.prod.walletsdk.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;

import leaf.prod.walletsdk.manager.LoginDataManager;
import leaf.prod.walletsdk.model.setting.Language;

/**
 * 标题:    LanguageUtil
 * 版本:    V-1.0.0
 */
public class LanguageUtil {

    /**
     * 修改app语言
     *
     * @param context
     * @param language
     * @return
     */
    public static Context changeLanguage(Context context, Language language) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(language.getLocale());
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        return context;
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
