package com.tomcat360.lyqb.core;

import com.tomcat360.lyqb.net.G;
import com.tomcat360.lyqb.utils.LyqbLogger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;

import java.io.IOException;

class Web3jHolder {

    private static Web3j web3jInstance;

    public static Web3j getWeb3jInstance() {
        if (web3jInstance == null) {
            Web3jService web3jService = Web3jHttpServiceHolder.getWeb3jService();
            web3jInstance = Web3jFactory.build(web3jService);
            if (G.IS_DEV) {
                try {
                    String web3ClientVersion = web3jInstance.web3ClientVersion().send().getWeb3ClientVersion();
                    LyqbLogger.debug("Connected to Ethereum client version: " + web3ClientVersion);
                } catch (IOException e) {
                    // swallow exception.
                }
            }
        }
        return web3jInstance;
    }

}
