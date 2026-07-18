package com.sky.controller.user;

import com.google.common.hash.BloomFilter;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private BloomFilter<Long> dishBloomFilter;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final int BASE_TTL_MINUTES = 30;
    private static final int RANDOM_TTL_MINUTES = 10;

    /**
     * 条件查询
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<Setmeal>> list(Long categoryId) {

        //构建Redis的key
        String key = "setmealCache::setmeal_" + categoryId;

        //查询Redis,如果不存在才查询数据库
        List<Setmeal> list =  (List<Setmeal>) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() >= 0) {
            return Result.success(list);
        }

        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        list = setmealService.list(setmeal);

        // 随机 TTL：30分钟 ~ 40分钟
        int ttl = BASE_TTL_MINUTES + ThreadLocalRandom.current().nextInt(0, RANDOM_TTL_MINUTES);
        redisTemplate.opsForValue().set(key, list, ttl, TimeUnit.MINUTES);


        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {

        // 布隆过滤器判断
        if (!dishBloomFilter.mightContain(id)) {
            return Result.error(MessageConstant.SETMEALID_IS_ERROR); // 一定不存在，直接返回，不查缓存也不查 DB
        }

        //构建Redis的key
        String key = "dishCache::dish_" + id;

        //查询Redis,如果不存在才查询数据库
        List<DishItemVO> list =  (List<DishItemVO>) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() >= 0) {
            return Result.success(list);
        }

         list = setmealService.getDishItemById(id);

        // 随机 TTL：30分钟 ~ 40分钟
        int ttl = BASE_TTL_MINUTES + ThreadLocalRandom.current().nextInt(0, RANDOM_TTL_MINUTES);
        redisTemplate.opsForValue().set(key, list, ttl, TimeUnit.MINUTES);

        return Result.success(list);
    }
}
