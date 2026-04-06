package com.zzl.myblog.service;

import com.zzl.myblog.entity.ProjectDesign;
import java.util.List;

public interface ProjectDesignService {
    //查询到某个项目的设计思路
    List<ProjectDesign> getByProjectId(Long projectId);

    //为项目添加设计思路
    void addProjectDesign(ProjectDesign design);

    //删除某一条设计思路
    //通过其单独的id
    void deleteById(Long id);

    //删除某个项目的全部设计思路
    void deleteByProjectId(Long projectId);

    //更新
    void updateProjectDesign(Long projectId, List<ProjectDesign> designList);
}