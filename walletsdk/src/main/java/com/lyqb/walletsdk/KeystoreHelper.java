package com.lyqb.walletsdk;

import com.google.gson.Gson;

import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class KeystoreHelper {

    private static Gson gson= new Gson();

    public static WalletFile loadFromFile(File keystoreFile) throws FileNotFoundException {
        FileReader reader = new FileReader(keystoreFile);
        WalletFile walletFile = gson.fromJson(reader, WalletFile.class);
        return walletFile;
    }


    public static WalletFile loadFromJsonString(String json) {
        WalletFile walletFile = gson.fromJson(json, WalletFile.class);
        return walletFile;
    }
}
