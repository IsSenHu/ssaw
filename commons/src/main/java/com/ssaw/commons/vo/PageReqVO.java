package com.ssaw.commons.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author HuSen.
 * @date 2018/12/12 17:16.
 */
@Getter
@Setter
@ToString
public class PageReqVO<T> implements Serializable {
    private static final long serialVersionUID = 8174880318487614804L;
    private Integer page;
    private Integer size;
    private String sortValue;
    private String sortType;
    private T data;
}
