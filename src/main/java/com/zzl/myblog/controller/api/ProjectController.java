package com.zzl.myblog.controller.api;

import com.zzl.myblog.dto.ProjectDTO;
import com.zzl.myblog.dto.ProjectResponseDTO;
import com.zzl.myblog.dto.TechResponseDTO;
import com.zzl.myblog.entity.Project;
import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.service.ProjectService;
import com.zzl.myblog.service.ProjectTechService;
import com.zzl.myblog.service.TechService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectTechService projectTechService;
    private final TechService techService;

    /**
     * 获取所有项目（简单版，不含技术标签）
     * GET /api/projects/simple
     */
    @GetMapping("/simple")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjectsSimple() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 获取所有项目（含技术标签）
     * GET /api/projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjectsWithTechs() {
        List<Project> projects = projectService.getAllProjectsWithTechs();
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 根据ID获取项目详情（不含技术标签）
     * GET /api/projects/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        Optional<Project> projectOpt = projectService.getProjectById(id);
        if (projectOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProjectResponseDTO.fromEntity(projectOpt.get()));
    }

    /**
     * 获取指定项目的所有技术标签
     * GET /api/projects/{projectId}/techs
     */
    @GetMapping("/{projectId}/techs")
    public ResponseEntity<List<TechResponseDTO>> getTechsByProjectId(@PathVariable Long projectId) {
        List<Tech> techs = techService.getTechsByProjectId(projectId);
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 根据分类获取项目列表
     * GET /api/projects/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByCategory(@PathVariable String category) {
        List<Project> projects = projectService.getProjectsByCategory(category);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 根据标题模糊查询项目
     * GET /api/projects/title?title=xxx
     */
    @GetMapping("/title")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByTitle(@RequestParam String title) {
        List<Project> projects = projectService.getProjectsByTitle(title);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 关键字搜索项目（标题或描述）
     * GET /api/projects/search?keyword=xxx
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponseDTO>> searchProjects(@RequestParam String keyword) {
        List<Project> projects = projectService.searchProjects(keyword);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 根据技术名称查询包含该技术的所有项目
     * GET /api/projects/tech/{techName}
     */
    @GetMapping("/tech/{techName}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByTechName(@PathVariable String techName) {
        List<Project> projects = projectService.getProjectsByTechName(techName);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 高级搜索
     * GET /api/projects/advanced-search?title=xxx&category=xxx&startDate=2025-01-01&endDate=2025-12-31&techName=React
     */
    @GetMapping("/advanced-search")
    public ResponseEntity<List<ProjectResponseDTO>> advancedSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String techName) {

        List<Project> projects = projectService.advancedSearch(title, category, startDate, endDate, techName);
        List<ProjectResponseDTO> responseDTOs = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 创建项目
     * POST /api/projects
     */
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        // 1. 将 DTO 转换为 Entity
        Project project = convertToEntity(projectDTO);

        // 2. 创建项目
        Project createdProject = projectService.createProject(project);

        // 3. 添加技术标签关联（如果有）
        if (projectDTO.getTechs() != null && !projectDTO.getTechs().isEmpty()) {
            projectTechService.addTechsToProjectByName(createdProject.getId(), projectDTO.getTechs());
        }

        // 4. 返回创建后的项目（含技术标签）
        Optional<Project> result = projectService.getProjectById(createdProject.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponseDTO.fromEntity(result.get()));
    }

    /**
     * 更新项目
     * PUT /api/projects/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id,
                                                            @Valid @RequestBody ProjectDTO projectDTO) {
        // 1. 检查项目是否存在
        Optional<Project> existingOpt = projectService.getProjectById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 2. 更新项目基本信息
        Project project = existingOpt.get();
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setCategory(projectDTO.getCategory());
        project.setImageUrl(projectDTO.getImageUrl());
        project.setDemoUrl(projectDTO.getDemoUrl());
        project.setGithubUrl(projectDTO.getGithubUrl());
        project.setDate(LocalDate.parse(projectDTO.getDate()));

        Project updatedProject = projectService.updateProject(project);

        // 3. 更新技术标签关联（替换）
        if (projectDTO.getTechs() != null) {
            projectTechService.updateProjectTechs(id, projectDTO.getTechs());
        }

        // 4. 返回更新后的项目
        Optional<Project> result = projectService.getProjectById(id);
        return ResponseEntity.ok(ProjectResponseDTO.fromEntity(result.get()));
    }

    /**
     * 删除项目
     * DELETE /api/projects/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Optional<Project> existingOpt = projectService.getProjectById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 为项目添加技术标签
     * POST /api/projects/{projectId}/techs
     */
    @PostMapping("/{projectId}/techs")
    public ResponseEntity<Void> addTechsToProject(@PathVariable Long projectId,
                                                  @RequestBody List<String> techNames) {
        Optional<Project> existingOpt = projectService.getProjectById(projectId);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        projectTechService.addTechsToProjectByName(projectId, techNames);
        return ResponseEntity.ok().build();
    }

    /**
     * 移除项目的技术标签
     * DELETE /api/projects/{projectId}/techs
     */
    @DeleteMapping("/{projectId}/techs")
    public ResponseEntity<Void> removeTechsFromProject(@PathVariable Long projectId,
                                                       @RequestBody List<String> techNames) {
        Optional<Project> existingOpt = projectService.getProjectById(projectId);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        projectTechService.removeTechsFromProject(projectId, techNames);
        return ResponseEntity.noContent().build();
    }

    /**
     * 更新项目的技术标签（替换）
     * PUT /api/projects/{projectId}/techs
     */
    @PutMapping("/{projectId}/techs")
    public ResponseEntity<Void> updateProjectTechs(@PathVariable Long projectId,
                                                   @RequestBody List<String> techNames) {
        Optional<Project> existingOpt = projectService.getProjectById(projectId);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        projectTechService.updateProjectTechs(projectId, techNames);
        return ResponseEntity.ok().build();
    }

    /**
     * 辅助方法：将 DTO 转换为 Entity
     */
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