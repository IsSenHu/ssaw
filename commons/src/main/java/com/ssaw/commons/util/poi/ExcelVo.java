package com.ssaw.commons.util.poi;

import java.util.List;

/**
 * Created by HuSen on 2018/11/8 9:36.
 */
public interface ExcelVo {
    List<String> colNames();
    List<Object> cellValues();
}
