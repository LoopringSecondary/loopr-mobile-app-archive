package com.tomcat360.lyqb.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tomcat360.lyqb.core.exception.InvalidKeystoreException;
import com.tomcat360.lyqb.utils.LyqbLogger;

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

    public static Bip39Wallet createWallet(String mnemonic, String dpath, String password, File dest) throws CipherException, IOException {
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

        String walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, dest, false);
        Bip39Wallet bip39Wallet = new Bip39Wallet(walletFileName, mnemonic);
        LyqbLogger.debug("wallet created! " + bip39Wallet.toString());
        return bip39Wallet;
    }

    public static Bip39Wallet importFromMnemonic(String mnemonic, String dpath, String password, File dest, int childNumber) throws CipherException, IOException {
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
        String walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, dest, false);
        Bip39Wallet bip39Wallet = new Bip39Wallet(walletFileName, mnemonic);
        LyqbLogger.debug("wallet created! " + bip39Wallet.toString());
        return bip39Wallet;
    }

//    public static String importFromKeystore(String keystoreJson, String oldPassword, String newPassword, File dest) {
//        Assert.hasText(keystoreJson, "empty keystore!");
//        Assert.checkDirectory(dest);
//
//        ECKeyPair ecKeyPair;
//        try {
//            Credentials credentials = unlock(oldPassword, keystoreJson);
//            ecKeyPair = credentials.getEcKeyPair();
//        } catch (Exception e) {
//            throw new RuntimeException("unlock wallet failure!", e);
//        }
//
//        String walletFileName = "";
//        try {
//            if (Strings.isNullOrEmpty(newPassword)) {
//                walletFileName = WalletUtils.generateWalletFile(oldPassword, ecKeyPair, dest, false);
//            } else {
//                walletFileName = WalletUtils.generateWalletFile(newPassword, ecKeyPair, dest, false);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LyqbLogger.log("wallet create failure!");
//        }
//
//        return walletFileName;
//    }

    public static String importFromKeystore(String keystoreJson, String password, File dest) {
        Assert.hasText(keystoreJson, "empty keystore!");
        Assert.checkDirectory(dest);

        WalletDetails walletDetails;
        try {
            walletDetails = unlock(password, keystoreJson);
        } catch (Exception e) {
            throw new RuntimeException("unlock wallet failure!", e);
        }

        WalletFile walletFile = walletDetails.getWalletFile();
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'");
        String fileName = dateFormat.format(new Date()) + walletFile.getAddress() + ".json";

        File destination = new File(dest, fileName);
        try {
            objectMapper.writeValue(destination, walletFile);
        } catch (IOException e) {
            throw new RuntimeException("save keystore file failure!", e);
        }
        return fileName;
    }

    public static String importFromPrivateKey(String privateKey, String newPassword, File dest) {

        Assert.hasText(privateKey, "private key can not be null");
        Assert.checkDirectory(dest);

        if (!WalletUtils.isValidPrivateKey(privateKey)) {
            throw new RuntimeException("illegal private key");
        }
        if (Strings.isNullOrEmpty(newPassword)) {
            newPassword = "";
        }

        Credentials credentials = Credentials.create(privateKey);

        String walletFileName = "";
        try {
            walletFileName = WalletUtils.generateWalletFile(newPassword, credentials.getEcKeyPair(), dest, false);
        } catch (Exception e) {
            e.printStackTrace();
            LyqbLogger.log("wallet create failure!");
        }
        return walletFileName;
    }

    public static Credentials unlock(String password, File keystore) throws IOException, CipherException {
        Assert.hasText(password, "password can not be null");
        return WalletUtils.loadCredentials(password, keystore);
    }

    public static WalletDetails unlock(String password, String keystoreJson) throws InvalidKeystoreException, CipherException {
        Assert.hasText(password, "password can not be null");
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(keystoreJson, WalletFile.class);
        } catch (IOException e) {
            throw new InvalidKeystoreException();
        }
        ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);

        return new WalletDetails(walletFile, Credentials.create(ecKeyPair));
    }
}
