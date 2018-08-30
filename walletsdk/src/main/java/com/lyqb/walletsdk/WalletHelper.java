package com.lyqb.walletsdk;

import com.lyqb.walletsdk.exception.IllegalCredentialException;
import com.lyqb.walletsdk.exception.InvalidKeystoreException;
import com.lyqb.walletsdk.exception.InvalidPrivateKeyException;
import com.lyqb.walletsdk.exception.KeystoreSaveException;
import com.lyqb.walletsdk.model.Account;
import com.lyqb.walletsdk.model.WalletDetail;
import com.lyqb.walletsdk.util.AccountUtils;
import com.lyqb.walletsdk.util.Assert;
import com.lyqb.walletsdk.util.KeystoreUtils;
import com.lyqb.walletsdk.util.MnemonicUtils;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.util.List;

public class WalletHelper {

    public static WalletDetail createFromMnemonic(String mnemonic, String dpath, String password, File keystoreDest) throws KeystoreSaveException {
        // validate inputs.
        Assert.validateMnemonic(mnemonic);
//        Assert.hasText(password, "password can not be null");
        Assert.checkDirectory(keystoreDest);

        if (dpath == null) {
            dpath = Default.DEFAULT_DPATH;
        }
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        List<ChildNumber> childNumberList = HDUtils.parsePath(dpath.replaceAll("\'", "H").toUpperCase());
        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy hdKey = new DeterministicHierarchy(rootKey);
        DeterministicKey destKey = hdKey.deriveChild(childNumberList, true, true, new ChildNumber(0));
        ECKeyPair ecKeyPair = ECKeyPair.create(destKey.getPrivKey());

        String walletFileName;
        try {
            walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, keystoreDest, false);
        } catch (Exception e) {
            throw new KeystoreSaveException(e);
        }
        return new WalletDetail(walletFileName, mnemonic);
    }

    public static WalletDetail createFromKeystore(String keystoreJson, String password, File dest) throws KeystoreSaveException, InvalidKeystoreException, IllegalCredentialException {
        WalletFile walletFile = KeystoreUtils.loadFromJsonString(keystoreJson);
        Assert.checkDirectory(dest);

        Credentials credentials = decrypt(password, keystoreJson);
        String fileName = AccountUtils.generateKeystoreFilename(credentials.getAddress());

        File destination = new File(dest, fileName);
        KeystoreUtils.writeToFile(walletFile, destination);
        return new WalletDetail(fileName);
    }

    public static WalletDetail createFromPrivateKey(String privateKey, String newPassword, File dest) throws InvalidPrivateKeyException, KeystoreSaveException {
        Assert.hasText(privateKey, "private key can not be null");
        Assert.hasText(newPassword, "new password can not be null");
        Assert.checkDirectory(dest);

        if (!WalletUtils.isValidPrivateKey(privateKey)) {
            throw new InvalidPrivateKeyException();
        }
        Credentials credentials = Credentials.create(privateKey);
        String walletFileName;
        try {
            walletFileName = WalletUtils.generateWalletFile(newPassword, credentials.getEcKeyPair(), dest, false);
        } catch (Exception e) {
            throw new KeystoreSaveException(e);
        }
        return new WalletDetail(walletFileName);
    }

    private static Credentials decrypt(String password, String keystore) throws InvalidKeystoreException, IllegalCredentialException {
        WalletFile walletFile = KeystoreUtils.loadFromJsonString(keystore);
        ECKeyPair ecKeyPair = null;
        try {
            ecKeyPair = Wallet.decrypt(password, walletFile);
        } catch (CipherException e) {
            throw new IllegalCredentialException(e);
        }
        return Credentials.create(ecKeyPair);
    }

    public static Account unlockWallet(String password, String keystore) throws InvalidKeystoreException, IllegalCredentialException {
        Credentials credentials = decrypt(password, keystore);
        return Account.create(credentials);
    }
}
