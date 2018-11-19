/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-13 3:33 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.utils;

public class PasswordUtils {

    /**
     * 判断密码强弱
     *
     * @param password
     * @return
     */
    public static int checkPasswordLevel(String password) {
        String regexZ = "\\d*";         // 只有数字
        String regexS = "[a-zA-Z]+";    // 只有字母
        String regexT = "\\W+$";        // 特殊字符
        String regexZT = "\\D*";        // 数字+特殊字符
        String regexST = "[\\d\\W]*";   // 字母+特殊字符
        String regexZS = "\\w*";        // 数字+字母
        String regexZST = "[\\w\\W]*";  // 数字+字母+特殊字符
        if (password.matches(regexZ) || password.matches(regexS) || password.matches(regexT) || password.length() <= 6) {
            return 1;
        } else if (password.matches(regexZT) || password.matches(regexST) || password.matches(regexZS) || password.length() <= 8) {
            return 2;
        } else if (password.matches(regexZST)) {
            return 3;
        }
        return 1;
    }
}
