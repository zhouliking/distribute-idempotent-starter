package com.joelly.idempotent.config.entity;

import com.joelly.idempotent.enums.RedisModeEnum;
import lombok.Data;

@Data
public class RedisConfig {

    private RedisModeEnum redisMode;

    /**
     * 单机模式
     */
    private SingleRedisServerConfig single;

    /**
     * 集群模式
     */
    private ClusterRedisServersConfig cluster;

    /**
     * 哨兵模式
     */
    private SentinelRedisServerConfig sentinel;
}
