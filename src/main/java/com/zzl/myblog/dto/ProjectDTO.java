package com.zzl.myblog.dto;


import com.zzl.myblog.entity.Tech;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String category;//分类
    private String imageUrl;//存放图片路径。不采用2进制的方式存进数据库
    private String demoUrl;
    private String githubUrl;
    private String date;//发布日期

    private List<String> techs;//标签，一对多
}
