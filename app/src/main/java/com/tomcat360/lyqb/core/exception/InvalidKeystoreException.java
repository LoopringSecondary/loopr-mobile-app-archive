package com.tomcat360.lyqb.core.exception;

public class InvalidKeystoreException extends RuntimeException {

    public InvalidKeystoreException() {
        super("invalid keystore, check the json format!");
    }
}
