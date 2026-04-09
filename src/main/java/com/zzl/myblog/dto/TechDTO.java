package com.zzl.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 技术标签数据传输对象
 * 用于接收前端传来的技术数据
 */
@Data
public class TechDTO {

    @NotBlank(message="技术标签名称不能为空")
    private String name;//名称

    private String category;//类型

    private String icon;//图标
}