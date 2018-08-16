package com.tomcat360.lyqb.core;

import com.tomcat360.lyqb.net.G;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;

public class Web3jHttpServiceHolder {

    private static Web3jService web3jService = null;

    public static Web3jService getWeb3jService() {
        if (web3jService == null) {
            web3jService = new HttpService(G.BASE_URL);
        }
        return web3jService;
    }
}
