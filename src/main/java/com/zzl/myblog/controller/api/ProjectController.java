package com.zzl.myblog.controller.api;

import com.zzl.myblog.dto.ProjectDTO;
import com.zzl.myblog.dto.ProjectResponseDTO;
import com.zzl.myblog.dto.TechResponseDTO;
import com.zzl.myblog.dto.ApiResponseDTO;
import com.zzl.myblog.entity.Project;
import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.service.ProjectService;
import com.zzl.myblog.service.ProjectTechService;
import com.zzl.myblog.service.TechService;
import com.zzl.myblog.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name="项目技术栈管理",description="和项目相关的增删改查")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectTechService projectTechService;
    private final TechService techService;

    @GetMapping("/simple")
    @Operation(summary = "获取所有项目", description = "获取所有项目（简单版，不含技术标签）")
    public ApiResponseDTO<List<ProjectResponseDTO>> getAllProjectsSimple() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping
    @Operation(summary = "获取所有项目及其标签",description = "获取所有项目，含标签")
    public ApiResponseDTO<List<ProjectResponseDTO>> getAllProjectsWithTechs() {
        List<Project> projects = projectService.getAllProjectsWithTechs();
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "通过项目id获取项目",description="通过项目id获取单个项目")
    public ApiResponseDTO<ProjectResponseDTO> getProjectById(
            @PathVariable @Parameter(description = "项目id",required = true) Long id) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
        return ApiResponseDTO.success(ProjectResponseDTO.fromEntity(project));
    }

    @GetMapping("/{projectId}/techs")
    @Operation(summary = "通过项目id项目的标签",description="通过该项目id获取该项目的标签")
    public ApiResponseDTO<List<TechResponseDTO>> getTechsByProjectId(@PathVariable @Parameter(description = "项目id",required = true) Long projectId) {
        List<Tech> techs = techService.getTechsByProjectId(projectId);
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/category/{category}")
    public ApiResponseDTO<List<ProjectResponseDTO>> getProjectsByCategory(@PathVariable String category) {
        List<Project> projects = projectService.getProjectsByCategory(category);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/title")
    public ApiResponseDTO<List<ProjectResponseDTO>> getProjectsByTitle(@RequestParam String title) {
        List<Project> projects = projectService.getProjectsByTitle(title);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/search")
    public ApiResponseDTO<List<ProjectResponseDTO>> searchProjects(@RequestParam String keyword) {
        List<Project> projects = projectService.searchProjects(keyword);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/tech/{techName}")
    public ApiResponseDTO<List<ProjectResponseDTO>> getProjectsByTechName(
            @PathVariable @NotBlank(message = "名字不能为空") String techName) {
        List<Project> projects = projectService.getProjectsByTechName(techName);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/advanced-search")
    public ApiResponseDTO<List<ProjectResponseDTO>> advancedSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String techName) {
        List<Project> projects = projectService.advancedSearch(title, category, startDate, endDate, techName);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @PostMapping
    public ApiResponseDTO<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        Project createdProject = projectService.createProject(project);

        if (projectDTO.getTechs() != null && !projectDTO.getTechs().isEmpty()) {
            projectTechService.addTechsToProjectByName(createdProject.getId(), projectDTO.getTechs());
        }

        Project result = projectService.getProjectById(createdProject.getId())
                .orElseThrow(() -> new ResourceNotFoundException("项目创建失败"));
        return ApiResponseDTO.success(ProjectResponseDTO.fromEntity(result));
    }

    @PutMapping("/{id}")
    public ApiResponseDTO<ProjectResponseDTO> updateProject(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody ProjectDTO projectDTO) {
        projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));

        Project project = new Project();
        project.setId(id);
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setCategory(projectDTO.getCategory());
        project.setImageUrl(projectDTO.getImageUrl());
        project.setDemoUrl(projectDTO.getDemoUrl());
        project.setGithubUrl(projectDTO.getGithubUrl());
        project.setDate(LocalDate.parse(projectDTO.getDate()));

        projectService.updateProject(project);
        if (projectDTO.getTechs() != null) {
            projectTechService.updateProjectTechs(id, projectDTO.getTechs());
        }

        Project result = projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
        return ApiResponseDTO.success(ProjectResponseDTO.fromEntity(result));
    }

    @DeleteMapping("/{id}")
    public ApiResponseDTO<Void> deleteProject(
            @PathVariable @NotNull(message = "项目id不能为空") Long id) {
        projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
        projectService.deleteProject(id);
        return ApiResponseDTO.success();
    }

    @PostMapping("/{projectId}/techs")
    public ApiResponseDTO<Void> addTechsToProject(
            @PathVariable @NotNull(message="项目id不能为空") Long projectId,
            @RequestBody List<String> techNames) {
        projectService.getProjectById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
        projectTechService.addTechsToProjectByName(projectId, techNames);
        return ApiResponseDTO.success();
    }

    @DeleteMapping("/{projectId}/techs")
    public ApiResponseDTO<Void> removeTechsFromProject(
            @PathVariable Long projectId,
            @RequestBody List<String> techNames) {
        projectService.getProjectById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
        projectTechService.removeTechsFromProject(projectId, techNames);
        return ApiResponseDTO.success();
    }

    @PutMapping("/{projectId}/techs")
    public ApiResponseDTO<Void> updateProjectTechs(
            @PathVariable Long projectId,
            @RequestBody List<String> techNames) {
        projectService.getProjectById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));
        projectTechService.updateProjectTechs(projectId, techNames);
        return ApiResponseDTO.success();
    }

    private Project convertToEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setCategory(dto.getCategory());
        project.setImageUrl(dto.getImageUrl());
        project.setDemoUrl(dto.getDemoUrl());
        project.setGithubUrl(dto.getGithubUrl());
        project.setDate(LocalDate.parse(dto.getDate()));
        return project;
    }
}