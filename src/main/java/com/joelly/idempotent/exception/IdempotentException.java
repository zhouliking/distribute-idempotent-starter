package com.joelly.idempotent.exception;

public class IdempotentException extends RuntimeException {

    public IdempotentException(String msg) {
        super(msg);
    }
}
