package com.lyqb.walletsdk;

import com.lyqb.walletsdk.service.LooprHttpService;
import com.lyqb.walletsdk.service.LooprSocketService;

public class Loopr {

    private String serviceUrl;

    private LooprHttpService http;
    private LooprSocketService socket;

    public Loopr() {
        LooprConfig config = new LooprConfig();
        this.socket = new LooprSocketService(config.relayBase);

    }
}
