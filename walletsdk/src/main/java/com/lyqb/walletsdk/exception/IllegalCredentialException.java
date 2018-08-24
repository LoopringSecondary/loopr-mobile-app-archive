package com.lyqb.walletsdk.exception;

public class IllegalCredentialException extends Exception {
    private static final String MESSAGE = "unable to unlock wallet, check the password!";

    public IllegalCredentialException() {
        super(MESSAGE);
    }

    public IllegalCredentialException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
