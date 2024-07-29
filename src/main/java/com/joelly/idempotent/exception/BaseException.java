package com.joelly.idempotent.exception;

public interface BaseException {

    /**
     * 获取异常码
     *
     * @return 异常码
     */
    Integer getCode();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    String getMessage();

}
