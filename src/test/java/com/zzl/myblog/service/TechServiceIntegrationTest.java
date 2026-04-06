package com.zzl.myblog.service;

import com.zzl.myblog.entity.Tech;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//集成测试
@SpringBootTest
@Transactional  //事务
public class TechServiceIntegrationTest {

    @Autowired
    private TechService techService;

    private Tech testTech;

    @BeforeEach
    void setUp() {
        // 构造测试数据
        testTech = new Tech();
        testTech.setName("集成测试技术");
        testTech.setCategory("测试分类");
        testTech.setIcon("test-icon");
    }

    // ==================== 1. 创建技术 ====================
    @Test
    void createTech_正常创建() {
        Tech saved = techService.createTech(testTech);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("集成测试技术", saved.getName());
    }

    @Test
    void createTech_参数为空_抛异常() {
        assertThrows(IllegalArgumentException.class, () -> {
            techService.createTech(null);
        });
    }

    @Test
    void createTech_名称为空_抛异常() {
        testTech.setName("");
        assertThrows(IllegalArgumentException.class, () -> {
            techService.createTech(testTech);
        });
    }

    @Test
    void createTech_重复名称_抛异常() {
        techService.createTech(testTech);
        assertThrows(RuntimeException.class, () -> {
            techService.createTech(testTech);
        });
    }

    // ==================== 2. 根据ID查询 ====================
    @Test
    void getTechById_查询成功() {
        Tech saved = techService.createTech(testTech);
        Optional<Tech> found = techService.getTechById(saved.getId());
        assertTrue(found.isPresent());
    }

    @Test
    void getTechById_不存在() {
        Optional<Tech> found = techService.getTechById(99999L);
        assertFalse(found.isPresent());
    }

    // ==================== 3. 根据名称查询 ====================
    @Test
    void getTechByName_查询成功() {
        techService.createTech(testTech);
        Optional<Tech> found = techService.getTechByName("集成测试技术");
        assertTrue(found.isPresent());
    }

    // ==================== 4. 查询全部 ====================
    @Test
    void getAllTechs_返回列表() {
        techService.createTech(testTech);
        List<Tech> list = techService.getAllTechs();
        for (Tech tech : list) {
            System.out.println(tech);
        }

        assertFalse(list.isEmpty());
    }

    // ==================== 5. 根据分类查询 ====================
    @Test
    void getTechsByCategory_正常查询() {
        techService.createTech(testTech);
        List<Tech> list = techService.getTechsByCategory("测试分类");
        assertFalse(list.isEmpty());
    }

    @Test
    void getTechsByCategory_分类为空_返回全部() {
        techService.createTech(testTech);
        List<Tech> list = techService.getTechsByCategory("");
        assertFalse(list.isEmpty());
    }

    // ==================== 6. 热门技术 ====================
    @Test
    void getHotTechs_默认限制10() {
        techService.createTech(testTech);
        List<Tech> list = techService.getHotTechs(0);
        assertNotNull(list);
    }

    // ==================== 7. 批量创建（不存在则创建） ====================
    @Test
    void createTechsIfNotExist_批量创建成功() {
        List<String> names = List.of("Vue3", "React", "Spring Boot");
        List<Tech> result = techService.createTechsIfNotExist(names);
        assertEquals(3, result.size());
    }

    @Test
    void createTechsIfNotExist_已存在_不重复创建() {
        techService.createTech(testTech);
        List<String> names = List.of("集成测试技术", "新技术");
        List<Tech> result = techService.createTechsIfNotExist(names);
        assertEquals(2, result.size());
    }

    @Test
    void getTechsByProjectId(){
        var result=techService.getTechsByProjectId(2L);
        System.out.println(result);
    }
}