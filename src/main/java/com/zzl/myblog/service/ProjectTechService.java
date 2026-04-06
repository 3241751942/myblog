package com.zzl.myblog.service;

import java.util.List;

/**
 * 项目-技术关联服务接口
 */
public interface ProjectTechService {

    /**
     * 为项目添加技术标签
     * @param projectId 项目ID
     * @param techIds 技术ID列表
     */
    void addTechsToProject(Long projectId, List<Long> techIds);

    /**
     * 为项目添加技术标签（通过技术名称）
     * @param projectId 项目ID
     * @param techNames 技术名称列表
     */
    void addTechsToProjectByName(Long projectId, List<String> techNames);

    /**
     * 移除项目的技术标签
     * @param projectId 项目ID
     * @param techNames 技术名称列表
     */
    void removeTechsFromProject(Long projectId, List<String> techNames);

    /**
     * 更新项目的技术标签（替换）
     * @param projectId 项目ID
     * @param names 新的技术名字
     */
    void updateProjectTechs(Long projectId, List<String> names);



}