package com.sky.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomFilterConfig {

    /**
     * 菜品 ID 布隆过滤器
     * expectedInsertions: 预期数据量（根据你数据库菜品数量预估）
     * fpp: 误判率，0.01 表示 1%
     */
    @Bean
    public BloomFilter<Long> dishBloomFilter() {
        return BloomFilter.create(
                Funnels.longFunnel(),
                1000,       // 预期插入量，根据实际菜品数量调整
                0.01        // 误判率 1%
        );
    }

    /**
     * 套餐 ID 布隆过滤器
     */
    @Bean
    public BloomFilter<Long> setmealBloomFilter() {
        return BloomFilter.create(
                Funnels.longFunnel(),
                500,        // 预期套餐数量
                0.01
        );
    }
}