package com.zzl.myblog.dto;

import com.zzl.myblog.entity.Project;
import com.zzl.myblog.entity.Tech;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目响应数据传输对象
 * 用于返回给前端的项目数据
 */
@Data
public class ProjectResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private String demoUrl;
    private String githubUrl;
    private LocalDate date;
    private List<Tech> techs;

    /**
     * 从 Project 实体转换为 ProjectResponseDTO
     */
    public static ProjectResponseDTO fromEntity(Project project) {
        if (project == null) {
            return null;
        }
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setCategory(project.getCategory());
        dto.setImageUrl(project.getImageUrl());
        dto.setDemoUrl(project.getDemoUrl());
        dto.setGithubUrl(project.getGithubUrl());
        dto.setDate(project.getDate());
        dto.setTechs(project.getTechs());
        return dto;
    }

    /**
     * 从 Project 实体列表转换为 ProjectResponseDTO 列表
     */
    public static List<ProjectResponseDTO> fromEntityList(List<Project> projects) {
        if (projects == null) {
            return null;
        }
        return projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}