package com.zzl.myblog.dto;

import com.zzl.myblog.entity.Tech;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 技术标签响应数据传输对象
 * 用于返回给前端的技术数据
 */
@Data
public class TechResponseDTO {

    @NotNull(message = "技术标签id不能为空")
    private Long id;
    @NotBlank(message="技术标签名称不能为空")
    private String name;

    private String category;
    private String icon;
    private LocalDateTime createdAt;

    /**
     * 从 Tech 实体转换为 TechResponseDTO
     */
    public static TechResponseDTO fromEntity(Tech tech) {
        if (tech == null) {
            return null;
        }
        TechResponseDTO dto = new TechResponseDTO();
        dto.setId(tech.getId());
        dto.setName(tech.getName());
        dto.setCategory(tech.getCategory());
        dto.setIcon(tech.getIcon());
        dto.setCreatedAt(tech.getCreatedAt());
        return dto;
    }

    /**
     * 从 Tech 实体列表转换为 TechResponseDTO 列表
     */
    public static List<TechResponseDTO> fromEntityList(List<Tech> techs) {
        if (techs == null) {
            return null;
        }
        return techs.stream()
                .map(TechResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}