package com.zzl.myblog.service.impl;

import com.zzl.myblog.entity.Project;
import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.mapper.ProjectMapper;
import com.zzl.myblog.mapper.ProjectTechMapper;
import com.zzl.myblog.service.ProjectService;
import com.zzl.myblog.service.TechService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final TechService techService;

    @Override
    @Transactional
    public Project createProject(Project project) {
        if(project==null){
            throw new IllegalArgumentException("项目信息不能为空");
        }
        if(!StringUtils.hasText(project.getTitle())){
            throw new IllegalArgumentException("项目标题不能为空");
        }
        if (project.getDate()==null) {
            project.setDate(LocalDate.now());
        }
        projectMapper.insert(project);
        return project;
    }

    @Override
    @Transactional
    public Project updateProject(Project project) {
        if(project==null||project.getId()==null){
            throw new IllegalArgumentException("项目信息和id不能为空");
        }
        Optional<Project> existing = projectMapper.findById(project.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("项目不存在，ID: " + project.getId());
        }
        projectMapper.update(project);
        return project;
    }

    @Override
    public void deleteProject(Long id) {
        if(id == null){
            throw new IllegalArgumentException("项目id不能为空");
        }
        projectMapper.deleteById(id);
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        if(id == null){
            return Optional.empty();
        }
        return projectMapper.findById(id);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectMapper.findAll();
    }

    @Override
    public List<Project> getProjectsByCategory(String category) {
        if(!StringUtils.hasText(category)){
            return projectMapper.findAll();
        }
        return projectMapper.findByCategory(category);
    }

    @Override
    public List<Project> getProjectsByTitle(String title) {
        if(!StringUtils.hasText(title)){
            return projectMapper.findAll();
        }
        return projectMapper.findByName(title);
    }

    @Override
    public List<Project> searchProjects(String keyword) {
        if(!StringUtils.hasText(keyword)){
            return projectMapper.findAll();
        }
        return projectMapper.search(keyword);
    }

    @Override
    public Project getProjectWithTechs(Long ProjectId) {
        var techIds = techService.getTechsByProjectId(ProjectId);
        List<Tech> techs=new ArrayList<>();
        for(Tech tech:techIds){
            if(tech==null){
                throw new IllegalArgumentException("标签为空");
            }
            techs.add(tech);
        }
        Optional<Project> project=getProjectById(ProjectId);
        if(project.isEmpty()){
            throw new IllegalArgumentException("项目不存在");
        }
        project.get().setTechs(techs);
        return project.get();
    }

    @Override
    public List<Project> getAllProjectsWithTechs() {
        return projectMapper.findAllWithTechs();
    }

    @Override
    public List<Project> getProjectsByTechName(String techName) {
        if(!StringUtils.hasText(techName)){
            return projectMapper.findAll();
        }
        return projectMapper.findByTechName(techName);
    }

    @Override
    public List<Project> advancedSearch(String title, String category, LocalDate startDate, LocalDate endDate, String techName) {
        if (!StringUtils.hasText(title) && !StringUtils.hasText(category)
                && startDate == null && endDate == null && !StringUtils.hasText(techName)) {
            return projectMapper.findAllWithTechs();
        }
        return projectMapper.advancedSearch(title,category,startDate,endDate,techName);
    }
}
