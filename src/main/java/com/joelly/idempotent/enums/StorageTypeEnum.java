package com.joelly.idempotent.enums;

public enum StorageTypeEnum {

    /**
     * 业务工程内部引入配置 redisson
     */
    spring_redisson,

    /**
     * 幂等组件内配置 redisson
     */
    inner_redisson,

    /**
     * 幂等组件内配置 mysql
     */
    mysql,

    ;

    public static boolean isRedisType(StorageTypeEnum typeEnum) {
        if (typeEnum == null) {
            return false;
        }
        return spring_redisson.equals(typeEnum) || inner_redisson.equals(typeEnum);
    }
}
