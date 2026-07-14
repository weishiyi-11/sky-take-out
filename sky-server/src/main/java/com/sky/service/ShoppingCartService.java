package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface ShoppingCartService {

    /*
    * 添加商品到购物车
    * */
    void add(@RequestBody ShoppingCartDTO shoppingCartDTO);
}
