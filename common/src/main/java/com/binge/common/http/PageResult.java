package com.binge.common.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author JiaBin Huang
 * @date 2020/10/24
 **/
@Data
@AllArgsConstructor
public class PageResult {
    private Object records;
    private long total;

    public static PageResult instance(Object records, long total) {
        return new PageResult(records, total);
    }
}
