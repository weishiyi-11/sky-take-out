package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {


    /*
    * 批量插入订单明细数据
    * */
    void insertBatch(List<OrderDetail> orderDetails);

    /*
    * 删除订单明细
    * */
    @Delete("delete from order_detail where order_id = #{id}")
    void cancel(Long id);

    /*
     * 根据订单id查看订单详情
     * */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}
