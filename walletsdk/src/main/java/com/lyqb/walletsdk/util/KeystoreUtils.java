package com.lyqb.walletsdk.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyqb.walletsdk.exception.KeystoreSaveException;

import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class KeystoreUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static WalletFile loadFromFile(File keystoreFile) {
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(keystoreFile, WalletFile.class);
        } catch (IOException e) {
            return new WalletFile();
        }
        return walletFile;
    }

    public static WalletFile loadFromJsonString(String json) {
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(json, WalletFile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new WalletFile();
        }
        return walletFile;
    }

    public static void writeToFile(String json, File keystoreFile) throws KeystoreSaveException {
        try {
            FileWriter fileWriter = new FileWriter(keystoreFile);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            throw new KeystoreSaveException(e);
        }
    }
}
