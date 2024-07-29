package com.joelly.idempotent.keys;

import org.aspectj.lang.JoinPoint;

public interface LockKeyGenerator {

    /**
     * 生成 key
     * @return
     */
    String generateLockKey(JoinPoint joinPoint);
}
