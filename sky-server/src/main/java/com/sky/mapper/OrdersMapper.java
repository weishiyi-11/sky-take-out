package com.sky.mapper;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper {


    /*
    * 插入1条订单数据
    * */
    void insert(Orders orders);
}
