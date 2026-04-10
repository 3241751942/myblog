package com.zzl.myblog.controller.api;

import com.zzl.myblog.dto.ApiResponseDTO;
import com.zzl.myblog.dto.TechDTO;
import com.zzl.myblog.dto.TechResponseDTO;
import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.exception.ResourceNotFoundException;
import com.zzl.myblog.service.TechService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/techs")
@RequiredArgsConstructor
@Validated
public class TechController {

    private final TechService techService;

    @GetMapping
    public ApiResponseDTO<List<TechResponseDTO>> getAllTechs() {
        List<Tech> techs = techService.getAllTechs();
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/{id}")
    public ApiResponseDTO<TechResponseDTO> getTechById(
            @PathVariable @NotNull(message="标签id不能为空") Long id) {
        Tech tech = techService.getTechById(id)
                .orElseThrow(() -> new ResourceNotFoundException("技术标签不存在"));
        return ApiResponseDTO.success(TechResponseDTO.fromEntity(tech));
    }

    @GetMapping("/project/{projectId}")
    public ApiResponseDTO<List<TechResponseDTO>> getAllTechsByProjectId(
            @PathVariable @NotNull(message = "项目id不能为空") Long projectId) {
        List<Tech> techs = techService.getTechsByProjectId(projectId);
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/category/{category}")
    public ApiResponseDTO<List<TechResponseDTO>> getTechsByCategory(
            @PathVariable @NotBlank(message = "标签类型不能为空") String category) {
        List<Tech> techs = techService.getTechsByCategory(category);
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }

    @GetMapping("/name/{name}")
    public ApiResponseDTO<TechResponseDTO> getTechByName(
            @PathVariable @NotBlank(message = "标签名称不能为空") String name) {
        Tech tech = techService.getTechByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在"));
        return ApiResponseDTO.success(TechResponseDTO.fromEntity(tech));
    }

    @PostMapping
    public ApiResponseDTO<TechResponseDTO> createTech(@Valid @RequestBody TechDTO techDTO) {
        Tech tech = new Tech();
        tech.setName(techDTO.getName());
        tech.setCategory(techDTO.getCategory());
        tech.setIcon(techDTO.getIcon());
        Tech createdTech = techService.createTech(tech);
        return ApiResponseDTO.success(TechResponseDTO.fromEntity(createdTech));
    }

    @PostMapping("/batch")
    public ApiResponseDTO<List<TechResponseDTO>> createTechsIfNotExist(
            @RequestBody @NotEmpty(message = "技术名称列表不能为空")
            List<@NotBlank(message = "技术名称不能为空") String> techNames) {
        List<Tech> techs = techService.createTechsIfNotExist(techNames);
        List<TechResponseDTO> responseDTOs = techs.stream()
                .map(TechResponseDTO::fromEntity)
                .toList();
        return ApiResponseDTO.success(responseDTOs);
    }
}