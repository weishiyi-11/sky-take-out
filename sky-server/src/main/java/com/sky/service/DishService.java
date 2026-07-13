package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /*
    * 新增菜品和对应的口味数据
    * */
    public void saveWithFlavor(DishDTO dishDTO);

    /*
    * 菜品分页查询
    * */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
    * 批量删除菜品
    * */
    void deleteById(List<Long> ids);

    /*
    * 根据id查询菜品及口味
    * */
    DishVO getByIdWithFlavor(Long dishId);

    /*
    * 修改菜品及其口味
    * */
    void update(DishDTO dishDTO);

    /*
    * 根据分类id查询菜品
    * */
    List<Dish> getByCategoryId(Long categoryId);
}
