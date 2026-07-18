package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /*
     * 插入菜品数据
     * */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /*
     * 菜品分页查询
     * */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
     * 根据id查询菜品
     * */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /*
     * 根据id删除菜品
     * */
    void deleteById(List<Long> ids);

    /*
    * 修改菜品
    * */
    void update(Dish dish);


    /*
     * 根据分类id查询菜品
     * */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /*
    * 加载所有有效菜品 ID 到布隆过滤器
    * */
    @Select("select * from dish")
    List<Dish> getAllDishs();
}