package com.binge.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.SysMenu;
import com.binge.entity.SysRole;
import com.binge.entity.SysRoleMenu;
import com.binge.service.ISysMenuService;
import com.binge.service.ISysRoleMenuService;
import com.binge.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/sysrole")
public class SysRoleController {
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysRoleMenuService iSysRoleMenuService;
    @Autowired
    private ISysMenuService iSysMenuService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iSysRoleService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<SysRole> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iSysRoleService.page(page).getRecords(), iSysRoleService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    public AxiosResult findById(@PathVariable Serializable id) {
        List<Long> menuIds = new ArrayList<>();

        SysRole sysrole = iSysRoleService.getById(id);
//        根据roleId查中间表role_menu
        List<SysRoleMenu> list = iSysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().lambda().eq(SysRoleMenu::getRoleId, id));
        ArrayList<SysMenu> sysMenus = new ArrayList<>();
        list.forEach(item -> {
//            前端虚拟了一个主目录，id是0，数据库查不到！
            SysMenu byId = iSysMenuService.getById(item.getMenuId());
            if (byId != null) {
                sysMenus.add(byId);
            }
        });
//        前端根据树形结构的末端选中的id进行勾选，目录不是末端，pass
//        先筛选出菜单和按钮
//        List<SysMenu> mfs = sysMenus.stream().filter(sysMenu -> !sysMenu.getMenuType().equalsIgnoreCase("M")).collect(Collectors.toList());
        ArrayList<SysMenu> mfs = new ArrayList<>();
        sysMenus.forEach(sysMenu -> {

            if (!"M".equalsIgnoreCase(sysMenu.getMenuType())) {
                mfs.add(sysMenu);
            }
        });
//        有的菜单还有孩子--->按钮权限比如增删改查，也不是末端，pass
        mfs.forEach(item -> {
            if (!hasChildren(item, mfs)) {
                menuIds.add(item.getMenuId());
            }
        });
        sysrole.setMenuIds(menuIds);
        return AxiosResult.success(sysrole);
    }

    @PostMapping("add")
    public AxiosResult add(@RequestBody SysRole sysRole) {
        //      注意mybatisplus添加成功后自动把主键封装到对象中
        iSysRoleService.save(sysRole);

        List<Long> menuIds = sysRole.getMenuIds();
//        不仅仅要添加角色，还要添加角色-权限中间表
        if (!ObjectUtils.isEmpty(menuIds)) {
            menuIds.forEach(item -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                //      注意mybatisplus添加成功后自动把主键封装到对象中
                sysRoleMenu.setRoleId(sysRole.getRoleId());
                sysRoleMenu.setMenuId(item);
                iSysRoleMenuService.save(sysRoleMenu);
            });
        }
        return AxiosResult.success();
    }

    @PutMapping("update")
    public AxiosResult update(@RequestBody SysRole sysRole) {
        //        先删了中间表的相关记录，再重新添加
        Long roleId = sysRole.getRoleId();
        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysRoleMenu::getRoleId, roleId);
        iSysRoleMenuService.remove(wrapper);

        sysRole.getMenuIds().forEach(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            iSysRoleMenuService.save(sysRoleMenu);
        });

        iSysRoleService.updateById(sysRole);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    public AxiosResult delete(@PathVariable Serializable id) {
        iSysRoleService.removeById(id);
        return AxiosResult.success();
    }


    private boolean hasChildren(SysMenu sysMenu, List<SysMenu> all) {
        return all.stream().anyMatch(item -> item.getParentId().equals(sysMenu.getMenuId()));
    }
}
