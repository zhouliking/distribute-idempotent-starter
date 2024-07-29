package com.joelly.idempotent.config.entity;

import lombok.Data;

import java.time.Duration;

/**
 * 单机模式 redis 配置
 */
@Data
public class SingleRedisServerConfig {
    private String addresses;
    private String password;
    private String username; // 如果Redis支持用户名
    private int database = 0; // Database index used for Redis connection
    private Duration timeout; // 连接超时和读取超时，默认3000ms, 如：5s、5000ms
    private Duration connectTimeout; // 连接超时, 如：5s、5000ms
    private int connectionPoolSize = 64; // 连接池大小
}
