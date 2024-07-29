package com.joelly.idempotent.aspect;

import com.joelly.idempotent.annotation.Idempotent;
import com.joelly.idempotent.keys.LockKeyGenerator;
import com.joelly.idempotent.switchs.IdempotentProcessSwitch;
import com.joelly.idempotent.uniqueness.UniquenessVerification;
import com.joelly.idempotent.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
@Slf4j
public class IdempotentAspect {


    @Autowired
    private UniquenessVerification uniquenessVerification;

    @Before("@annotation(com.joelly.idempotent.annotation.Idempotent)")
    public void beforeAdvice(JoinPoint joinPoint) throws Exception {
        // 在此处编写需要执行的方法逻辑
        log.info("Before method execution: {}", joinPoint.getSignature());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Idempotent annotationIdempotent = signature.getMethod().getAnnotation(Idempotent.class);
        if (annotationIdempotent == null) {
            return;
        }
        LockKeyGenerator keyGenerator = ApplicationContextUtils.getBean(annotationIdempotent.keyGenerator());
        String key = keyGenerator.generateLockKey(joinPoint);
        log.info("Before method execution, pre: {}, key: {}", joinPoint.getSignature(), key);
        if (!IdempotentProcessSwitch.isPreAspectProcessSwitch()) {
            log.warn("Before method execution switch closed, pre: {}, key: {}", joinPoint.getSignature(), key);
            return;
        }
        uniquenessVerification.takeUse(key, annotationIdempotent);
    }

    @After("@annotation(com.joelly.idempotent.annotation.Idempotent)")
    public void after(JoinPoint joinPoint) throws Exception {
        // 在此处编写需要执行的方法逻辑
        log.info("after method execution: {}", joinPoint.getSignature());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Idempotent annotationIdempotent = signature.getMethod().getAnnotation(Idempotent.class);
        if (annotationIdempotent == null) {
            return;
        }
        if (!annotationIdempotent.vacate()) {
            log.info("after vacate switch closed, signature: {}", joinPoint.getSignature());
            return;
        }
        LockKeyGenerator keyGenerator = ApplicationContextUtils.getBean(annotationIdempotent.keyGenerator());
        String key = keyGenerator.generateLockKey(joinPoint);
        log.info("after method execution, pre: {}, key: {}", joinPoint.getSignature(), key);
        uniquenessVerification.vacate(key, annotationIdempotent);
    }
}
