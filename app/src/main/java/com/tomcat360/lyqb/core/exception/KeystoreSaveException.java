package com.tomcat360.lyqb.core.exception;

public class KeystoreSaveException extends Exception {

    private static String MESSAGE = "unable to save keystore file!";

    public KeystoreSaveException() {
        super(MESSAGE);
    }

    public KeystoreSaveException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
