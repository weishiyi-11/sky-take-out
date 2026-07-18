package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
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

    /*
    * 销量排名统计
    * */
    SalesTop10ReportVO getSalesReport(LocalDate begin, LocalDate end);

    /**
     * 导出运营数据报表
     * @param response
     */
    public void exportBusinessData(HttpServletResponse response);
}
