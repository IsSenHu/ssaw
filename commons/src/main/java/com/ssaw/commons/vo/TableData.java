package com.ssaw.commons.vo;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/12 12:00.
 */
@Setter
@Getter
public class TableData<T> {
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long totals;
    private List<T> content;
}
