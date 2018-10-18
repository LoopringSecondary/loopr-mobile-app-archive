/**
 * Created with IntelliJ IDEA.
 * User: kenshin chenwang34@creditease.cn
 * Time: 2018-10-08 上午12:39
 * Cooperation: CreditEase©2017 普信恒业科技发展(北京)有限公司
 */
package leaf.prod.app.utils;

import java.util.List;

import android.content.Context;

import leaf.prod.app.model.WalletEntity;

public class WalletUtil {

    public static WalletEntity getCurrentWallet(Context context) {
        WalletEntity result = null;
        List<WalletEntity> wallets = SPUtils.getWalletDataList(context, "walletlist", WalletEntity.class);
        String owner = (String) SPUtils.get(context, "address", "");
        for (WalletEntity walletEntity : wallets) {
            if (walletEntity.getAddress().equalsIgnoreCase(owner)) {
                result = walletEntity;
                break;
            }
        }
        return result;
    }

    public static boolean isWalletExisted(Context context, WalletEntity wallet) {
        boolean result = false;
        List<WalletEntity> wallets = SPUtils.getWalletDataList(context, "walletlist", WalletEntity.class);
        for (WalletEntity walletEntity : wallets) {
            if ((!walletEntity.getAddress().isEmpty() && walletEntity.getAddress()
                    .equalsIgnoreCase(wallet.getAddress())) ||
                    walletEntity.getWalletname().equalsIgnoreCase(wallet.getWalletname())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean isWalletExisted(Context context, String walletName) {
        return isWalletExisted(context, new WalletEntity(walletName));
    }
}
