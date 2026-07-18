package com.sky.config;

import com.google.common.hash.BloomFilter;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Slf4j
public class BloomFilterInitializer implements CommandLineRunner {

    @Autowired
    private BloomFilter<Long> dishBloomFilter;

    @Autowired
    private BloomFilter<Long> setmealBloomFilter;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void run(String... args) {
        log.info("加载布隆过滤器");


        // 加载所有有效菜品的分类id到布隆过滤器
        List<Dish> dishes = dishMapper.getAllDishs();
        for (Dish dish : dishes) {
            if (dish.getStatus() == 1) { // 只放起售的的分类id
                dishBloomFilter.put(dish.getCategoryId());
            }
        }

        // 加载所有有效套餐id到布隆过滤器
        List<Setmeal> setmeals = setmealMapper.getAllSetmeal();
        for (Setmeal setmeal : setmeals) {
            if (setmeal.getStatus() == 1) {
                setmealBloomFilter.put(setmeal.getId());
            }
        }
    }
}