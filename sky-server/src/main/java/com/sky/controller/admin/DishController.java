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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/dish")
@Api("菜品相关接口")
@Slf4j
public class DishController {


    @Autowired
    private DishService dishService;

    /*
    * 新增菜品
    * */
    @PostMapping
    @ApiOperation("新增菜品")
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
    @GetMapping
    @ApiOperation("修改菜品及其口味")
    public Result update(DishDTO dishDTO) {
        log.info("修改菜品及其口味：{}",dishDTO);

        dishService.update(dishDTO);

        return Result.success();
    }
}
















