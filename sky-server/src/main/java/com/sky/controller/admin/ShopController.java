package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Key;


/*
店铺相关接口
*/
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /*
    * 设置店铺状态
    * */
    @PutMapping("{status}")
    @ApiOperation("设置店铺状态")
    public Result setSatus(@PathVariable Integer status) {
        log.info("店铺状态 = {}", status == 1 ? "营业中" : "已打烊");

        redisTemplate.opsForValue().set(KEY, status);

        return  Result.success();
    }

    /*
    * 查询店铺状态
    * */
    @GetMapping("status")
    @ApiOperation("查询店铺状态")
    public Result<Integer> getSatus() {

        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);

        log.info("店铺状态 = {}", status == 1 ? "营业中" : "已打烊");
        return  Result.success(status);
    }
}
