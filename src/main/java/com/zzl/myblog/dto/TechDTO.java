package com.zzl.myblog.dto;

import lombok.Data;

/**
 * 技术标签数据传输对象
 * 用于接收前端传来的技术数据
 */
@Data
public class TechDTO {


    private String name;//名称

    private String category;//类型

    private String icon;//图标
}