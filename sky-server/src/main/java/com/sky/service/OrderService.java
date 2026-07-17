package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /*
    * 用户下单
    * */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);


    /**
     * 获取订单id
     * @param userId
     * @return
     *//*
    *
    * */
    Long getId(Long userId);

    /*
    * 查看历史订单
    * */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /*
    * 查看订单详情
    * */
    OrderVO getOrderDetail(Long id);

    /*
    * 用户取消订单
    * */
    void cancel(Long id) throws Exception;

    /*
    * 商家取消订单
    * */
    void cancel(OrdersCancelDTO  ordersCancelDTO) ;

    /*
    *再来一单
    * */
    void repetition(Long id);

    /*
    * 订单搜索
    * */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /*
    * 接单
    * */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /*
    * 拒单
    * */
    void rejection(OrdersCancelDTO ordersCancelDTO);

    /*
    * 派送订单
    * */
    void delivery(Long id);

    /*
    * 完成订单
    * */
    void complete(Long id);

    /*
    * 各个状态的订单数量统计
    * */
    OrderStatisticsVO getStatistics();

    /*
    * 用户催单
    * */
    void reminder(Long id);
}
