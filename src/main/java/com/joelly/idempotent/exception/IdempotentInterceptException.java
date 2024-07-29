package com.joelly.idempotent.exception;

public class IdempotentInterceptException extends IdempotentException{
    public IdempotentInterceptException(String msg) {
        super(msg);
    }
}
