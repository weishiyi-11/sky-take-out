package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /*
    * 插入口味
    * */
    void insertBatch(List<DishFlavor> flavors);

    /*
    * 根据菜品id删除口味
    * */
    void deleteByDishId(List<Long> dishids);

    /*
    * 根据id查询口味
    * */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
