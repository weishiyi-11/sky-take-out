package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {


    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /*
    * 新增菜品
    * */
    @PostMapping
    @ApiOperation("新增菜品")
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        return Result.success();
    }

    /*
    * 菜品分页查询
    * */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}", dishPageQueryDTO);

        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /*
    * 批量删除菜品
    * */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result deleteById(@RequestParam List<Long> ids) {
        log.info("批量删除菜品：{}",ids);

        dishService.deleteById(ids);

        return Result.success();
    }


    /*
    * 根据id查询菜品及口味
    * */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getByIdWithFlavor(@PathVariable Long id) {
        log.info("根据id查询菜品：{}",id);

        DishVO dishVO = dishService.getByIdWithFlavor(id);

        return Result.success(dishVO);
    }

    /*
    * 根据id修改菜品及其口味
    * */
    @PutMapping
    @ApiOperation("修改菜品及其口味")
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品及其口味：{}",dishDTO);

        dishService.update(dishDTO);

        return Result.success();
    }

    /*
    * 根据分类id查询菜品
    * */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("根据分类id查询菜品：{}",categoryId);

        List<Dish> dishs = dishService.getByCategoryId(categoryId);

        return Result.success(dishs);
    }

    /*
    * 起售，停售菜品
    * */
    @PostMapping("/status/{status}")
    @ApiOperation("起售，停售菜品")
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("起售or停售菜品{}: {}",id,status);

        dishService.startOrStop(status,id);

        return Result.success();
    }


    /*
     * 清理Redis缓存数据
     * */
    private void cleanCache(String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern));
    }

}
















