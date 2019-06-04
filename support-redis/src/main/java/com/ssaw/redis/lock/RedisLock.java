package com.ssaw.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis锁
 *
 * 后续可修改为可重入的锁
 *
 * @author HuSen
 * @date 2019/5/10 15:25
 */
@SuppressWarnings("WeakerAccess")
@Slf4j
public class RedisLock {

    private StringRedisTemplate template;

    public RedisLock(StringRedisTemplate template) {
        this.template = template;
    }

    private String value;

    /**
     * 默认超时时间 10秒
     */
    private final static long DEFAULT_TIMEOUT = 10000;

    public boolean lock(String key) {
        return lock(key, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public boolean lock(String key, long timeout, TimeUnit unit) {
        try {
            // 开始时间
            long start = System.currentTimeMillis();
            while (start + unit.toMillis(timeout) >= System.currentTimeMillis()) {
                String string = UUID.randomUUID().toString();
                Boolean success = template.opsForValue().setIfAbsent(key, string, timeout, unit);
                if (Objects.nonNull(success) && success) {
                    value = string;
                    break;
                }
                Thread.sleep(10);
            }
            return StringUtils.isNotBlank(this.value);
        } catch (Exception e) {
            log.error("对 {} 加锁失败:", key, e);
            return false;
        }
    }

    public void unlock(String key) {
        try {
            String value = template.opsForValue().get(key);
            // 相等才能解锁 不然可能会释放错锁
            if (StringUtils.equals(this.value, value)) {
                template.delete(key);
            }
        } catch (Exception e) {
            log.error("{} 解锁失败:", key, e);
        }
    }
}