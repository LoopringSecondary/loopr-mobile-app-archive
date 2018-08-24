package com.tomcat360.lyqb.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomcat360.lyqb.core.exception.InvalidKeystoreException;
import com.tomcat360.lyqb.core.singleton.ObjectMapperInstance;

import org.web3j.crypto.WalletFile;

import java.io.File;
import java.io.IOException;

public class KeystoreHelper {
    private static ObjectMapper objectMapper = ObjectMapperInstance.getMapper();

    public static WalletFile loadFromFile(File keystoreFile) throws InvalidKeystoreException {
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(keystoreFile, WalletFile.class);
        } catch (IOException e) {
            throw new InvalidKeystoreException(e);
        }
        return walletFile;
    }

    public static WalletFile loadFromJsonString(String json) throws InvalidKeystoreException {
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(json, WalletFile.class);
        } catch (IOException e) {
            throw new InvalidKeystoreException(e);
        }
        return walletFile;
    }
}
