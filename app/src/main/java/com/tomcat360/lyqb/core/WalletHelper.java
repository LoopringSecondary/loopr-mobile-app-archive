package com.tomcat360.lyqb.core;

import android.annotation.SuppressLint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomcat360.lyqb.core.exception.IllegalCredentialException;
import com.tomcat360.lyqb.core.exception.InvalidKeystoreException;
import com.tomcat360.lyqb.core.exception.InvalidPrivateKeyException;
import com.tomcat360.lyqb.core.exception.KeystoreSaveException;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WalletHelper {

    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    private static final String DEFAULT_DPATH = "m/44'/60'/0'/0";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateMnemonic() {
        // generate mnemonic words.
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        return MnemonicUtils.generateMnemonic(initialEntropy);
    }

    public static Bip39Wallet createWallet(String mnemonic, String dpath, String password, File dest) throws KeystoreSaveException {
        // validate inputs.
        Assert.validateMnemonic(mnemonic);
//        Assert.hasText(password, "password can not be null");
        Assert.checkDirectory(dest);

        if (dpath == null) {
            dpath = DEFAULT_DPATH;
        }

        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        List<ChildNumber> childNumberList = HDUtils.parsePath(dpath.replaceAll("\'", "H").toUpperCase());
        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy hdKey = new DeterministicHierarchy(rootKey);
        DeterministicKey destKey = hdKey.deriveChild(childNumberList, true, true, new ChildNumber(0));
        ECKeyPair ecKeyPair = ECKeyPair.create(destKey.getPrivKey());

        String walletFileName;
        try {
            walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, dest, false);
        } catch (Exception e) {
            throw new KeystoreSaveException(e);
        }
        return new Bip39Wallet(walletFileName, mnemonic);
    }

    public static Bip39Wallet importFromMnemonic(String mnemonic, String dpath, String password, File dest, int childNumber) throws KeystoreSaveException {
        // validate inputs.
        Assert.hasText(mnemonic, "illegal mnemonic");
        Assert.hasText(password, "password can not be null");
        Assert.checkDirectory(dest);

        if (dpath == null) {
            dpath = DEFAULT_DPATH;
        }

        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        List<ChildNumber> childNumberList = HDUtils.parsePath(dpath.replaceAll("\'", "H").toUpperCase());
        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy hdKey = new DeterministicHierarchy(rootKey);
        DeterministicKey destKey = hdKey.deriveChild(childNumberList, true, true, new ChildNumber(childNumber));
        ECKeyPair ecKeyPair = ECKeyPair.create(destKey.getPrivKey());
        String walletFileName;
        try {
            walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, dest, false);
        } catch (Exception e) {
            throw new KeystoreSaveException(e);
        }
        return new Bip39Wallet(walletFileName, mnemonic);
    }

    @SuppressLint("SimpleDateFormat")
    public static String importFromKeystore(String keystoreJson, String password, File dest) throws KeystoreSaveException, InvalidKeystoreException, IllegalCredentialException {
        Assert.hasText(keystoreJson, "empty keystore!");
        Assert.checkDirectory(dest);

        WalletFile walletFile = KeystoreHelper.loadFromJsonString(keystoreJson);

        Credentials credentials = unlock(password, walletFile);
        if (credentials == null) {
            throw new IllegalCredentialException();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'");
        String fileName = dateFormat.format(new Date()) + walletFile.getAddress() + ".json";

        File destination = new File(dest, fileName);
        try {
            objectMapper.writeValue(destination, walletFile);
        } catch (IOException e) {
            throw new KeystoreSaveException();
        }
        return fileName;
    }

    public static String importFromPrivateKey(String privateKey, String newPassword, File dest) throws InvalidPrivateKeyException, KeystoreSaveException {

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
        return walletFileName;
    }

    public static Credentials unlock(String password, WalletFile walletFile) {
        Assert.hasText(password, "password can not be null");
        Assert.notNull(walletFile, "walletFile can not be null");

        ECKeyPair ecKeyPair;
        try {
            ecKeyPair = Wallet.decrypt(password, walletFile);
        } catch (CipherException e) {
            return null;
        }
        return Credentials.create(ecKeyPair);
    }
}
