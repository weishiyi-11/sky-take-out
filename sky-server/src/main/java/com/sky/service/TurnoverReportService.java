package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface TurnoverReportService {

    /*
    * 营业额数据统计
    * */
    TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end);
}
