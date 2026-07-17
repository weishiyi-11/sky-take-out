package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;


    /*
    * 营业额数据统计
    * */
    public TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end) {

        //当天日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //当天营业额
        List<Double> turnoverList = new ArrayList<>();
        dateList.forEach(date->{
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);

            Double amount = orderMapper.sumAmount(map);
            amount = amount == null ? 0.0 : amount;
            turnoverList.add(amount);
        });

        //封装数据
        TurnoverReportVO turnoverReportVO = TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();


        return turnoverReportVO;
    }

    /*
    * 用户数量统计
    * */
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        //当天日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }


        //总共用户数量
        List<Integer> totalUserList = new ArrayList<>();
        //新增用户数量
        List<Integer> newUserList = new ArrayList<>();
        dateList.forEach(date->{
            Map map = new HashMap();

            //统计总共用户数量
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.put("endTime", endTime);
            Integer totalUser = userMapper.sumUser(map);
            totalUser =  totalUser == null ? 0 : totalUser;

            //统计新增用户数量
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            map.put("beginTime", beginTime);
            Integer newUser = userMapper.sumUser(map);
            newUser = newUser == null ? 0 : newUser;

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        });

        UserReportVO userReportVO = UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();


        return userReportVO;
    }

    /*
    * 订单数量统计
    * */
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {
        //当天日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> totalOrderList = new ArrayList<>();
        List<Integer> completeOrderList = new ArrayList<>();
        //总共订单
        Integer total = 0;
        //有效订单
        Integer complete = 0;
        //完成率
        Double completeRate = 0.0;

        for(LocalDate date:dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);

            //当天订单总数
            Integer totalorder = orderMapper.countOrders(map);
            total += totalorder;

            map.put("status", Orders.COMPLETED);
            //当天有效订单总数
            Integer completeorder = orderMapper.countOrders(map);
            complete += completeorder;

            totalOrderList.add(totalorder);
            completeOrderList.add(completeorder);
        }

        if(complete != 0){
            completeRate = complete.doubleValue() / total;
        }

        OrderReportVO orderReportVO = OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(totalOrderList,","))
                .validOrderCountList(StringUtils.join(completeOrderList,","))
                .totalOrderCount(total)
                .validOrderCount(complete)
                .orderCompletionRate(completeRate)
                .build();

        return orderReportVO;
    }

    /*
    * 销量排名统计
    * */
    public SalesTop10ReportVO getSalesReport(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> list = orderMapper.getTop10(beginTime,endTime);

        //获取商品名称
        List<String> names = list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        //获取商品销量
        List<Integer> sales = list.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(names,","))
                .numberList(StringUtils.join(sales,","))
                .build();

        return salesTop10ReportVO;
    }
}
