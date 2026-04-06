package com.zzl.myblog.service.impl;

import com.zzl.myblog.entity.ProjectDesign;
import com.zzl.myblog.mapper.ProjectDesignMapper;
import com.zzl.myblog.service.ProjectDesignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProjectDesignServiceImpl implements ProjectDesignService {

    private final ProjectDesignMapper projectDesignMapper;

    /**
     * 查询某个项目的所有设计思路
     */
    @Override
    public List<ProjectDesign> getByProjectId(Long projectId) {
        if (projectId == null || projectId <= 0) {
            log.error("项目ID非法：{}", projectId);
            throw new IllegalArgumentException("项目ID不能为空或小于0");
        }
        return projectDesignMapper.findByProjectId(projectId);
    }

    /**
     * 为项目添加一条设计思路
     */
    @Override
    @Transactional
    public void addProjectDesign(ProjectDesign design) {
        if (design == null) {
            throw new IllegalArgumentException("设计思路不能为空");
        }
        if (design.getProjectId() == null || design.getProjectId() <= 0) {
            throw new IllegalArgumentException("项目ID不能为空");
        }
        if (design.getTitle() == null || design.getTitle().isBlank()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (design.getContent() == null || design.getContent().isBlank()) {
            throw new IllegalArgumentException("内容不能为空");
        }

        projectDesignMapper.insertDesign(design);
        log.info("添加项目设计思路成功，项目ID：{}", design.getProjectId());
    }

    /**
     * 删除单条设计思路（按id）
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID不能为空");
        }

        int rows = projectDesignMapper.deleteById(id);
        if (rows == 0) {
            throw new RuntimeException("删除失败，数据不存在");
        }
        log.info("删除单条设计思路成功，ID：{}", id);
    }

    /**
     * 删除某个项目的全部设计思路
     */
    @Override
    @Transactional
    public void deleteByProjectId(Long projectId) {
        if (projectId == null || projectId <= 0) {
            throw new IllegalArgumentException("项目ID不能为空");
        }

        int rows = projectDesignMapper.deleteByProjectId(projectId);
        log.info("删除项目【{}】全部设计思路，共{}条", projectId, rows);
    }

    /**
     * 更新项目设计思路：
     * 先删除全部旧数据 → 再批量插入新数据
     */
    @Override
    @Transactional
    public void updateProjectDesign(Long projectId, List<ProjectDesign> designList) {
        if (projectId == null || projectId <= 0) {
            throw new IllegalArgumentException("项目ID不能为空");
        }
        if (designList == null || designList.isEmpty()) {
            throw new IllegalArgumentException("设计思路列表不能为空");
        }

        try {
            // 1. 删除旧数据
            deleteByProjectId(projectId);

            // 2. 批量插入新数据
            for (ProjectDesign design : designList) {
                design.setProjectId(projectId);
                addProjectDesign(design);
            }

            log.info("项目【{}】更新设计思路成功，共{}条", projectId, designList.size());

        } catch (Exception e) {
            log.error("项目【{}】更新设计思路失败", projectId, e);
            throw new RuntimeException("更新项目设计思路失败：" + e.getMessage());
        }
    }
}