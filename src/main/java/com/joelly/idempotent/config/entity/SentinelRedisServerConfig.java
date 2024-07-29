package com.joelly.idempotent.config.entity;

import lombok.Data;

import java.util.List;

@Data
public class SentinelRedisServerConfig {

    // 主节点的名称
    private String masterName;

    // 哨兵地址列表: 如127.0.0.1:7000,127.0.0.1:7001
    private List<String> addresses;

    // 连接哨兵时的密码（如果需要的话）
    private String password;

    // 连接哨兵时的数据库索引（默认为0）
    private int database;

    // 连接哨兵的超时时间（毫秒）
    private int connectTimeoutMillis;

}
