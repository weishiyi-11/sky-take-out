package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
* 套餐相关接口
*
* */
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private RedisTemplate redisTemplate;


    /*
    * 新增套餐
    * */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐: {}", setmealDTO);

        setmealService.save(setmealDTO);

        return Result.success();
    }

    /*
    * 分页查询
    * */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询: {}", setmealPageQueryDTO);

        PageResult page = setmealService.getPage(setmealPageQueryDTO);

        return Result.success(page);
    }

    /*
    * 删除套餐
    * */
    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除套餐：: {}", ids);

        setmealService.delete(ids);

        return Result.success();
    }

    /*
    * 根据id查询套餐
    * */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐: {}",id);

        SetmealVO setmealVO = setmealService.getById(id);

        return Result.success(setmealVO);
    }

    /*
    * 修改套餐
    * */
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐: {}", setmealDTO);

        setmealService.update(setmealDTO);

        return Result.success();
    }


    /*
    * 启用，禁用套餐
    * */
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用or禁用套餐{}：{}",id,status);

        setmealService.startOrStop(status,id);

        //清理Redis缓存
        String key = "setmealCache:setmeal_" +  id;
        cleanCache(key);

        return Result.success();
    }

    /*
     * 清理Redis缓存数据
     * */
    private void cleanCache(String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern));
    }


}
