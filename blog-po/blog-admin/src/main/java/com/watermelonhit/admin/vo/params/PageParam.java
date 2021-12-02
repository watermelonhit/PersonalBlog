package com.watermelonhit.admin.vo.params;

import lombok.Data;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/28
 */
@Data
public class PageParam {

    private Integer currentPage;

    private Integer pageSize;

    private String queryString;
}