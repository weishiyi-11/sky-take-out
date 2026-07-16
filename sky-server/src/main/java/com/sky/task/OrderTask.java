package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;


    @Scheduled(cron = "0 * * * * ?")
    public void overOrderTime(){
      log.info("自动处理超时订单");

        LocalDateTime time = LocalDateTime.now().minusMinutes(15);

        List<Orders> list = orderMapper.processOrderTime(Orders.PENDING_PAYMENT,time);

        if(list != null && list.size()>0){
            for(Orders o:list){
                o.setStatus(Orders.CANCELLED);
                o.setCancelReason(MessageConstant.ORDER_OVER_TIME);
                orderMapper.update(o);
            }
        }

    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void overDeliveryTime(){
        log.info("自动处理异常待派送订单");

        LocalDateTime time = LocalDateTime.now().minusMinutes(60);

        List<Orders> list = orderMapper.processOrderTime(Orders.DELIVERY_IN_PROGRESS,time);

        if(list != null && list.size()>0){
            for(Orders o:list){
                o.setStatus(Orders.COMPLETED);
                orderMapper.update(o);
            }
        }

    }
}
