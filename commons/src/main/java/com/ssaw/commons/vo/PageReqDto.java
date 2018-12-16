package com.ssaw.commons.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HuSen.
 * @date 2018/12/12 17:16.
 */
@Getter
@Setter
public class PageReqDto<T> {
    private Integer page = 1;
    private Integer size = 10;
    private String sortValue;
    private String sortType;
    private T data;
}
