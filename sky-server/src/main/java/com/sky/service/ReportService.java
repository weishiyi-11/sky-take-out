package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    /*
    * 营业额数据统计
    * */
    TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end);

    /*
    * 用户数量统计
    * */
    UserReportVO userReport(LocalDate begin, LocalDate end);

    /*
    * 订单数量统计
    * */
    OrderReportVO orderReport(LocalDate begin, LocalDate end);
}
