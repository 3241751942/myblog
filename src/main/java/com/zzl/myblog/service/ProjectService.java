package com.zzl.myblog.service;

import com.zzl.myblog.entity.Project;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ProjectService {

    //创建项目
    //param project 项目信息
    //return 创建后的项目（包含自动生成的ID）
    Project createProject(Project project);


     //更新项目
     //param project 项目信息
     //return 更新后的项目
    Project updateProject(Project project);


    //根据ID删除项目
    //param id 项目ID
    void deleteProject(Long id);


    //根据ID查询项目
    // param id 项目ID
    // return 项目信息
    Optional<Project> getProjectById(Long id);


    //查询所有项目
    // return 项目列表

    List<Project> getAllProjects();


    //根据分类查询项目
    // param category 分类
    // return 项目列表
    List<Project> getProjectsByCategory(String category);

    //根据标题模糊查询项目
     //param title 标题关键字
     //return 项目列表
    List<Project> getProjectsByTitle(String title);

     //关键字搜索项目（标题或描述）
     //param keyword 关键字
     //return 项目列表
    List<Project> searchProjects(String keyword);


    //查询项目及其技术标签
    //return 项目（包含技术标签）
    Project getProjectWithTechs(Long ProjectId);



     //查询所有项目及其技术标签
     //return 项目列表（包含技术标签）
    List<Project> getAllProjectsWithTechs();


    //根据技术名称查询包含该技术的所有项目
    // param techName 技术名称
    // return 项目列表
    List<Project> getProjectsByTechName(String techName);

    //高级搜索
    // param title 标题（可选）
    // param category 分类（可选）
    // param startDate 开始日期（可选）
    // param endDate 结束日期（可选）
    // param techName 技术名称（可选）
    // return 项目列表
    List<Project> advancedSearch(String title, String category,
                                 LocalDate startDate, LocalDate endDate,
                                 String techName);
}
