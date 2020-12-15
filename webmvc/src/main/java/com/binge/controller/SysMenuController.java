package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.SysMenu;
import com.binge.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/sysmenu")
public class SysMenuController {
    @Autowired
    ISysMenuService iSysMenuService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iSysMenuService.list());
    }

    @GetMapping("menuTree")
    public AxiosResult getAllMenuTree() {
        List<SysMenu> all = iSysMenuService.list();
//        递归，先拿第一级
        List<SysMenu> collect = all.stream().filter(item -> item.getParentId().equals(0L)).collect(Collectors.toList());
        collect.forEach(item -> {
            iSysMenuService.getMenuChildren(item, all);
        });
        return AxiosResult.success(collect);
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<SysMenu> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iSysMenuService.page(page).getRecords(), iSysMenuService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iSysMenuService.getById(id));
    }

    @PostMapping("add")
    public AxiosResult add(@RequestBody SysMenu SysMenu) {
        iSysMenuService.save(SysMenu);
        return AxiosResult.success();
    }

    @PutMapping("update")
    public AxiosResult update(@RequestBody SysMenu SysMenu) {
        iSysMenuService.updateById(SysMenu);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    public AxiosResult delete(@PathVariable Serializable id) {
        iSysMenuService.removeById(id);
        return AxiosResult.success();
    }
}
