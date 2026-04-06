package com.zzl.myblog.mapper;

import com.zzl.myblog.entity.ProjectTechRelation;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProjectTechMapper {

    // 插入项目-技术关联
    int insert(Long projectId,Long techId,Integer sortOrder);

    // 批量插入
    int batchInsert(Long projectId,List<ProjectTechRelation> relations);

    // 删除项目的所有技术关联
    int deleteByProjectId(Long projectId);

    // 删除某个技术的所有关联
    int deleteByTechId(Long techId);

    // 获取项目的技术ID列表
    List<Long> findTechIdsByProjectId(Long projectId);

    //获取目前该项目所含有标签数目
    Integer getMaxSortOrderByProjectId(Long projectId);

    //检查该项目与该标签的光联是否已经存在了
    boolean existsRelation(Long projectId, Long techId);

    //批量删除该项目与该标签的关联
    int batchDelete(@Param("projectId")Long projectId,@Param("techIds")List<Long> techIds);
}
