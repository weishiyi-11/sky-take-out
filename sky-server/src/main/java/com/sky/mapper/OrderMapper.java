package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {


    /*
    * 插入1条订单数据
    * */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);


    /*
    * 根据用户id查询订单号
    * */
    @Select("select number from orders where user_id = #{userId} and status = 1")
    Long getIdByUserId(Long userId);

    /*
    * 查看历史订单
    * */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /*
    * 根据订单id查看订单
    * */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);


    //根据状态统计订单数量
    @Select("select count(*) from orders where status = #{status}")
    Integer countOrder(Integer status);


    //查询过期订单
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> processOrderTime(Integer status, LocalDateTime time);


    /*
    *营业额数据统计
    * */
    Double sumAmount(LocalDateTime beginTime, LocalDateTime endTime,Integer status);
}
