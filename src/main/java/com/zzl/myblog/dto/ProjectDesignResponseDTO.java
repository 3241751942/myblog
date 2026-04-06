package com.zzl.myblog.dto;

import com.zzl.myblog.entity.ProjectDesign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDesignResponseDTO {
    private Long id;
    private Long projectId;
    private String title;
    private String content;
    private Integer sort;

    public static ProjectDesignResponseDTO fromEntity(ProjectDesign design) {
        return new ProjectDesignResponseDTO(
                design.getId(),
                design.getProjectId(),
                design.getTitle(),
                design.getContent(),
                design.getSort()
        );
    }
}