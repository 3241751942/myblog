package com.zzl.myblog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectDesignDTO {
    private Long id;// 前端修改必须传这个

    @NotNull(message="项目id不能为空")
    private Long projectId;// 项目ID

    private String title;
    private String content;
    private Integer sort;
}