package com.zzl.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDesign {
    private Long id;          // 自增 唯一 主键
    private Long projectId;   // 关联项目ID
    private String title;     // 不唯一
    private String content;
    private Integer sort;
}