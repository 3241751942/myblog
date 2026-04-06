package com.zzl.myblog.mapper;

import com.zzl.myblog.entity.ProjectDesign;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProjectDesignMapper {
    List<ProjectDesign> findByProjectId(@Param("projectId") Long projectId);

    int insertDesign(ProjectDesign design);

    int deleteById(Long id);

    //删除该项目的全部设计思路
    int deleteByProjectId(Long projectId);
}
