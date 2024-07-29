package com.joelly.idempotent.factory;

import com.joelly.idempotent.config.entity.RedisConfig;
import com.joelly.idempotent.config.entity.SentinelRedisServerConfig;
import com.joelly.idempotent.config.entity.SingleRedisServerConfig;
import com.joelly.idempotent.enums.RedisModeEnum;
import com.joelly.idempotent.exception.IdempotentConfigException;
import com.joelly.idempotent.config.entity.ClusterRedisServersConfig;
import jodd.util.StringUtil;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RedisServerConfigFactory {

    /**
     * 配置ip端口： redis://10.13.5.83:6399
     */
    private static final String REDIS_ADDRESS = "redis://%s";

    public static Config createVehicle(RedisConfig redis) {
        if (redis == null) {
            throw new IdempotentConfigException("current store node need config redis");
        }
        RedisModeEnum redisMode = redis.getRedisMode();
        if (RedisModeEnum.single.equals(redisMode)) {
            return convertConfig(redis.getSingle());
        } else if (RedisModeEnum.cluster.equals(redisMode)) {
            return convertConfig(redis.getCluster());
        } else if (RedisModeEnum.sentinel.equals(redisMode)) {
            return convertConfig(redis.getSentinel());
        } else {
            throw new IdempotentConfigException("Unsupported this redis mode: " + redisMode);
        }
    }

    private static Config convertConfig(SingleRedisServerConfig single) {
        Config config = new Config();
        if (single == null) {
            throw new IdempotentConfigException("redis single mode not config");
        }
        if (StringUtil.isBlank(single.getAddresses())) {
            throw new IdempotentConfigException("redis single mode must config addresses (like this: ip:port)");
        }
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(convertRedisAddr(single.getAddresses()));
        singleServerConfig.setDatabase(single.getDatabase());
        if (StringUtil.isNotBlank(single.getPassword())) {
            singleServerConfig.setPassword(single.getPassword());
        }
        if (single.getUsername() != null) {
            singleServerConfig.setUsername(single.getUsername());
        }
        if (single.getTimeout() != null) {
            singleServerConfig.setTimeout((int) single.getTimeout().toMillis());
        }
        if (single.getConnectionPoolSize() >= 0) {
            singleServerConfig.setConnectionPoolSize(single.getConnectionPoolSize());
        }

        if (single.getConnectTimeout() != null) {
            singleServerConfig.setConnectTimeout((int) single.getConnectTimeout().toMillis());
        }
        return config;
    }

    private static Config convertConfig(ClusterRedisServersConfig redisConfig) {
        if (redisConfig == null) {
            throw new IdempotentConfigException("redis cluster mode not config");
        }
        if (CollectionUtils.isEmpty(redisConfig.getAddresses())) {
            throw new IdempotentConfigException("redis cluster mode not config node addresses");
        }
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        clusterServersConfig.setNodeAddresses(redisConfig.getAddresses());
        if (redisConfig.getPassword() != null) {
            clusterServersConfig.setPassword(redisConfig.getPassword());
        }
        if (redisConfig.getScanIntervalMillis() > -1) {
            clusterServersConfig.setScanInterval(redisConfig.getScanIntervalMillis());
        }
        if (redisConfig.getConnectionTimeoutMillis() > 0) {
            clusterServersConfig.setConnectTimeout(redisConfig.getConnectionTimeoutMillis());
        }
        return config;
    }

    private static Config convertConfig(SentinelRedisServerConfig redisConfig) {
        if (redisConfig == null) {
            throw new IdempotentConfigException("redis sentinel mode not config");
        }
        if (CollectionUtils.isEmpty(redisConfig.getAddresses())) {
            throw new IdempotentConfigException("redis sentinel mode not config node addresses");
        }
        Config config = new Config();
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
        sentinelServersConfig.setMasterName(redisConfig.getMasterName());
        sentinelServersConfig.setSentinelAddresses(convertRedisAddr(redisConfig.getAddresses()));
        if (redisConfig.getPassword() != null) {
            sentinelServersConfig.setPassword(redisConfig.getPassword());
        }
        sentinelServersConfig.setDatabase(redisConfig.getDatabase());
        if (redisConfig.getConnectTimeoutMillis() > 0) {
            sentinelServersConfig.setConnectTimeout(redisConfig.getConnectTimeoutMillis());
        }
        return config;
    }

    private static String convertRedisAddr(String ipPort) {
        return String.format(REDIS_ADDRESS, ipPort);
    }

    private static List<String> convertRedisAddr(List<String> ipPorts) {
        if (CollectionUtils.isEmpty(ipPorts)) {
            return new ArrayList<>();
        }
        return ipPorts.stream()
                .map(RedisServerConfigFactory::convertRedisAddr)
                .collect(Collectors.toList());
    }
}
