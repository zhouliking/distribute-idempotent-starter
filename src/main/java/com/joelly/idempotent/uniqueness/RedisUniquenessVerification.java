package com.joelly.idempotent.uniqueness;

import com.joelly.idempotent.annotation.Idempotent;
import com.joelly.idempotent.exception.IdempotentInterceptException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@Slf4j
public class RedisUniquenessVerification implements UniquenessVerification {


    private RedissonClient redissonClient;


    public RedisUniquenessVerification(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void takeUse(String uniquenessKey, Idempotent annotationIdempotent) throws Exception {
        RLock lock = redissonClient.getLock(uniquenessKey);
        if (lock.isLocked()) {
            throw new IdempotentInterceptException(annotationIdempotent.msg());
        }
        boolean lockResult = lock.tryLock(annotationIdempotent.waitTime(),
                annotationIdempotent.expireTime(), annotationIdempotent.timeUnit());
        if (!lockResult) {
            throw new IdempotentInterceptException(annotationIdempotent.msg());
        }
    }

    @Override
    public void vacate(String uniquenessKey, Idempotent annotationIdempotent) throws Exception {
        RLock lock = redissonClient.getLock(uniquenessKey);
        if (lock == null) {
            return;
        }
        if (annotationIdempotent.vacate() && lock.isLocked()) {
            lock.unlock();
            log.info("vacate success, uniquenessKey: {}", uniquenessKey);
        }
    }


}
