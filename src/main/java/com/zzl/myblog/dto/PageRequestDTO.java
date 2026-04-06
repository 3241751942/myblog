package com.zzl.myblog.dto;

import lombok.Data;

/**
 * 分页请求参数
 */
@Data
public class PageRequestDTO {

    /**
     * 当前页码，默认第1页
     */
    private Integer pageNum = 1;

    /**
     * 每页大小，默认10条
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy = "date";

    /**
     * 排序方向：ASC 升序，DESC 降序
     */
    private String orderDirection = "DESC";
}