package com.ssaw.commons.util.poi;

import java.util.List;

/**
 * 导出的Excel数据模型必须实现该接口
 * @author HuSen
 * @date 2018/11/8 9:36
 */
public interface ExcelVO {

    /**
     * 标题集合
     * @return 标题集合
     */
    List<String> colNames();

    /**
     * 每一列的值的集合
     * @return 每一列的值的集合
     */
    List<Object> cellValues();
}
