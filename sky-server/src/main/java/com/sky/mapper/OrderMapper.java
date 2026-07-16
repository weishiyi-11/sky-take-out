package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    Long getIdByUserId(Long currentId);

    /*
    * 查看历史订单
    * */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /*
    * 根据id查看订单
    * */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);


    //统计待接单数量
    @Select("select count(*) from orders where status = #{status}")
    Integer toBeConfirmed(Integer status);

    //统计待派送数量
    @Select("select count(*) from orders where status = #{status}")
    Integer confirmed(Integer status);

    //统计派送中数量
    @Select("select count(*) from orders where status = #{status}")
    Integer deliveryInProgress(Integer status);
}
