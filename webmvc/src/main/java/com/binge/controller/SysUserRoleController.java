package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.SysUserRole;
import com.binge.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * <p>
 * 用户和角色关联表 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("/sys-user-role")
public class SysUserRoleController {
    @Autowired
    ISysUserRoleService iSysUserRoleService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iSysUserRoleService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<SysUserRole> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iSysUserRoleService.page(page).getRecords(), iSysUserRoleService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iSysUserRoleService.getById(id));
    }

    @PostMapping("add")
    public AxiosResult add(@RequestBody SysUserRole SysUserRole) {
        iSysUserRoleService.save(SysUserRole);
        return AxiosResult.success();
    }

    @PutMapping("update")
    public AxiosResult update(@RequestBody SysUserRole SysUserRole) {
        iSysUserRoleService.updateById(SysUserRole);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    public AxiosResult delete(@PathVariable Serializable id) {
        iSysUserRoleService.removeById(id);
        return AxiosResult.success();
    }
}
