package com.lyqb.walletsdk.model;

import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import lombok.Data;

@Data
public class Account {

    private String address;

    private String publicKey;

    private String privateKey;

    public Account(String address, String publicKey, String privateKey) {
        this.address = address;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public static Account create(String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        return create(credentials);
    }

    public static Account create(Credentials credentials) {
        return new Account(
                credentials.getAddress(),
                Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPublicKey()),
                Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey())
        );
    }

    public Credentials toCredentials() {
        return Credentials.create(privateKey, publicKey);
    }
}
