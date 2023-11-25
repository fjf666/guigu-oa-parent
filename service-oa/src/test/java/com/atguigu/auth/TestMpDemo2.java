package com.atguigu.auth;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestMpDemo2 {

    //注入
    @Autowired
    private SysRoleService service;

    //查询所有记录
    @Test
    public void getAll(){
        List<SysRole> list = service.list();
        System.out.println(list);
    }

    @Test
    public void selectById(){
        SysRole role = service.getById(2);
        System.out.println(role);
    }

    @Test
    public void insert(){
        SysRole role = new SysRole();
        role.setDescription("2");
        role.setRoleCode("role2");
        role.setRoleName("董事长");
        boolean update = service.saveOrUpdate(role);
        System.out.println(update);
    }

    @Test
    public void delete(){
        boolean b = service.removeById(12);
        System.out.println(b);
    }

    @Test
    public void update(){
        SysRole role = service.getById(9);
        role.setRoleCode("role11");
        boolean b = service.updateById(role);
        System.out.println(b);
    }

}
