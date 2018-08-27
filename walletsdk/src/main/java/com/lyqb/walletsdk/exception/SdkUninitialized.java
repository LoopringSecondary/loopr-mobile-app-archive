package com.lyqb.walletsdk.exception;

public class SdkUninitialized extends RuntimeException {
    public SdkUninitialized() {
        super("Loopring not initialized!");
    }
}
