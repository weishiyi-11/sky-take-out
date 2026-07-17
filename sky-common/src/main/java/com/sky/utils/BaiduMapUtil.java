package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class BaiduMapUtil {

    @Value("${sky.baidu.ak}")
    private String ak;

    @Value("${sky.shop.address}")
    private String shopAddress;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 地址 → 经纬度 lat,lng
     */
    public String getLocation(String address) {
        try {
            // 地址必须编码，否则中文报错
            String encodeAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.name());

            String url = "https://api.map.baidu.com/geocoding/v3/"
                    + "?address=" + encodeAddress
                    + "&output=json"
                    + "&ak=" + ak;

            String json = restTemplate.getForObject(url, String.class);
            JSONObject result = JSONObject.parseObject(json);

            // 判断接口是否调用成功
            int status = result.getIntValue("status");
            if (status != 0) {
                throw new RuntimeException("百度地图接口异常：" + result.getString("message"));
            }

            JSONObject res = result.getJSONObject("result");
            JSONObject location = res.getJSONObject("location");

            return location.getDouble("lat") + "," + location.getDouble("lng");
        } catch (Exception e) {
            throw new RuntimeException("获取经纬度失败：" + e.getMessage());
        }
    }

    /**
     * 计算两点距离，单位：米
     */
    public double getDistance(String origin, String destination) {
        try {
            String url = "https://api.map.baidu.com/directionlite/v1/riding"
                    + "?origin=" + origin
                    + "&destination=" + destination
                    + "&ak=" + ak;

            String json = restTemplate.getForObject(url, String.class);
            JSONObject result = JSONObject.parseObject(json);

            int status = result.getIntValue("status");
            if (status != 0) {
                throw new RuntimeException("距离计算接口异常：" + result.getString("message"));
            }

            JSONObject routes = result.getJSONObject("result")
                    .getJSONArray("routes")
                    .getJSONObject(0);

            return routes.getDouble("distance");
        } catch (Exception e) {
            throw new RuntimeException("计算距离失败：" + e.getMessage());
        }
    }

    /**
     * 校验是否在 5公里 配送范围内
     */
    public boolean checkDistance(String userAddress) {
        String shopLoc = getLocation(shopAddress);
        String userLoc = getLocation(userAddress);
        double distance = getDistance(shopLoc, userLoc);

        // 5公里 = 5000米
        return distance <= 5000;
    }
}