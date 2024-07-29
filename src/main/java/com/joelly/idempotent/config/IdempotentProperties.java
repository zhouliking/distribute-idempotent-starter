package com.joelly.idempotent.config;

import com.joelly.idempotent.config.entity.RedisConfig;
import com.joelly.idempotent.enums.StorageTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "joelly.idempotent")
@Data
public class IdempotentProperties {

    private boolean enable = true;

    private StorageTypeEnum storage = StorageTypeEnum.spring_redisson;

    private RedisConfig redis;
}
