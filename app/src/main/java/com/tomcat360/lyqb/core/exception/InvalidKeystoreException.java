package com.tomcat360.lyqb.core.exception;

public class InvalidKeystoreException extends Exception {

    public InvalidKeystoreException() {
        super("invalid keystore, check the json format!");
    }
}
