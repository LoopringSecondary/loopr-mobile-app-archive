package com.lyqb.walletsdk;

import com.lyqb.walletsdk.exception.InvalidKeystoreException;
import com.lyqb.walletsdk.exception.InvalidPrivateKeyException;
import com.lyqb.walletsdk.exception.KeystoreSaveException;
import com.lyqb.walletsdk.model.WalletDetail;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WalletHelper {

    public WalletDetail createFromMnemonic(String mnemonic, String dpath, String password, File keystoreDest) throws KeystoreSaveException {
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
//        Credentials credentials = Credentials.create(ecKeyPair);

        String walletFileName;
        try {
            walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, keystoreDest, false);
        } catch (Exception e) {
            throw new KeystoreSaveException(e);
        }
//        registerToRelay(credentials.getAddress());
        return new WalletDetail(walletFileName, mnemonic);
    }

//    public WalletDetail importFromMnemonic(String mnemonic, String dpath, String password, File dest, int childNumber) throws KeystoreSaveException {
//        // validate inputs.
//        Assert.hasText(mnemonic, "illegal mnemonic");
//        Assert.hasText(password, "password can not be null");
//        Assert.checkDirectory(dest);
//
//        if (dpath == null) {
//            dpath = Default.DEFAULT_DPATH;
//        }
//
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
//        List<ChildNumber> childNumberList = HDUtils.parsePath(dpath.replaceAll("\'", "H").toUpperCase());
//        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);
//        DeterministicHierarchy hdKey = new DeterministicHierarchy(rootKey);
//        DeterministicKey destKey = hdKey.deriveChild(childNumberList, true, true, new ChildNumber(childNumber));
//        ECKeyPair ecKeyPair = ECKeyPair.create(destKey.getPrivKey());
//        Credentials credentials = Credentials.create(ecKeyPair);
//        String walletFileName;
//        try {
//            walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, dest, false);
//        } catch (Exception e) {
//            throw new KeystoreSaveException(e);
//        }
////        registerToRelay(credentials.getAddress());
//        return new WalletDetail(walletFileName, mnemonic);
//    }

    public WalletDetail createFromKeystore(String keystoreJson, String password, File dest) throws CipherException, KeystoreSaveException, InvalidKeystoreException {
        WalletFile walletFile = KeystoreUtils.loadFromJsonString(keystoreJson);
        Assert.checkDirectory(dest);

        Credentials credentials = unlockWallet(password, keystoreJson);
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'", Locale.CHINA);
        String fileName = dateFormat.format(new Date()) + credentials.getAddress() + ".json";

        File destination = new File(dest, fileName);
        KeystoreUtils.writeToFile(walletFile, destination);
//        registerToRelay(credentials.getAddress());
        return new WalletDetail(fileName);
    }

    public WalletDetail createFromPrivateKey(String privateKey, String newPassword, File dest) throws InvalidPrivateKeyException, KeystoreSaveException {
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
//        registerToRelay(credentials.getAddress());
        return new WalletDetail(walletFileName);
    }

    public Credentials unlockWallet(String password, File keystore) throws CipherException, InvalidKeystoreException {
        WalletFile walletFile = KeystoreUtils.loadFromFile(keystore);
        ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
        return Credentials.create(ecKeyPair);
    }

    public Credentials unlockWallet(String password, String keystore) throws CipherException, InvalidKeystoreException {
        WalletFile walletFile = KeystoreUtils.loadFromJsonString(keystore);
        ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
        return Credentials.create(ecKeyPair);
    }

}
