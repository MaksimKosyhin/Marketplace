package com.marketplace.config.exception;

public class NonExistingEntityException extends RuntimeException {
    public NonExistingEntityException(String msg) {
        super(msg);
    }
}
