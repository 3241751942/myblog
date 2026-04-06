package com.zzl.myblog.service.impl;

import com.zzl.myblog.entity.Project;
import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.mapper.ProjectMapper;
import com.zzl.myblog.mapper.ProjectTechMapper;
import com.zzl.myblog.mapper.TechMapper;
import com.zzl.myblog.service.TechService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 技术标签服务实现类
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechServiceImpl implements TechService {

    private final TechMapper techMapper;
    private final ProjectTechMapper projectTechMapper;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public Tech createTech(Tech tech) {
        if (tech == null) {
            throw new IllegalArgumentException("技术标签信息不能为空");
        }
        if (!StringUtils.hasText(tech.getName())) {
            throw new IllegalArgumentException("技术名称不能为空");
        }

        // 检查是否已存在同名技术
        Optional<Tech> existing = getTechByName(tech.getName());
        if (existing.isPresent()) {
            throw new RuntimeException("技术标签已存在: " + tech.getName());
        }

        techMapper.insert(tech);
        return tech;
    }

    @Override
    public Optional<Tech> getTechById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(techMapper.findById(id));
    }

    @Override
    public Optional<Tech> getTechByName(String name) {
        if (!StringUtils.hasText(name)) {
            return Optional.empty();
        }
        return techMapper.findByName(name);
    }

    @Override
    public List<Tech> getAllTechs() {
        return techMapper.findAll();
    }

    @Override
    public List<Tech> getTechsByCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return getAllTechs();
        }
        return techMapper.findByCategory(category);
    }


    @Override
    public List<Tech> getHotTechs(int limit) {
        if (limit <= 0) {
            limit = 10; // 默认返回10个
        }
        return techMapper.findHotTechs(limit);
    }

    @Override
    @Transactional
    public List<Tech> createTechsIfNotExist(List<String> techNames) {
        if (techNames == null || techNames.isEmpty()) {
            return new ArrayList<>();
        }

        // 去重并过滤空值
        List<String> distinctTechNames = techNames.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .toList();

        List<Tech> result = new ArrayList<>();

        for (String name : distinctTechNames) {
            Optional<Tech> existing = getTechByName(name);
            if (existing.isPresent()) {
                result.add(existing.get());
            } else {
                Tech newTech = new Tech();
                newTech.setName(name);
                // 可以设置默认分类，或者根据业务逻辑自动分类
                // newTech.setCategory("OTHER");
                createTech(newTech);
                result.add(newTech);
            }
        }
        return result;
    }

    @Override
    public List<Tech> getTechsByProjectId(Long projectId) {

        if (projectId == null) {
            throw new IllegalArgumentException("项目id不能为空");
        }

        Optional<Project> project=projectMapper.findById(projectId);
        if(project.isEmpty()){
            throw new IllegalArgumentException("项目不存在");
        }

        List<Tech> techs = new ArrayList<>();
        List<Long> techIds=projectTechMapper.findTechIdsByProjectId(projectId);
        for (Long techId : techIds) {
            Tech tech = techMapper.findById(techId);
            techs.add(tech);
        }
        return techs;
    }

}