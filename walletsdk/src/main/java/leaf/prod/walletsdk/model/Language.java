/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:35
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

public enum Language {
    zh_CN("zh-Hans"),
    en_US("en"),
    zh_Hant("zh-Hant");

    private final String text;

    Language(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
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
