package com.ssaw.redis.request;

import com.google.common.collect.Lists;
import com.ssaw.commons.vo.TableData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @date 2019/4/24 10:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest<T> {
    private Long page;
    private Integer size;
    private Long totalPages;
    private Long totals;
    private List<T> content;
    private boolean asc;

    public static <T> PageRequest<T> of(Long page, Integer size, boolean asc) {
        return new PageRequest<>(page, size, 0L, 0L, Lists.newArrayList(), asc);
    }

    public static <T> PageRequest<T> of(Long page, Integer size, boolean asc, Long totals) {
        return new PageRequest<>(page, size, 0L, totals, Lists.newArrayList(), asc);
    }

    public <R> TableData<R> toViews(Function<? super T, ? extends R> mapper) {
        TableData<R> tableData = new TableData<>();
        tableData.setPage(this.getPage().intValue());
        tableData.setSize(this.getSize());
        tableData.setTotals(this.getTotals());
        tableData.setTotalPages(this.getTotalPages().intValue());
        tableData.setContent(this.content.stream().map(mapper).collect(Collectors.toList()));
        return tableData;
    }
}