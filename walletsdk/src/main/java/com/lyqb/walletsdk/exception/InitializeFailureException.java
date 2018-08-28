package com.lyqb.walletsdk.exception;

public class InitializeFailureException extends RuntimeException {
    public InitializeFailureException() {
        super("Loopring not initialized!");
    }
}
