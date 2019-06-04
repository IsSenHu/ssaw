package com.ssaw.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 信号量
 * 非公平版 分布式下 时间慢的系统可能会获取到额外的信号量 并且可能导致时间正常的系统获取不到本该可以获取的信号量
 *
 * 所以实现公平锁的目的就是让时间慢的系统不能获取到额外的信号量 时间快的机器不会导致时间正常的系统获取不到本该获取的信号量
 * 但是会导致获取到不该获取的信号量
 *
 * 可以实现公平版
 *
 * 有效的控制共享资源的并发数 不精确
 *
 * @author HuSen
 * @date 2019/5/10 11:26
 */
@SuppressWarnings("WeakerAccess")
@Slf4j
public class RedisSemaphore {

    private StringRedisTemplate template;

    private long times;

    /**
     * 默认过期时间
     */
    private static final Long DEFAULT_TIMEOUT = 10000L;

    private static final int RANK_OFFSET = 4;

    public RedisSemaphore(StringRedisTemplate template, long times) {
        this.template = template;
        this.times = times;
    }

    /**
     * 获取信号量
     *
     * @param key 资源标识键
     * @return 唯一标识
     */
    public String acquire(String key) {
        return acquire(key, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取信号量
     *
     * @param key      资源标识键
     * @param timeout  超时时间
     * @param timeUnit 超时单位
     * @return 唯一标识
     */
    public String acquire(String key, Long timeout, TimeUnit timeUnit) {
        // 生成唯一标识ID
        String id = UUID.randomUUID().toString();
        // 先加锁
        RedisLock lock = new RedisLock(template);
        try {
            boolean ifLock = lock.lock(key.concat(":lock"));
            if (!ifLock) {
                return null;
            }
            long millis = timeUnit.toMillis(timeout);
            Long increment = template.opsForValue().increment(key.concat(":count"));
            if (Objects.isNull(increment)) {
                return null;
            }
            List<Object> objects = template.executePipelined((RedisCallback<Object>) connection -> {
                // 清理过期的信号量
                connection.zRemRangeByScore(key.getBytes(), RedisZSetCommands.Range.range().lt(System.currentTimeMillis() - millis));
                // 对超时时间集合和信号量成员集合求交集
                connection.zInterStore(key.concat(":member").getBytes(), RedisZSetCommands.Aggregate.SUM, RedisZSetCommands.Weights.of(1, 0), key.getBytes(), key.concat(":member").getBytes());
                // 添加新的标识
                connection.zAdd(key.concat(":member").getBytes(), increment, id.getBytes());
                connection.zAdd(key.getBytes(), System.currentTimeMillis(), id.getBytes());
                // 获取排名, 低于最大次数的获取信号量失败
                connection.zRank(key.concat(":member").getBytes(), id.getBytes());
                // 使用executePipelined 必须返回null
                return null;
            });
            // 成功 则返回ID
            if (CollectionUtils.isNotEmpty(objects) && (Long) objects.get(RANK_OFFSET) < times) {
                return id;
            }
            // 失败 删除刚刚新增的key
            release(key, id);
        } catch (Exception e) {
            log.error("获取信号量异常:", e);
        } finally {
            lock.unlock(key.concat(":lock"));
        }
        return null;
    }

    /**
     * 释放信号量
     *
     * @param key 资源标识键
     * @param id  唯一标识
     */
    public void release(String key, String id) {
        try {
            template.opsForZSet().remove(key, id);
            template.opsForZSet().remove(key.concat(":member"), id);
        } catch (Exception e) {
            log.error("释放信号量失败:{}-{}:", key, id, e);
        }
    }
}