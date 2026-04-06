package com.zzl.myblog.service.impl;

import com.zzl.myblog.entity.Project;
import com.zzl.myblog.entity.ProjectTechRelation;
import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.mapper.ProjectMapper;
import com.zzl.myblog.mapper.ProjectTechMapper;
import com.zzl.myblog.mapper.TechMapper;
import com.zzl.myblog.service.ProjectTechService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j                  //生成一个日志对象：log
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectTechServiceImpl implements ProjectTechService {

    private final ProjectTechMapper projectTechMapper;

    private final ProjectMapper projectMapper;

    private final TechMapper techMapper;

    @Override
    @Transactional
    public void addTechsToProject(Long projectId, List<Long> techIds) {
        // 1. 参数校验
        if (projectId == null || projectId <= 0) {
            log.error("添加技术标签失败：项目ID无效，projectId: {}", projectId);
            throw new IllegalArgumentException("项目ID无效");
        }

        if (CollectionUtils.isEmpty(techIds)) {
            log.warn("添加技术标签失败：技术ID列表为空，projectId: {}", projectId);
            throw new IllegalArgumentException("技术ID列表不能为空");
        }

        // 2. 检查项目是否存在
        Optional<Project> projectOpt = projectMapper.findById(projectId);
        if (projectOpt.isEmpty()) {
            log.error("添加技术标签失败：项目不存在，projectId: {}", projectId);
            throw new RuntimeException("项目不存在，ID: " + projectId);
        }

        // 3. 获取当前项目下一个排序值
        int sortOrder = getNextSortOrder(projectId);
        int startOrderId = sortOrder;
        // 4. 过滤并构建关联关系（从下一个排序值开始）
        List<ProjectTechRelation> relations = new ArrayList<>();

        for (Long techId : techIds) {
            if (techId != null && techId > 0) {
                // 检查技术是否存在
                Tech tech = techMapper.findById(techId);
                if (tech != null) {
                    // 检查是否已经关联（避免重复添加）
                    boolean alreadyExists = projectTechMapper.existsRelation(projectId, techId);

                    if (!alreadyExists) {
                        relations.add(new ProjectTechRelation(techId, sortOrder++));
                    } else {
                        log.warn("技术标签已关联，跳过：techId: {}", techId);
                    }
                } else {
                    log.warn("技术标签不存在，跳过：techId: {}", techId);
                }
            }
        }

        if (relations.isEmpty()) {
            log.warn("添加技术标签失败：没有有效的技术ID，projectId: {}", projectId);
            throw new IllegalArgumentException("没有有效的技术ID");
        }

        // 5. 批量插入关联关系
        try {
            int count = projectTechMapper.batchInsert(projectId, relations);
            log.info("成功为项目添加技术标签，projectId: {}, 添加数量: {}, 起始排序: {}",
                    projectId, count, startOrderId);
        } catch (Exception e) {
            log.error("添加技术标签失败，projectId: {}, techIds: {}", projectId, techIds, e);
            throw new RuntimeException("添加技术标签失败: " + e.getMessage(), e);
        }
    }


     //为项目添加技术标签（通过技术名称）

    @Override
    @Transactional
    public void addTechsToProjectByName(Long projectId, List<String> techNames) {
        // 1. 参数校验
        if (projectId == null || projectId <= 0) {
            log.error("添加技术标签失败：项目ID无效，projectId: {}", projectId);
            throw new IllegalArgumentException("项目ID无效");
        }

        if (CollectionUtils.isEmpty(techNames)) {
            log.warn("添加技术标签失败：技术名称列表为空，projectId: {}", projectId);
            throw new IllegalArgumentException("技术名称列表不能为空");
        }

        // 2. 检查项目是否存在
        Optional<Project> project = projectMapper.findById(projectId);
        if (project.isEmpty()) {
            log.error("添加技术标签失败：项目不存在，projectId: {}", projectId);
            throw new RuntimeException("项目不存在，ID: " + projectId);
        }

        // 3. 根据技术名称查询或创建技术标签
        List<Long> techIds = new ArrayList<>();
        for (String techName : techNames) {
            if (!StringUtils.hasText(techName)) {
                continue;  // 跳过空的技术名称
            }

            String trimmedName = techName.trim();

            // 查询技术是否存在
            Optional<Tech> tech = techMapper.findByName(trimmedName);

            if (tech.isPresent()) {
                // 技术已存在，直接使用
                techIds.add(tech.get().getId());
                log.debug("技术标签已存在: {}", trimmedName);
            } else {
                // 技术不存在，创建新技术
                try {
                    Tech newTech = new Tech();
                    newTech.setName(trimmedName);
                    techMapper.insert(newTech);
                    techIds.add(newTech.getId());
                    log.info("创建新技术标签: {}", trimmedName);
                } catch (Exception e) {
                    log.error("创建技术标签失败: {}", trimmedName, e);
                    throw new RuntimeException("创建技术标签失败: " + trimmedName, e);
                }
            }
        }

        if (techIds.isEmpty()) {
            log.warn("添加技术标签失败：没有有效的技术名称，projectId: {}", projectId);
            throw new IllegalArgumentException("没有有效的技术名称");
        }

        // 4. 批量插入关联关系
        int sortOrder = getNextSortOrder(projectId);
        List<ProjectTechRelation> relations = new ArrayList<>();//包装成ProjectTechRelation类
        for (Long techId : techIds) {
            relations.add(new ProjectTechRelation(techId, sortOrder));
            sortOrder++;
        }
        try {
            int count = projectTechMapper.batchInsert(projectId, relations);
            log.info("成功为项目添加技术标签（通过名称），projectId: {}, 添加数量: {}, 技术名称: {}",
                    projectId, count, techNames);
        } catch (Exception e) {
            log.error("添加技术标签失败（通过名称），projectId: {}, techNames: {}", projectId, techNames, e);
            throw new RuntimeException("添加技术标签失败: " + e.getMessage(), e);
        }
    }


    //移除项目的技术标签

    @Override
    @Transactional
    public void removeTechsFromProject(Long projectId, List<String> techNames) {
        // 1. 参数校验
        if (projectId == null) {
            log.error("移除技术标签失败：项目ID无效，projectId: {}", projectId);
            throw new IllegalArgumentException("项目ID无效");
        }

        if (CollectionUtils.isEmpty(techNames)) {
            log.warn("移除技术标签失败：技术名称列表为空，projectId: {}", projectId);
            throw new IllegalArgumentException("技术名称列表不能为空");
        }

        // 2. 检查项目是否存在
        Optional<Project> project = projectMapper.findById(projectId);
        if (project.isEmpty()) {
            log.error("移除技术标签失败：项目不存在，projectId: {}", projectId);
            throw new RuntimeException("项目不存在，ID: " + projectId);
        }

        // 3. 根据技术名称获取技术ID列表
        List<Long> techIds = new ArrayList<>();
        for (String techName : techNames) {
            if (!StringUtils.hasText(techName)) {
                continue; // 跳过空的技术名称
            }

            Optional<Tech> tech = techMapper.findByName(techName.trim());
            if (tech.isPresent()) {
                techIds.add(tech.get().getId());
            } else {
                log.warn("技术标签不存在，跳过移除：techName: {}", techName);
            }
        }

        if (techIds.isEmpty()) {
            log.warn("移除技术标签失败：没有找到有效的技术ID，projectId: {}, techNames: {}", projectId, techNames);
            throw new IllegalArgumentException("没有找到有效的技术标签");
        }

        // 4. 批量删除关联关系
        try {
            int count = projectTechMapper.batchDelete(projectId, techIds);
            log.info("成功移除项目的技术标签，projectId: {}, 移除数量: {}, 技术名称: {}",
                    projectId, count, techNames);
        } catch (Exception e) {
            log.error("移除技术标签失败，projectId: {}, techNames: {}", projectId, techNames, e);
            throw new RuntimeException("移除技术标签失败: " + e.getMessage(), e);
        }
    }


    //更新项目的技术标签（替换）

    @Override
    @Transactional
    public void updateProjectTechs(Long projectId, List<String> names) {
        // 1. 参数校验
        if (projectId == null || projectId <= 0) {
            log.error("更新技术标签失败：项目ID无效，projectId: {}", projectId);
            throw new IllegalArgumentException("项目ID无效");
        }

        // 2. 检查项目是否存在
        Optional<Project> project = projectMapper.findById(projectId);
        if (project.isEmpty()) {
            log.error("更新技术标签失败：项目不存在，projectId: {}", projectId);
            throw new RuntimeException("项目不存在，ID: " + projectId);
        }

        // 3. 如果技术名字列表为空，则删除所有关联
        if (CollectionUtils.isEmpty(names)) {
            projectTechMapper.deleteByProjectId(projectId);
            log.info("清空项目的所有技术标签，projectId: {}", projectId);
            return;
        }

        // 4. 过滤掉无效的技术名称
        List<String> validTechNames = names.stream()
                .filter(StringUtils::hasText)  // 过滤 null、空字符串、纯空格
                .map(String::trim)              // 去除前后空格
                .distinct()                     // 去重
                .toList();

        if (validTechNames.isEmpty()) {
            log.warn("更新技术标签失败：没有有效的技术名称，projectId: {}", projectId);
            throw new IllegalArgumentException("没有有效的技术名称");
        }

        // 5. 验证所有技术ID都存在,不存在的创建
        for (String techName : validTechNames) {
            Optional<Tech> tech = techMapper.findByName(techName);
            if (tech.isEmpty()) {
                techMapper.insert(new Tech(techName,null,null));
            }
        }

        // 6. 先删除所有旧关联，再插入新关联（替换）
        try {
            // 开启事务，确保原子性
            projectTechMapper.deleteByProjectId(projectId);
            List<ProjectTechRelation> relations = new ArrayList<>();//包装

            int sortOrder = getNextSortOrder(projectId);
            for (String techName : validTechNames) {
                Optional<Tech> tech = techMapper.findByName(techName);
                if(tech.isEmpty()) {
                    throw new IllegalArgumentException("更新失败");
                }
                relations.add(new ProjectTechRelation(tech.get().getId(),sortOrder));
            }

            int count = projectTechMapper.batchInsert(projectId, relations);
            log.info("成功更新项目的技术标签，projectId: {}, 新标签数量: {}, 技术名称: {}",
                    projectId, count, validTechNames);
        } catch (Exception e) {
            log.error("更新技术标签失败，projectId: {}, techIds: {}", projectId, validTechNames, e);
            throw new RuntimeException("更新技术标签失败: " + e.getMessage(), e);
        }
    }


    //获取下一个排序值
    private int getNextSortOrder(Long projectId) {
        Integer maxSortOrder = projectTechMapper.getMaxSortOrderByProjectId(projectId);
        int currentMaxSortOrder = (maxSortOrder != null) ? maxSortOrder : -1;
        log.debug("项目当前最大排序值: {}, 下一个排序值: {}", currentMaxSortOrder, currentMaxSortOrder + 1);
        return currentMaxSortOrder + 1;
    }

}