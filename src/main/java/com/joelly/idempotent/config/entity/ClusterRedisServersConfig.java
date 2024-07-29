package com.joelly.idempotent.config.entity;

import lombok.Data;

import java.util.List;

@Data
public class ClusterRedisServersConfig {

    // 集群节点地址列表, 如：127.0.0.1:7000,127.0.0.1:7001
    private List<String> addresses;

    // 密码（如果Redis集群设置了密码）
    private String password;

    // 集群状态扫描间隔（可选），单位为毫秒,默认5000ms
    private int scanIntervalMillis;

    // 连接超时时间（可选），默认10000ms
    private int connectionTimeoutMillis;
}
