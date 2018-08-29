package com.lyqb.walletsdk.exception;

public class SdkInitializeException extends RuntimeException {

    public SdkInitializeException() {
        super("SDK not initialized!");
    }
}
