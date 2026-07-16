package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
    * 订单搜索
    * */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索: {}", ordersPageQueryDTO);

        PageResult pageResult =  orderService.conditionSearch(ordersPageQueryDTO);


        return Result.success(pageResult);
    }

    /*
    *查看订单详情
    * */
    @GetMapping("/details/{id}")
    @ApiOperation("查看订单详情")
    public Result<OrderVO> getDetailById(@PathVariable Long id){
        log.info("查看订单详情：{}",id);

        OrderVO orderVO = orderService.getOrderDetail(id);

        return Result.success(orderVO);

    }

    /*
    * 接单
    * */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单：{}",ordersConfirmDTO);

        orderService.confirm(ordersConfirmDTO);

        return Result.success();
    }

    /*
    * 拒单
    * */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersCancelDTO  ordersCancelDTO){
        log.info("拒单：{}",ordersCancelDTO);

        orderService.rejection(ordersCancelDTO);

        return Result.success();
    }

    /*
     * 拒单
     * */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("取消订单：{}",ordersCancelDTO);

        orderService.cancel(ordersCancelDTO);

        return Result.success();
    }

    /*
     * 派送订单
     * */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id){
        log.info("派送订单：{}",id);

        orderService.delivery(id);

        return Result.success();
    }

    /*
     * 派送订单
     * */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id){
        log.info("完成订单：{}",id);

        orderService.complete(id);

        return Result.success();
    }

    /*
    * 各个状态的订单数量统计
    * */
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics(){
        log.info("各个状态的订单数量统计");

        OrderStatisticsVO orderStatisticsVO = orderService.getStatistics();

        return Result.success(orderStatisticsVO);
    }

}
