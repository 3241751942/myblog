package com.zzl.myblog.dto;

import lombok.Data;

@Data
public class ProjectDesignDTO {
    private Long id;          // 前端修改必须传这个
    private Long projectId;   // 项目ID
    private String title;
    private String content;
    private Integer sort;
}