package com.binge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.SysMenu;
import com.binge.entity.SysRoleMenu;
import com.binge.entity.SysUser;
import com.binge.entity.SysUserRole;
import com.binge.mapper.SysUserMapper;
import com.binge.service.ISysMenuService;
import com.binge.service.ISysRoleMenuService;
import com.binge.service.ISysUserRoleService;
import com.binge.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@Service
@Transactional

public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    ISysUserRoleService iSysUserRoleService;
    @Autowired
    ISysRoleMenuService iSysRoleMenuService;
    @Autowired
    ISysMenuService iSysMenuService;

    @Override
    public List<SysMenu> findUserRouterByUserId(Serializable userId) {
//        allMenus已经去重过了
        List<SysMenu> allMenus = findAllMenusByUserId(userId);
//        去除按钮权限的menus(路由信息不需要按钮级别的权限)
        List<SysMenu> menus = allMenus.stream().filter(sysMenu -> !sysMenu.getMenuType().equalsIgnoreCase("F")).collect(Collectors.toList());
//        找一级目录
        List<SysMenu> rootMenus = menus.stream().filter(menu -> menu.getParentId().longValue() == 0).collect(Collectors.toList());
//        通过递归给一级目录找孩子
        rootMenus.forEach(item -> {
            findChildren(item, menus);
        });
        return rootMenus;
    }

    @Override
    public List<SysMenu> findUserBtnPermByUserId(Serializable userId) {
        List<SysMenu> allMenus = findAllMenusByUserId(userId);
        List<SysMenu> btnMenus = allMenus.stream().filter(sysMenu -> sysMenu.getMenuType().equalsIgnoreCase("F")).collect(Collectors.toList());

        return btnMenus;
    }


    public void findChildren(SysMenu sysMenu, List<SysMenu> allMenus) {
        List<SysMenu> children = allMenus.stream().filter(item -> item.getParentId().longValue() == sysMenu.getMenuId().longValue()).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(children)) {
//            设置孩子
            sysMenu.setChildren(children);
//            递归设置孩子的孩子...
            children.forEach(child -> {
                findChildren(child, allMenus);
            });
        }
    }

    private List<SysMenu> findAllMenusByUserId(Serializable userId) {
        List<SysUserRole> sysUserRoles = iSysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId));
        ArrayList<SysMenu> all = new ArrayList<>();
        sysUserRoles.forEach(sysUserRole -> {
            List<SysRoleMenu> sysRoleMenus = iSysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().lambda().eq(SysRoleMenu::getRoleId, sysUserRole.getRoleId()));
            sysRoleMenus.forEach(sysRoleMenu -> {
                SysMenu sysMenu = iSysMenuService.getById(sysRoleMenu.getMenuId());
                if (sysMenu != null) {
                    all.add(sysMenu);
                }
            });
        });
//      对all进行去重（因为一个用户可能对应多个角色，角色之间对应的权限可能重复）
        List<SysMenu> list = all.stream().distinct().collect(Collectors.toList());
//        经过检验，去重成功
        System.err.println(all.size());
        System.err.println(list.size());
        return list;
    }

}
