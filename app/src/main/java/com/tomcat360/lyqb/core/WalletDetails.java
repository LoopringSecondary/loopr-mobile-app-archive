package com.tomcat360.lyqb.core;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;

public class WalletDetails {
    private WalletFile walletFile;
    private Credentials credentials;

    public WalletDetails(WalletFile walletFile, Credentials credentials) {
        this.walletFile = walletFile;
        this.credentials = credentials;
    }

    public WalletFile getWalletFile() {
        return walletFile;
    }

    public void setWalletFile(WalletFile walletFile) {
        this.walletFile = walletFile;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
