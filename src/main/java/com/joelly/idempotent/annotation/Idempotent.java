package com.joelly.idempotent.annotation;

import com.joelly.idempotent.keys.LockKeyGenerator;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Inherited
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /**
     * 有效期 默认：5
     */
    long expireTime() default 5L;

    /**
     * 获取锁等待的时间
     */
    long waitTime() default 0L;

    /**
     * 时间单位 默认：s
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否执行完后是否资源 (默认不释放)
     */
    boolean vacate() default false;

    /**
     * 生成锁 key 方式 默认为  header -> cookie-> sessionId
     *
     * @return
     */
    Class<? extends LockKeyGenerator> keyGenerator();

    String msg() default "任务正则执行中";
}
