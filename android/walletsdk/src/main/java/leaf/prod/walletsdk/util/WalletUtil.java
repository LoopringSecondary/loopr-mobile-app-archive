/**
 * Created with IntelliJ IDEA.
 * User: kenshin chenwang34@creditease.cn
 * Time: 2018-10-08 上午12:39
 * Cooperation: CreditEase©2017 普信恒业科技发展(北京)有限公司
 */
package leaf.prod.walletsdk.util;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;

import org.json.JSONException;
import org.web3j.crypto.Credentials;

import leaf.prod.walletsdk.exception.IllegalCredentialException;
import leaf.prod.walletsdk.exception.InvalidKeystoreException;
import leaf.prod.walletsdk.exception.InvalidPrivateKeyException;
import leaf.prod.walletsdk.model.RandomWallet;
import leaf.prod.walletsdk.model.wallet.ImportWalletType;
import leaf.prod.walletsdk.model.wallet.WalletEntity;

public class WalletUtil {

    public static WalletEntity getCurrentWallet(Context context) {
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
        return isWalletExisted(context, WalletEntity.builder().walletname(walletName).build());
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

    public static boolean hasWallet(Context context) {
        return SPUtils.getDataList(context, "walletlist", WalletEntity.class).size() > 0;
    }

    public static boolean needPassword(Context context) {
        WalletEntity walletEntity = getCurrentWallet(context);
        if (walletEntity.getWalletType() != ImportWalletType.MNEMONIC || !StringUtils.isEmpty(walletEntity.getPas()))
            return true;
        return false;
    }

    public static Credentials getCredential(Context context, String password) throws IOException, JSONException, InvalidKeystoreException, IllegalCredentialException {
        WalletEntity walletEntity = getCurrentWallet(context);
        if (!StringUtils.isEmpty(password) && !StringUtils.isEmpty(walletEntity.getPas()) && ImportWalletType.MNEMONIC == walletEntity
                .getWalletType() && !passwordValid(password, walletEntity.getPas())) {
            throw new InvalidKeystoreException();
        }
        password = "imToken".equals(walletEntity.getWalletFrom()) && ImportWalletType.MNEMONIC == walletEntity.getWalletType() ? "" : password;
        String keystore = FileUtils.getKeystoreFromSD(context);
        return KeystoreUtils.unlock(password, keystore);
    }

    /**
     * 生成随机钱包 
     */
    public static RandomWallet getRandomWallet() throws InvalidPrivateKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        return KeystoreUtils.createFromPrivateKey();
    }

    /**
     * 加密助记词
     *
     * @param content
     * @param password
     * @return
     */
    public static String encryptMnemonic(String content, String password, String salt, String iv) {
        String sha256 = EncryptUtil.encryptSHA256(password, salt);
        return EncryptUtil.encryptAES(content, sha256, iv);
    }

    /**
     * 解密助记词
     *
     * @param content
     * @return
     */
    public static String decryptMnemonic(String content, String password, String salt, String iv) {
        return EncryptUtil.decryptAES(content, EncryptUtil.encryptSHA256(password, salt), iv);
    }

    public static boolean passwordValid(String inputPwd, String encryptPwd) {
        return encryptPwd.equals(EncryptUtil.encryptSHA3(inputPwd));
    }

    public static void cleanOldWallets(Context context) {
        List<WalletEntity> walletList = getWalletList(context);
        Iterator<WalletEntity> iterator = walletList.iterator();
        while (iterator.hasNext()) {
            WalletEntity wallet = iterator.next();
            if (!StringUtils.isEmpty(wallet.getMnemonic()) && StringUtils.isEmpty(wallet.getSalt())) {
                iterator.remove();
            }
        }
        setWalletList(context, walletList);
    }
}
