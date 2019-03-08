package com.ssaw.commons.util.poi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HuSen on 2018/11/9 9:53.
 */
public class ExcelTestVo implements Serializable, ExcelVo {
    private static final long serialVersionUID = -4544002190825510002L;
    @ExcelColumn(value = "第一列")
    private String fieldOne;

    @ExcelColumn(value = "第二列")
    private Integer fieldTwo;

    @ExcelColumn(value = "第三列")
    private Double fieldThree;

    @ExcelColumn(value = "第四列", format = "yyyy-MM-dd HH:mm:ss")
    private Date fieldFour;

    @ExcelColumn(value = "第五列", format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fieldFive;

    @ExcelColumn(value = "第六列", format = "yyyy-MM-dd")
    private LocalDate fieldSix;

    public String getFieldOne() {
        return fieldOne;
    }

    public void setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
    }

    public Integer getFieldTwo() {
        return fieldTwo;
    }

    public void setFieldTwo(Integer fieldTwo) {
        this.fieldTwo = fieldTwo;
    }

    public Double getFieldThree() {
        return fieldThree;
    }

    public void setFieldThree(Double fieldThree) {
        this.fieldThree = fieldThree;
    }

    public Date getFieldFour() {
        return fieldFour;
    }

    public void setFieldFour(Date fieldFour) {
        this.fieldFour = fieldFour;
    }

    public LocalDateTime getFieldFive() {
        return fieldFive;
    }

    public void setFieldFive(LocalDateTime fieldFive) {
        this.fieldFive = fieldFive;
    }

    public LocalDate getFieldSix() {
        return fieldSix;
    }

    public void setFieldSix(LocalDate fieldSix) {
        this.fieldSix = fieldSix;
    }

    @Override
    public List<String> colNames() {
        List<String> list = new ArrayList<>(6);
        list.add("第一列");
        list.add("第二列");
        list.add("第三列");
        list.add("第四列");
        list.add("第五列");
        list.add("第六列");
        return list;
    }

    @Override
    public List<Object> cellValues() {
        List<Object> list = new ArrayList<>();
        list.add(fieldOne);
        list.add(fieldTwo);
        list.add(fieldThree);
        list.add(fieldFour);
        list.add(fieldFive);
        list.add(fieldSix);
        return list;
    }
}
