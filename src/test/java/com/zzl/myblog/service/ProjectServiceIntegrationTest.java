package com.zzl.myblog.service;

import com.zzl.myblog.entity.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//集成测试

@SpringBootTest // 启动整个 Spring 容器
@Transactional  // 测试完自动回滚，不脏数据库
public class ProjectServiceIntegrationTest {

    // 直接注入真正的 Service
    @Autowired
    private ProjectService projectService;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setTitle("集成测试项目");
        testProject.setDescription("这是集成测试生成的描述");
        testProject.setCategory("后端");
        testProject.setDate(LocalDate.now());
    }

    // ====================== 新增、查询 ======================
    @Test
    void testCreateAndGetById() {
        // 1. 创建
        Project saved = projectService.createProject(testProject);
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());

        // 2. 查询
        Optional<Project> found = projectService.getProjectById(saved.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("集成测试项目", found.get().getTitle());
    }

    // ====================== 修改 ======================
    @Test
    void testUpdate() {
        Project saved = projectService.createProject(testProject);
        Long id = saved.getId();

        saved.setTitle("修改后的标题");
        projectService.updateProject(saved);

        Optional<Project> updated = projectService.getProjectById(id);
        Assertions.assertEquals("修改后的标题", updated.get().getTitle());
    }

    // ====================== 删除 ======================
    @Test
    void testDelete() {
        Project saved = projectService.createProject(testProject);
        Long id = saved.getId();

        projectService.deleteProject(id);

        Optional<Project> afterDelete = projectService.getProjectById(id);
        Assertions.assertTrue(afterDelete.isEmpty());
    }

    // ====================== 查询列表 ======================
    @Test
    void testGetAll() {
        projectService.createProject(testProject);
        List<Project> list = projectService.getAllProjects();
        for(Project project : list){
            System.out.println(project);
        }
        Assertions.assertFalse(list.isEmpty());
    }

    // ====================== 按分类查询 ======================
    @Test
    void testGetByCategory() {
        projectService.createProject(testProject);
        List<Project> list = projectService.getProjectsByCategory("后端");
        Assertions.assertFalse(list.isEmpty());
    }

    // ====================== 搜索 ======================
    @Test
    void testSearch() {
        projectService.createProject(testProject);
        List<Project> list = projectService.searchProjects("集成测试");
        Assertions.assertFalse(list.isEmpty());
    }

    @Test
    void testGetProjectWithTechs() {
        var project=projectService.getProjectWithTechs(1L);
        System.out.println(project);
    }
}