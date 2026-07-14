package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    /*
    * 查询购物车
    * */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /*
    * 修改购物车数据
    * */
    @Update("update shopping_cart set number = #{number}")
    void update(ShoppingCart shoppingCart);

    /*
    * 添加商品到购物车
    * */
    void insert(ShoppingCart shoppingCart);
}
