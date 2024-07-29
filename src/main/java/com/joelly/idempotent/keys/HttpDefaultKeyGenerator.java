package com.joelly.idempotent.keys;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HttpDefaultKeyGenerator implements LockKeyGenerator {

    @Override
    public String generateLockKey(JoinPoint joinPoint) {
        // 获取http对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String fullPath = request.getRequestURI();
        String key = request.getRemoteHost();
        if (request.getSession() != null) {
             key = request.getSession().getId();
        }
        log.info("generateLockKey, fullPath: {}, key: {}", fullPath, key);
        return String.format("%s_%s", fullPath, key);
    }
}
