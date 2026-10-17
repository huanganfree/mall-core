package com.ha.springbootdemo.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private List<T> records;

    public static <T> PageResult<T> success(List<T> list, long total) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setRecords(list);
        return result;
    }
}
