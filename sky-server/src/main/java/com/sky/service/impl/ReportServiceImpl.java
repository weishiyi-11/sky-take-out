package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private WorkspaceService workspaceService;


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

    /**
     * 导出运营数据报表
     * @param response
     */
    public void exportBusinessData(HttpServletResponse response) {
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);
            // ========== 创建居中样式（只需创建一次，复用） ==========
            XSSFCellStyle centerStyle = excel.createCellStyle();
            //水平居中
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            //垂直居中
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            //获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            XSSFCell timeCell = sheet.getRow(1).getCell(1);
            timeCell.setCellValue("时间：" + dateBegin + "至" + dateEnd);
            timeCell.setCellStyle(centerStyle); // 设置居中样式

            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                XSSFCell dateCell = row.getCell(1);
                dateCell.setCellValue(date.toString());
                dateCell.setCellStyle(centerStyle); // 明细日期居中
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
