package com.zzl.myblog.service;

import com.zzl.myblog.entity.Project;
import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.mapper.ProjectMapper;
import com.zzl.myblog.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock
    private ProjectMapper projectMapper;

    // 必须是实现类！！！
    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project testProject;
    private List<Project> projectList;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setTitle("测试项目");
        testProject.setDescription("测试描述");
        testProject.setCategory("后端");
        testProject.setDate(LocalDate.now());

        List<Tech> techList = new ArrayList<>();
        techList.add(new Tech("Java", "后端", "java-icon"));
        testProject.setTechs(techList);

        projectList = new ArrayList<>();
        projectList.add(testProject);
    }

    // ======================== 新增项目测试 ========================
    @Test
    void createProject_正常创建() {
        // 这里绝对没有 doNothing！！！
        when(projectMapper.insert(any(Project.class))).thenReturn(1);

        Project result = projectService.createProject(testProject);

        Assertions.assertNotNull(result);
        verify(projectMapper, times(1)).insert(any(Project.class));
    }

    @Test
    void createProject_项目为空_抛出异常() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> projectService.createProject(null));
    }

    @Test
    void createProject_标题为空_抛出异常() {
        testProject.setTitle(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> projectService.createProject(testProject));
    }

    // ======================== 修改项目测试 ========================
    @Test
    void updateProject_正常修改() {
        when(projectMapper.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectMapper.update(any(Project.class))).thenReturn(1);

        Project result = projectService.updateProject(testProject);
        Assertions.assertNotNull(result);
    }

    @Test
    void updateProject_项目不存在_抛出异常() {
        testProject.setId(99L);
        when(projectMapper.findById(99L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> projectService.updateProject(testProject));
    }

    // ======================== 删除项目测试 ========================
    @Test
    void deleteProject_正常删除() {
        when(projectMapper.deleteById(1L)).thenReturn(1);
        projectService.deleteProject(1L);
        verify(projectMapper, times(1)).deleteById(1L);
    }

    // ======================== 根据ID查询 ========================
    @Test
    void getProjectById_存在() {
        when(projectMapper.findById(1L)).thenReturn(Optional.of(testProject));
        Optional<Project> result = projectService.getProjectById(1L);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void getProjectById_不存在() {
        when(projectMapper.findById(99L)).thenReturn(Optional.empty());
        Optional<Project> result = projectService.getProjectById(99L);
        Assertions.assertFalse(result.isPresent());
    }

    // ======================== 查询所有 ========================
    @Test
    void getAllProjects() {
        when(projectMapper.findAll()).thenReturn(projectList);
        List<Project> result = projectService.getAllProjects();
        Assertions.assertEquals(1, result.size());
    }

    // ======================== 根据分类查询 ========================
    @Test
    void getProjectsByCategory() {
        when(projectMapper.findByCategory("后端")).thenReturn(projectList);
        List<Project> result = projectService.getProjectsByCategory("后端");
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getProjectsByCategory_空参数() {
        when(projectMapper.findAll()).thenReturn(projectList);
        List<Project> result = projectService.getProjectsByCategory("");
        Assertions.assertEquals(1, result.size());
    }

    // ======================== 根据标题查询 ========================
    @Test
    void getProjectsByTitle() {
        when(projectMapper.findByName("测试项目")).thenReturn(projectList);
        List<Project> result = projectService.getProjectsByTitle("测试项目");
        Assertions.assertEquals(1, result.size());
    }

    // ======================== 关键词搜索 ========================
    @Test
    void searchProjects() {
        when(projectMapper.search("测试")).thenReturn(projectList);
        List<Project> result = projectService.searchProjects("测试");
        Assertions.assertEquals(1, result.size());
    }

    // ======================== 查询所有带技术栈 ========================
    @Test
    void getAllProjectsWithTechs() {
        when(projectMapper.findAllWithTechs()).thenReturn(projectList);
        List<Project> result = projectService.getAllProjectsWithTechs();
        Assertions.assertEquals(1, result.size());
    }

    // ======================== 根据技术栈名称查询 ========================
    @Test
    void getProjectsByTechName() {
        when(projectMapper.findByTechName("Java")).thenReturn(projectList);
        List<Project> result = projectService.getProjectsByTechName("Java");
        Assertions.assertEquals(1, result.size());
    }

    // ======================== 高级搜索 ========================
    @Test
    void advancedSearch_有参数() {
        when(projectMapper.advancedSearch(any(), any(), any(), any(), any())).thenReturn(projectList);
        List<Project> result = projectService.advancedSearch(
                "测试", "后端", LocalDate.now(), LocalDate.now(), "Java"
        );
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void advancedSearch_无参数() {
        when(projectMapper.findAllWithTechs()).thenReturn(projectList);
        List<Project> result = projectService.advancedSearch(null, null, null, null, null);
        Assertions.assertEquals(1, result.size());
    }
}