package com.ssaw.commons.util.poi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hszyp
 */
public class ExcelCVo implements Serializable, ExcelVo {

    @ExcelColumn(value = "订单号")
    private String orderBn;

    @ExcelColumn(value = "快喝给的经纬度")
    private String kuaiheDis;

    @ExcelColumn(value = "传给配送app的经纬度")
    private String toDeliveryAppDis;

    public String getKuaiheDis() {
        return kuaiheDis;
    }

    public void setKuaiheDis(String kuaiheDis) {
        this.kuaiheDis = kuaiheDis;
    }

    public String getToDeliveryAppDis() {
        return toDeliveryAppDis;
    }

    public void setToDeliveryAppDis(String toDeliveryAppDis) {
        this.toDeliveryAppDis = toDeliveryAppDis;
    }

    public String getOrderBn() {
        return orderBn;
    }

    public void setOrderBn(String orderBn) {
        this.orderBn = orderBn;
    }

    @Override
    public List<String> colNames() {
        List<String> list = new ArrayList<>(3);
        list.add("订单号");
        list.add("快喝给的经纬度");
        list.add("传个配送app的经纬度");
        return list;
    }

    @Override
    public List<Object> cellValues() {
        List<Object> list = new ArrayList<>();
        list.add(orderBn);
        list.add(kuaiheDis);
        list.add(toDeliveryAppDis);
        return list;
    }
}
