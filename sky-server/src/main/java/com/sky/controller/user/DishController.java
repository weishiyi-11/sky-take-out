package com.sky.controller.user;

import com.google.common.hash.BloomFilter;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private BloomFilter<Long> dishBloomFilter;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final int BASE_TTL_MINUTES = 30;
    private static final int RANDOM_TTL_MINUTES = 10;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);

        // 布隆过滤器判断
        if (!dishBloomFilter.mightContain(categoryId)) {
            return Result.error(MessageConstant.CATEGORYID_IS_ERROR); // 一定不存在，直接返回，不查缓存也不查 DB
        }

        //构建Redis的key
        String key = "dishCache::category_" + categoryId;

        //查询Redis，如果Redis不存在才去查询数据库
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() >= 0) {
            return Result.success(list);
        }

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        list = dishService.listWithFlavor(dish);

        // 随机 TTL：30分钟 ~ 40分钟
        int ttl = BASE_TTL_MINUTES + ThreadLocalRandom.current().nextInt(0, RANDOM_TTL_MINUTES);
        redisTemplate.opsForValue().set(key, list, ttl, TimeUnit.MINUTES);

        return Result.success(list);
    }


}
