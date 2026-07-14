package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ShoppingCartService {

    /*
    * 添加商品到购物车
    * */
    void add(@RequestBody ShoppingCartDTO shoppingCartDTO);

    /*
    * 查看购物车
    * */
    List<ShoppingCart> showShoppingCart();
}
