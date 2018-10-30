/**
 * Created with IntelliJ IDEA.
 * User: kenshin chenwang34@creditease.cn
 * Time: 2018-10-08 上午12:39
 * Cooperation: CreditEase©2017 普信恒业科技发展(北京)有限公司
 */
package leaf.prod.app.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import leaf.prod.app.model.ThirdLoginUser;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.walletsdk.util.StringUtils;

public class WalletUtil {

    public static WalletEntity getCurrentWallet(Context context) {
        //        WalletEntity result = null;
        //        List<WalletEntity> wallets = SPUtils.getDataList(context, "walletlist", WalletEntity.class);
        //        String owner = (String) SPUtils.get(context, "address", "");
        //        for (WalletEntity walletEntity : wallets) {
        //            if (walletEntity.getAddress().equalsIgnoreCase(owner)) {
        //                result = walletEntity;
        //                break;
        //            }
        //        }
        //        return result;
        return SPUtils.getBean(context, "currentWallet", WalletEntity.class);
    }

    public static String getCurrentAddress(Context context) {
        WalletEntity wallet = getCurrentWallet(context);
        return wallet != null ? wallet.getAddress() : "";
    }

    public static List<String> getChooseTokens(Context context) {
        WalletEntity wallet = getCurrentWallet(context);
        return wallet != null ? wallet.getChooseTokenList() : new ArrayList<>();
    }

    public static String getCurrentFileName(Context context) {
        WalletEntity wallet = getCurrentWallet(context);
        return wallet != null ? wallet.getFilename() : "";
    }

    public static WalletEntity getWalletByAddr(Context context, String address) {
        WalletEntity result = null;
        List<WalletEntity> wallets = SPUtils.getDataList(context, "walletlist", WalletEntity.class);
        for (WalletEntity walletEntity : wallets) {
            if (walletEntity.getAddress().equalsIgnoreCase(address)) {
                result = walletEntity;
                break;
            }
        }
        return result;
    }

    public static List<WalletEntity> getWalletList(Context context) {
        return SPUtils.getDataList(context, "walletlist", WalletEntity.class);
    }

    public static void setWalletList(Context context, List<WalletEntity> list) {
        if (list == null || list.isEmpty()) {
            SPUtils.remove(context, "walletlist");
        }
        SPUtils.setDataList(context, "walletlist", list);
        ThirdLoginUser thirdLoginUser = ThirdUserUtil.getThirdLoginUserBean(context);
        if (thirdLoginUser != null) {
            // 同步更新第三方登录相关信息
            ThirdUserUtil.updateWalletList(context, list);
        }
    }

    public static boolean isWalletExisted(Context context, WalletEntity wallet) {
        boolean result = false;
        List<WalletEntity> wallets = SPUtils.getDataList(context, "walletlist", WalletEntity.class);
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

    public static void addWallet(Context context, WalletEntity wallet) {
        List<WalletEntity> wallets = SPUtils.getDataList(context, "walletlist", WalletEntity.class);
        if (!wallets.contains(wallet)) {
            wallets.add(wallet);
            setWalletList(context, wallets);
        }
        setCurrentWallet(context, wallet);
    }

    public static void updateWallet(Context context, WalletEntity newWallet) {
        if (newWallet == null || StringUtils.isEmpty(newWallet.getAddress()))
            return;
        if (getCurrentWallet(context).getAddress().equals(newWallet.getAddress())) {
            setCurrentWallet(context, newWallet);
        }
        List<WalletEntity> wallets = SPUtils.getDataList(context, "walletlist", WalletEntity.class);
        for (WalletEntity wallet : wallets) {
            if (wallet.equals(newWallet)) {
                wallets.set(wallets.indexOf(wallet), newWallet);
                setWalletList(context, wallets);
                return;
            }
        }
    }

    public static void removeWallet(Context context, String address) {
        List<WalletEntity> wallets = SPUtils.getDataList(context, "walletlist", WalletEntity.class);
        if (getCurrentWallet(context).getAddress().equals(address)) {
            SPUtils.remove(context, "currentWallet");
        }
        for (WalletEntity wallet : wallets) {
            if (wallet.getAddress().equals(address)) {
                wallets.remove(wallet);
                break;
            }
        }
        setWalletList(context, wallets);
    }

    public static void clearWalletList(Context context) {
        SPUtils.remove(context, "walletlist");
        SPUtils.remove(context, "currentWallet");
    }

    public static void setCurrentWallet(Context context, WalletEntity wallet) {
        SPUtils.put(context, "currentWallet", wallet);
    }
}
