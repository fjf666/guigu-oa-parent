package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysRoleService;
import com.atguigu.common.config.exception.GuiguException;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    //注入service
    @Autowired
    private SysRoleService sysRoleService;


    //1.查询所有角色 和 当前用户 所属角色
    @ApiOperation("获取角色")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){

        Map<String, Object> map = sysRoleService.findRoleDataByUserId(userId);
        return Result.ok(map);
    }

    //2.为用户分配角色
    @ApiOperation("为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }

    //查询所有角色
//    @GetMapping("/findAll")
//    public List<SysRole> findAll(){
//        /http://localhost:8800/admin/system/sysRole/findAll/
//        //调用service方法
//        List<SysRole> list = sysRoleService.list();
//        return list;
//    }

    //同一返回数据（有状态码 信息 数据)
    @ApiOperation(value = "获取全部角色列表") // 可以没有 有是方便测试
    @GetMapping("/findAll")
    public Result findAll(){
        //http://localhost:8800/admin/system/sysRole/findAll
        //调用service方法
        //模拟异常效果
//        try {
//            int i = 1 / 0;
//        } catch (Exception e) {
//            //抛出自定义异常
//            throw new GuiguException(20001, "执行了自定义异常处理..");
//        }
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }

    //条件分页查询
    //page 当前页 limit 每页显示记录数
    //SysRoleQueryVo 条件查询对象
    @ApiOperation("条件分页查询")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo){
        //1.创建Page对象，传递分页相关参数
        //page 当前页 limit每页显示记录数
        Page<SysRole> pageParam = new Page<>(page, limit);

        //2.封装条件， 判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)){
            //封装
            wrapper.like(SysRole::getRoleName, roleName);
        }

        //3.调用方法实现
        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    //添加角色
    @ApiOperation("添加角色")
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @PostMapping("save")
    //@RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
    public Result save(@RequestBody SysRole role){
        //调用service的方法
        boolean is_success = sysRoleService.save(role);
        if (is_success){
            return Result.ok();
        } else{
            return Result.fail();
        }
    }

    //修改角色-根据id查询
    @ApiOperation("根据id查询")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    //修改角色-最终修改
    //添加角色
    @ApiOperation("修改角色")
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @PutMapping("update")
    public Result update(@RequestBody SysRole role){
        //调用service的方法
        boolean is_success = sysRoleService.updateById(role);
        if (is_success){
            return Result.ok();
        } else{
            return Result.fail();
        }
    }

    //根据id删除
    @ApiOperation("根据id删除")
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean is_success = sysRoleService.removeById(id);
        if (is_success){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    //批量删除
    //前端的数组格式对应后端Java的集合（list）
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean is_success = sysRoleService.removeByIds(idList);
        if (is_success){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

}
