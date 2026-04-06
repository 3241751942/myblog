package com.zzl.myblog.service;

import com.zzl.myblog.entity.Tech;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 技术标签服务接口
 */
public interface TechService {


    //创建技术标签
    //param tech 技术标签信息
    //return 创建后的技术标签
    Tech createTech(Tech tech);

    //根据ID查询技术标签
    //param id 技术ID
    //return 技术标签信息
    Optional<Tech> getTechById(Long id);

    // 根据名称查询技术标签
    // param name 技术名称
    // return 技术标签信息
    Optional<Tech> getTechByName(String name);

     //查询所有技术标签
     //return 技术标签列表
    List<Tech> getAllTechs();


    //按分类查询技术标签
    //param category 分类
    //return 技术标签列表
    List<Tech> getTechsByCategory(String category);


    //获取热门技术标签
    //param limit 数量限制
    //return 热门技术标签列表
    List<Tech> getHotTechs(int limit);

    //批量创建技术标签（如果不存在）
    //@param techNames 技术名称列表
    //return 创建或已存在的技术标签列表
    List<Tech> createTechsIfNotExist(List<String> techNames);


    //通过项目id找出它所有的标签
    List<Tech> getTechsByProjectId(Long projectId);
}