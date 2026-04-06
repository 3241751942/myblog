package com.zzl.myblog.controller.api;

import com.zzl.myblog.dto.TechDTO;
import com.zzl.myblog.dto.TechResponseDTO;
import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.service.TechService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/techs")
@RequiredArgsConstructor
public class TechController {

    private final TechService techService;

    /**
     * 获取所有技术标签
     * GET /api/techs
     */
    @GetMapping
    public ResponseEntity<List<TechResponseDTO>> getAllTechs() {
        List<Tech> techs = techService.getAllTechs();
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 根据ID获取单个技术标签
     * GET /api/techs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechResponseDTO> getTechById(@PathVariable Long id) {
        Optional<Tech> techOpt = techService.getTechById(id);
        if (techOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(TechResponseDTO.fromEntity(techOpt.get()));
    }

    /**
     * 根据项目ID获取该项目关联的所有技术标签
     * GET /api/techs/project/{projectId}
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TechResponseDTO>> getAllTechsByProjectId(@PathVariable Long projectId) {
        List<Tech> techs = techService.getTechsByProjectId(projectId);
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * 根据分类获取技术标签
     * GET /api/techs/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TechResponseDTO>> getTechsByCategory(@PathVariable String category) {
        List<Tech> techs = techService.getTechsByCategory(category);
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }


    /**
     * 根据名称获取技术标签
     * GET /api/techs/name/{name}
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<TechResponseDTO> getTechByName(@PathVariable String name) {
        Optional<Tech> techOpt = techService.getTechByName(name);
        if (techOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(TechResponseDTO.fromEntity(techOpt.get()));
    }

    /**
     * 创建新技术标签
     * POST /api/techs
     */
    @PostMapping
    public ResponseEntity<TechResponseDTO> createTech(@Valid @RequestBody TechDTO techDTO) {
        // 将 DTO 转换为 Entity
        Tech tech = new Tech();
        tech.setName(techDTO.getName());
        tech.setCategory(techDTO.getCategory());
        tech.setIcon(techDTO.getIcon());

        Tech createdTech = techService.createTech(tech);

        return ResponseEntity.status(HttpStatus.CREATED).body(TechResponseDTO.fromEntity(createdTech));
    }


    /**
     * 批量创建或获取技术标签（用于项目创建时）
     * POST /api/techs/batch
     */
    @PostMapping("/batch")
    public ResponseEntity<List<TechResponseDTO>> createTechsIfNotExist(@RequestBody List<String> techNames) {
        List<Tech> techs = techService.createTechsIfNotExist(techNames);
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
}