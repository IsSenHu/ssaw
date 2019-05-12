package com.ssaw.redis.dao;

import com.ssaw.redis.request.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @date 2019/4/24 10:06
 */
public class BaseDao<T> {

    private final StringRedisTemplate stringRedisTemplate;

    public BaseDao(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void page(String key, PageRequest<T> page, Function<String, T> function) {
        ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
        Long count = Objects.isNull(page.getTotals()) || page.getTotals() == 0L ? zSet.count(key, Double.MIN_VALUE, Double.MAX_VALUE) : page.getTotals();
        Set<String> keys;
        if (page.isAsc()) {
            keys = zSet.rangeByScore(key, Double.MIN_VALUE, Double.MAX_VALUE, (page.getPage() - 1) * page.getSize(), page.getSize());
        } else {
            keys = zSet.reverseRangeByScore(key, Double.MIN_VALUE, Double.MAX_VALUE, (page.getPage() - 1) * page.getSize(), page.getSize());
        }
        Assert.notNull(keys, "keys should not null");
        List<T> collect = keys.stream().map(function).collect(Collectors.toList());
        page.setContent(collect);
        page.setTotals(count);
        long totalPages = Objects.nonNull(count) ? (count % page.getSize() == 0 ? count / page.getSize() : count / page.getSize() + 1) : 0;
        page.setTotalPages(totalPages);
    }
}