package com.zzl.myblog.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
public class Project {
    private Long id;
    private String title;
    private String description;
    private String category;//分类
    private String imageUrl;//存放图片路径。不采用2进制的方式存进数据库
    private String demoUrl;
    private String githubUrl;
    private String createdAt;
    private LocalDate date;

    private List<Tech> techs;//标签，一对多

}
