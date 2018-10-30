package leaf.prod.app.utils;

import java.util.List;

import android.content.Context;

import leaf.prod.app.model.ThirdLoginUser;
import leaf.prod.app.model.WalletEntity;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午11:33
 * Cooperation: Loopring
 */
public class ThirdUserUtil {

    private static String THIRD_LOGIN = "third_login";

    public static ThirdLoginUser getThirdLoginUserBean(Context context) {
        return SPUtils.getBean(context, THIRD_LOGIN + "_" + SPUtils.get(context, THIRD_LOGIN, ""), ThirdLoginUser.class);
    }

    public static void initThirdLogin(Context context, ThirdLoginUser thirdLoginUser) {
        if (thirdLoginUser == null)
            return;
        WalletUtil.clearWalletList(context);
        SPUtils.put(context, THIRD_LOGIN, thirdLoginUser.getUserId());
        // todo db
        SPUtils.put(context, THIRD_LOGIN + "_" + thirdLoginUser.getUserId(), thirdLoginUser);
    }

    public static void updateWalletList(Context context, List<WalletEntity> walletEntityList) {
        ThirdLoginUser thirdLoginUser = getThirdLoginUserBean(context);
        if (thirdLoginUser != null && !thirdLoginUser.equals(new ThirdLoginUser(walletEntityList))) {
            thirdLoginUser.setWalletList(walletEntityList);
            // todo db
            SPUtils.put(context, THIRD_LOGIN + "_" + thirdLoginUser.getUserId(), thirdLoginUser);
        }
    }
}
