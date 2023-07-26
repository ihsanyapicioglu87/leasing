package com.allane.leasing.exception;

public class LeasingContractNotFoundException extends RuntimeException {
    public LeasingContractNotFoundException(String message) {
        super(message);
    }
}
