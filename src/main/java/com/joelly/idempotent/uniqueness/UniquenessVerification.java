package com.joelly.idempotent.uniqueness;

import com.joelly.idempotent.annotation.Idempotent;

/**
 * 唯一性校验方式
 */
public interface UniquenessVerification {

    /**
     * 唯一占用资源
     * @param uniquenessKey
     * @param annotationIdempotent
     * @throws Exception
     */
    void takeUse(String uniquenessKey, Idempotent annotationIdempotent) throws Exception;

    /**
     * 执行完后执行操作，如释放资源（可在takeUse方法中设置超时释放）
     * @param uniquenessKey
     * @param annotationIdempotent
     * @throws Exception
     */
    void vacate(String uniquenessKey, Idempotent annotationIdempotent) throws Exception;

}
