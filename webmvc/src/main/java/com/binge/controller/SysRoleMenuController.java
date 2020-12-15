package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.SysRoleMenu;
import com.binge.service.ISysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * <p>
 * 角色和菜单关联表 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/rolemenu")
public class SysRoleMenuController {
    @Autowired
    ISysRoleMenuService iSysRoleMenuService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iSysRoleMenuService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<SysRoleMenu> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iSysRoleMenuService.page(page).getRecords(), iSysRoleMenuService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iSysRoleMenuService.getById(id));
    }

    @PostMapping("add")
    public AxiosResult add(@RequestBody SysRoleMenu SysRoleMenu) {
        iSysRoleMenuService.save(SysRoleMenu);
        return AxiosResult.success();
    }

    @PutMapping("update")
    public AxiosResult update(@RequestBody SysRoleMenu SysRoleMenu) {
        iSysRoleMenuService.updateById(SysRoleMenu);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    public AxiosResult delete(@PathVariable Serializable id) {
        iSysRoleMenuService.removeById(id);
        return AxiosResult.success();
    }
}
