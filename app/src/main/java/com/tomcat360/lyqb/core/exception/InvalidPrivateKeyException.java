package com.tomcat360.lyqb.core.exception;

public class InvalidPrivateKeyException extends RuntimeException {

    public InvalidPrivateKeyException() {
        super("invalid private key, check the input!");
    }
}
