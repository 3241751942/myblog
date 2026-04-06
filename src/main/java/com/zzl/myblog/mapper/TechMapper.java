package com.zzl.myblog.mapper;

import com.zzl.myblog.entity.Tech;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TechMapper {
    int insert(Tech tech);//插入标签,返回的行数

    Optional<Tech> findByName(String name);//看不懂Option是干嘛的

    Tech findById(Long id);

    List<Tech> findAll();

    List<Tech> findByCategory(String category);

    List<Object[]> getTechUsageStats();// 统计技术使用次数

    List<Tech> findHotTechs(int limit); // 获取热门技术（使用次数最多的前N个）,这两没啥用
}
