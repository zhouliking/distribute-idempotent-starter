package com.joelly.idempotent.exception;

public class IdempotentConfigException extends IdempotentException{
    public IdempotentConfigException(String msg) {
        super(msg);
    }
}
