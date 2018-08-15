package com.tomcat360.lyqb.core;

import com.tomcat360.lyqb.utils.Assert;
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
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;

public class WalletHelper {

    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    private static final String DEFAULT_DPATH = "m/44'/60'/0'/0";

    public static Bip39Wallet create(String password, File dest) throws Exception {
        // validate inputs.
        Assert.hasText(password, "password can not be null");
        Assert.checkDirectory(dest);
        // generate mnemonic words.
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        return generateEthWallet(mnemonic, DEFAULT_DPATH, password, dest);
    }

    public static Bip39Wallet importFromMnemonic(String mnemonic, String dpath, String password, File dest) throws Exception {
        // validate inputs.
        Assert.hasText(mnemonic, "illegal mnemonic");
        Assert.hasText(dpath, "dpath can not be null");
        Assert.hasText(password, "password can not be null");
        Assert.checkDirectory(dest);
        return generateEthWallet(mnemonic, dpath, password, dest);
    }

    public static void unlock(String password, File keystore) throws Exception {
        if (password == null || "".equals(password.trim())){
            throw new Exception("password can not be null");
        }
        Credentials credentials = WalletUtils.loadCredentials(password, keystore);
        LyqbLogger.debug("wallet unlocked!");
    }


    private static Bip39Wallet generateEthWallet(String mnemonic, String dpath, String password, File dest) throws CipherException, IOException {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
        List<ChildNumber> childNumberList = HDUtils.parsePath(dpath.replaceAll("\'", "H").toUpperCase());
        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy hdKey = new DeterministicHierarchy(rootKey);
        DeterministicKey destKey = hdKey.deriveChild(childNumberList, true, true, new ChildNumber(1));

        ECKeyPair ecKeyPair = ECKeyPair.create(destKey.getPrivKey());

        String walletFileName = WalletUtils.generateWalletFile(password, ecKeyPair, dest, false);
        Bip39Wallet bip39Wallet = new Bip39Wallet(walletFileName, mnemonic);
        LyqbLogger.debug("wallet created! " + bip39Wallet.toString());
        return bip39Wallet;
    }
}
