package com.zzl.myblog.mapper;

import com.zzl.myblog.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Mapper
public interface ProjectMapper {

    int insert(Project project);//插入项目

    Optional<Project> findById(Long id);

    List<Project> findAll();

    List<Project> findByCategory(String category);

    List<Project> findByName(String title);

    List<Project> search(String keyword);//关键字搜索项目

    int update(Project project);//更新项目

    int deleteById(Long id);//删除项目

    List<Project> findAllWithTechs();//查询项目及其技术标签

    List<Project> findByTechName(String techName);//查询包含该标签的项目

    List<Project> advancedSearch(
            @Param("title") String title,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("techName") String techName
    );
}
