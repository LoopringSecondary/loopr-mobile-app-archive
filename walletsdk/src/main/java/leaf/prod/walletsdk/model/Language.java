/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:35
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import java.util.Locale;

public enum Language {
    zh_CN("zh-Hans", Locale.SIMPLIFIED_CHINESE),
    en_US("en", Locale.ENGLISH),
    zh_Hant("zh-Hant", Locale.TRADITIONAL_CHINESE);

    private final String text;

    private Locale locale;

    Language(String text, Locale locale) {
        this.text = text;
        this.locale = locale;
    }

    public String getText() {
        return text;
    }

    public Locale getLocale() {
        return locale;
    }

    public static Language getLanguage(String text) {
        for (Language language : Language.values()) {
            if (language.getText().equals(text)) {
                return language;
            }
        }
        return Language.zh_CN;
    }
}
