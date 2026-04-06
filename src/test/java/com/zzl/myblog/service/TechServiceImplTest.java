package com.zzl.myblog.service;

import com.zzl.myblog.entity.Tech;
import com.zzl.myblog.mapper.TechMapper;
import com.zzl.myblog.service.impl.TechServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension; //Mockito扩展

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Mockito 扩展，不是SpringExtension
@ExtendWith(MockitoExtension.class)
public class TechServiceImplTest {

    @Mock
    private TechMapper techMapper;

    @InjectMocks
    private TechServiceImpl techService;

    Tech testTech = new Tech();

    @BeforeEach
    public void setup() {
        testTech.setId(1L);
        testTech.setName("测试");
        testTech.setCategory("类型");
        testTech.setIcon("log");
    }

    @Test
    void CreateTech_正常创建() {
        when(techMapper.insert(any(Tech.class))).thenReturn(1);

        Tech result = techService.createTech(testTech);
        //断言
        assertNotNull(result);
        assertEquals(testTech, result);
    }

    @Test
    void CreateTech_技术标签为空(){
        //断言
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            techService.createTech(null);
        });
        assertEquals("技术标签信息不能为空",e.getMessage());
    }

    @Test
    void CreateTech_空的名称(){
        testTech.setName("");
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            techService.createTech(testTech);
        });
        assertEquals("技术名称不能为空",e.getMessage());

    }

    @Test
    void CreateTech_标签已存在(){
        when(techMapper.findByName("测试")).thenReturn(Optional.of(testTech));
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            techService.createTech(testTech);
        });
        assertEquals("技术标签已存在: "+testTech.getName(),e.getMessage());

    }

    @Test
    void getTechBuyId_参数id为空(){
        var result=techService.getTechById(null);
        assertEquals(Optional.empty(),result);
    }
    @Test
    void getTechBuyId_正常查寻(){
        when(techMapper.findById(1L)).thenReturn(testTech);
        Optional<Tech> result=techService.getTechById(1L);
        assertEquals(Optional.of(testTech), result);
    }

    // ====================== 根据名称查询 ======================
    @Test
    void getTechByName_名称为空() {
        Optional<Tech> result = techService.getTechByName("");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void getTechByName_正常查询() {
        when(techMapper.findByName("测试")).thenReturn(Optional.of(testTech));
        Optional<Tech> result = techService.getTechByName("测试");
        assertTrue(result.isPresent());
    }

    // ====================== 查询全部 ======================
    @Test
    void getAllTechs_正常返回() {
        when(techMapper.findAll()).thenReturn(List.of(testTech));
        List<Tech> result = techService.getAllTechs();
        assertFalse(result.isEmpty());
    }

    // ====================== 根据分类查询 ======================
    @Test
    void getTechsByCategory_分类为空_返回全部() {
        when(techMapper.findAll()).thenReturn(List.of(testTech));
        List<Tech> result = techService.getTechsByCategory("");
        assertFalse(result.isEmpty());
    }

    @Test
    void getTechsByCategory_正常查询() {
        when(techMapper.findByCategory("类型")).thenReturn(List.of(testTech));
        List<Tech> result = techService.getTechsByCategory("类型");
        assertFalse(result.isEmpty());
    }

    // ====================== 查询热门技术 ======================
    @Test
    void getHotTechs_使用默认值10() {
        when(techMapper.findHotTechs(10)).thenReturn(List.of(testTech));
        List<Tech> result = techService.getHotTechs(0);
        assertFalse(result.isEmpty());
    }

    @Test
    void getHotTechs_正常限制() {
        when(techMapper.findHotTechs(5)).thenReturn(List.of(testTech));
        List<Tech> result = techService.getHotTechs(5);
        assertFalse(result.isEmpty());
    }

    // ====================== 批量创建（不存在则创建） ======================
    @Test
    void createTechsIfNotExist_空列表返回空() {
        List<Tech> result = techService.createTechsIfNotExist(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void createTechsIfNotExist_已存在_不创建() {
        when(techMapper.findByName("测试")).thenReturn(Optional.of(testTech));
        List<Tech> result = techService.createTechsIfNotExist(List.of("测试"));
        assertFalse(result.isEmpty());
        verify(techMapper, never()).insert(any());
    }

    @Test
    void createTechsIfNotExist_不存在_创建() {
        when(techMapper.findByName("新测试")).thenReturn(Optional.empty());
        when(techMapper.insert(any(Tech.class))).thenReturn(1);

        List<Tech> result = techService.createTechsIfNotExist(List.of("新测试"));
        assertFalse(result.isEmpty());
        verify(techMapper, times(1)).insert(any());
    }


}
