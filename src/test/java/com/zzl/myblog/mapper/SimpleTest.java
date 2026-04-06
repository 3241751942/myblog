package com.zzl.myblog.mapper;

import com.zzl.myblog.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class SimpleTest {
    @Autowired
    ProjectMapper projectMapper;
    @Test
    void testFindAll() {
        List<Project> projects = projectMapper.findAllWithTechs();
        for (Project project : projects) {
            System.out.println(project);
        }
        System.out.println("下一个方法：");
        Optional<Project> project = projectMapper.findById(1L);
        System.out.println(project);
    }
}
