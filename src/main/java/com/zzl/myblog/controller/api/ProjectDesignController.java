package com.zzl.myblog.controller.api;

import com.zzl.myblog.dto.ProjectDesignDTO;
import com.zzl.myblog.dto.ProjectDesignResponseDTO;
import com.zzl.myblog.entity.ProjectDesign;
import com.zzl.myblog.service.ProjectDesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects/design")
@RequiredArgsConstructor
public class ProjectDesignController {

    private final ProjectDesignService projectDesignService;

    /**
     * 查询某个项目的 全部设计思路
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<List<ProjectDesignResponseDTO>> getByProjectId(
            @PathVariable Long projectId
    ) {
        List<ProjectDesign> list = projectDesignService.getByProjectId(projectId);
        List<ProjectDesignResponseDTO> result = list.stream()
                .map(ProjectDesignResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * 单条 添加设计思路
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody ProjectDesignDTO dto) {
        ProjectDesign design = new ProjectDesign();
        design.setId(dto.getId());
        design.setProjectId(dto.getProjectId());
        design.setTitle(dto.getTitle());
        design.setContent(dto.getContent());
        design.setSort(dto.getSort());

        projectDesignService.addProjectDesign(design);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除单条设计思路（根据 id）
     */
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        projectDesignService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除某个项目的 全部设计思路
     */
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<Void> deleteByProjectId(@PathVariable Long projectId) {
        projectDesignService.deleteByProjectId(projectId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 【核心】更新项目设计思路
     * 先删全部 → 再批量插入
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProjectDesign(
            @PathVariable Long projectId,
            @RequestBody List<ProjectDesignDTO> dtoList
    ) {
        List<ProjectDesign> designList = dtoList.stream().map(dto -> {
            ProjectDesign design = new ProjectDesign();
            design.setId(dto.getId());
            design.setTitle(dto.getTitle());
            design.setContent(dto.getContent());
            design.setSort(dto.getSort());
            return design;
        }).collect(Collectors.toList());

        projectDesignService.updateProjectDesign(projectId, designList);
        return ResponseEntity.ok().build();
    }
}