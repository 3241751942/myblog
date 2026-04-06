package com.zzl.myblog.dto;

import lombok.Data;

/**
 * 项目-技术关联数据传输对象
 */
@Data
public class ProjectTechRelationDTO {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 技术ID
     */
    private Integer techId;

    /**
     * 排序顺序
     */
    private Integer sortOrder;
}