package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /*
    * 根据菜品id查询套餐id
    * */
    List<Long> getSetmealIdByDishId(List<Long> dishIds);

    /*
    * 在套餐中插入菜品
    * */
    @Insert("insert into setmeal_dish (setmeal_id, dish_id, name, price, copies) VALUES (#{setmealId},#{dishId},#{name},#{price},#{copies})")
    void insert(SetmealDish setmealDish);

    /*
    *根据套餐id查询菜品
    * */
    List<SetmealDish> getSetmealById(Long id);

    /*
    * 根据套餐id删除菜品
    * */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void delete(Long id);

    /*
    * 修改套餐
    * */
    void update(Dish dish);
}
