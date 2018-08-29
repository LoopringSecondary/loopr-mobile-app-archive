package com.lyqb.walletsdk;

import com.google.common.collect.ImmutableList;
import com.lyqb.walletsdk.exception.InvalidPrivateKeyException;
import com.lyqb.walletsdk.exception.KeystoreSaveException;
import com.lyqb.walletsdk.model.WalletDetail;
import com.lyqb.walletsdk.util.MnemonicUtils;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WalletHelpetTest {

    @Test
    public void createTest() throws KeystoreSaveException {
//        WalletHelper helper = new WalletHelper();
//        String m = "hospital offer drop retreat cook zero mandate cigar orchard three grain seven";
        String m = "door lend source dumb install immune thumb crater ostrich tongue buyer huge";
//        String m = "deal release style gadget hold cannon traffic boat skirt great anxiety sight";

        String dpath = Default.DEFAULT_DPATH;
        byte[] seed = MnemonicUtils.generateSeed(m, "1212");
        List<ChildNumber> childNumberList = HDUtils.parsePath(dpath.replaceAll("\'", "H").toUpperCase());
        DeterministicKey rootKey = HDKeyDerivation.createMasterPrivateKey(seed);

        DeterministicHierarchy hdKey = new DeterministicHierarchy(rootKey);
        DeterministicKey destKey = hdKey.deriveChild(childNumberList, true, true, new ChildNumber(0));

        ECKeyPair ecKeyPair = ECKeyPair.create(destKey.getPrivKey());
        Credentials credentials1 = Credentials.create(ecKeyPair);
        System.out.println(credentials1.toString());


        ImmutableList<ChildNumber> childNumberImmutableList = ImmutableList.copyOf(childNumberList);
        int numChildren = hdKey.getNumChildren(childNumberImmutableList);
        System.out.println(numChildren);

    }

    @Test
    public void qwe() throws IOException, InvalidPrivateKeyException, KeystoreSaveException {
        WalletHelper helper = new WalletHelper();
        WalletDetail fromPrivateKey = helper.createFromPrivateKey(
                "6385615bc43c0accaa331291a48e71e4de9dd021aaaa6b1238fe2c82dbce3c50",
                "",
                new File("/Users/slice30k/Desktop/test")
        );
        System.out.println(fromPrivateKey.toString());
    }
}
