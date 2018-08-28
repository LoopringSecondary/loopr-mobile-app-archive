package com.lyqb.walletsdk;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.IOException;

public class KeystoreUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static WalletFile loadFromFile(File keystoreFile) {
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(keystoreFile, WalletFile.class);
        } catch (IOException e) {
            return null;
        }
        return walletFile;
    }

    public static WalletFile loadFromJsonString(String json) {
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(json, WalletFile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return walletFile;
    }
}
