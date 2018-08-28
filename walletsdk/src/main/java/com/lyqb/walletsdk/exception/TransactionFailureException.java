package com.lyqb.walletsdk.exception;

public class TransactionFailureException extends Exception {
    public TransactionFailureException(String message) {
        super(message);
    }

    public TransactionFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
