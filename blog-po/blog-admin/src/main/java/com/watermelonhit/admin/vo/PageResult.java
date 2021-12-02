package com.watermelonhit.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
@Data
public class PageResult<T> {

    private List<T> list;

    private Long total;
}