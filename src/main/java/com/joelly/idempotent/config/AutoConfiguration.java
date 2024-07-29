package com.joelly.idempotent.config;

import com.joelly.idempotent.aspect.IdempotentAspect;
import com.joelly.idempotent.enums.StorageTypeEnum;
import com.joelly.idempotent.exception.IdempotentConfigException;
import com.joelly.idempotent.factory.RedisServerConfigFactory;
import com.joelly.idempotent.keys.HttpDefaultKeyGenerator;
import com.joelly.idempotent.uniqueness.RedisUniquenessVerification;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 启动配置与注入
 */
@Configuration
@ComponentScan(basePackages = {"com.joelly.idempotent"})
@ConditionalOnProperty(name = "joelly.idempotent.enable", havingValue = "true")
@ConfigurationProperties
@Slf4j
public class AutoConfiguration {

    @Autowired
    private IdempotentProperties idempotentProperties;

    @Bean
    public IdempotentAspect getIdempotentAspect() {
        log.info("init idempotent aspect, config: {}", idempotentProperties);
        return new IdempotentAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("'${joelly.idempotent.storage:spring_redisson}'.equals('inner_redisson') " +
            "|| '${joelly.idempotent.storage:spring_redisson}'.equals('spring_redisson')")
    public RedisUniquenessVerification getRedisUniquenessVerification(@Autowired(required = false) RedissonClient redissonClient) {
        log.info("init idempotent RedisUniquenessVerification");
        if (StorageTypeEnum.inner_redisson.equals(idempotentProperties.getStorage())) {
            Config config = RedisServerConfigFactory.createVehicle(idempotentProperties.getRedis());
            log.info("init idempotent inner redisson, config: {}", config);
            return new RedisUniquenessVerification(Redisson.create(config));
        }
        if (redissonClient == null) {
            throw new IdempotentConfigException("no redisson client in spring boot ! ");
        }
        return new RedisUniquenessVerification(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpDefaultKeyGenerator getHttpDefaultKeyGenerator() {
        log.info("init idempotent HttpDefaultKeyGenerator");
        return new HttpDefaultKeyGenerator();
    }
}
