package com.zzl.myblog.controller.api;

import com.zzl.myblog.dto.ApiResponseDTO;
import com.zzl.myblog.dto.ProjectDesignDTO;
import com.zzl.myblog.dto.ProjectDesignResponseDTO;
import com.zzl.myblog.entity.ProjectDesign;
import com.zzl.myblog.service.ProjectDesignService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/design")
@RequiredArgsConstructor
@Validated
public class ProjectDesignController {

    private final ProjectDesignService projectDesignService;

    @GetMapping("/{projectId}")
    public ApiResponseDTO<List<ProjectDesignResponseDTO>> getByProjectId(
            @NotNull(message = "项目不能为空") @PathVariable Long projectId
    ) {
        List<ProjectDesign> list = projectDesignService.getByProjectId(projectId);
        List<ProjectDesignResponseDTO> result = list.stream()
                .map(ProjectDesignResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(result);
    }

    @PostMapping
    public ApiResponseDTO<Void> add(@Valid @RequestBody ProjectDesignDTO dto) {
        ProjectDesign design = new ProjectDesign();
        design.setProjectId(dto.getProjectId());
        design.setTitle(dto.getTitle());
        design.setContent(dto.getContent());
        design.setSort(dto.getSort());
        projectDesignService.addProjectDesign(design);
        return ApiResponseDTO.success();
    }

    @DeleteMapping("/item/{id}")
    public ApiResponseDTO<Void> deleteById(
            @NotNull(message = "设计思路id不能为空") @PathVariable Long id) {
        projectDesignService.deleteById(id);
        return ApiResponseDTO.success();
    }

    @DeleteMapping("/project/{projectId}")
    public ApiResponseDTO<Void> deleteByProjectId(
            @NotNull(message = "项目id不能为空") @PathVariable Long projectId) {
        projectDesignService.deleteByProjectId(projectId);
        return ApiResponseDTO.success();
    }

    @PutMapping("/{projectId}")
    public ApiResponseDTO<Void> updateProjectDesign(
            @PathVariable @NotNull(message="项目id不能空") Long projectId,
            @RequestBody List<ProjectDesignDTO> dtoList
    ) {
        List<ProjectDesign> designList = dtoList.stream().map(dto -> {
            ProjectDesign design = new ProjectDesign();
            design.setId(dto.getId());
            design.setTitle(dto.getTitle());
            design.setContent(dto.getContent());
            design.setSort(dto.getSort());
            return design;
        }).toList();
        projectDesignService.updateProjectDesign(projectId, designList);
        return ApiResponseDTO.success();
    }
}