package com.binge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.SysMenu;
import com.binge.entity.SysRoleMenu;
import com.binge.entity.SysUserRole;
import com.binge.mapper.SysMenuMapper;
import com.binge.service.ISysMenuService;
import com.binge.service.ISysRoleMenuService;
import com.binge.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@Service
@Transactional

public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    ISysUserRoleService iSysUserRoleService;
    @Autowired
    ISysRoleMenuService iSysRoleMenuService;

    @Override
    public void getMenuChildren(SysMenu sysMenu, List<SysMenu> all) {
        List<SysMenu> children = new ArrayList<>();
        all.forEach(item -> {
            if (item.getParentId().equals(sysMenu.getMenuId())) {
                children.add(item);
            }
        });
//        拿到所有的子菜单
        if (!ObjectUtils.isEmpty(children)) {
//            子菜单存在，设置为孩子，并继续递归
            sysMenu.setChildren(children);
            children.forEach(item -> {
                getMenuChildren(item, all);
            });
        }
    }

    @Override
    public List<SysMenu> getRouterByUserId(Serializable userId) {
        //        最终结果：菜单数组
        ArrayList<SysMenu> results = new ArrayList<>();
        QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
        sysUserRoleQueryWrapper.lambda().eq(SysUserRole::getUserId, userId);
//        根据userId找到对应的roleId
        List<SysUserRole> userRoles = iSysUserRoleService.list(sysUserRoleQueryWrapper);
//        根据roleId找到对应的menuId
        userRoles.forEach(item -> {
            QueryWrapper<SysRoleMenu> menuQueryWrapper = new QueryWrapper<>();
            menuQueryWrapper.lambda().eq(SysRoleMenu::getRoleId, item.getRoleId());
            List<SysRoleMenu> menuIds = iSysRoleMenuService.list(menuQueryWrapper);
//            根据menuId获取menu
            menuIds.forEach(menuId -> {
                SysMenu byId = this.getById(menuId);
                if (byId != null) {
                    results.add(byId);
                }
            });
        });

//        结果包含了按钮级别的权限，而我们的目的是展示左侧的动态菜单，所以只要目录和菜单的权限即可
//        List<SysMenu> all = results.stream().filter(item -> !item.getMenuType().equals("F")).collect(Collectors.toList());
        ArrayList<SysMenu> all = new ArrayList<>();
        results.forEach(result -> {
            if (!"F".equals(result.getMenuType())) {
                all.add(result);
            }
        });
//        接着组装成树形结构
//        先拿到一级目录
//        List<SysMenu> rootMenus = all.stream().filter(item -> item.getParentId().equals(0)).collect(Collectors.toList());
        ArrayList<SysMenu> rootMenus = new ArrayList<>();
        all.forEach(item -> {
            if (item.getParentId().longValue() == 0L) {
                rootMenus.add(item);
            }
        });
        rootMenus.forEach(item -> {
//            递归的方法已经写好
            getMenuChildren(item, all);
        });
        return rootMenus;
    }

    @Override
    public List<SysMenu> getSimpleMenuByUserId(Serializable userId) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUserRole::getUserId, userId);
        List<SysUserRole> sysUserRoles = iSysUserRoleService.list(wrapper);
        ArrayList<SysMenu> sysMenus = new ArrayList<>();
        sysUserRoles.forEach(sysUserRole -> {
            Long roleId = sysUserRole.getRoleId();
            QueryWrapper<SysRoleMenu> wrapper1 = new QueryWrapper<>();
            wrapper1.lambda().eq(SysRoleMenu::getRoleId, roleId);
            List<SysRoleMenu> sysRoleMenus = iSysRoleMenuService.list(wrapper1);
            sysRoleMenus.forEach(sysRoleMenu -> {
                QueryWrapper<SysMenu> wrapper2 = new QueryWrapper<>();
                wrapper2.lambda().eq(SysMenu::getMenuId, sysRoleMenu.getMenuId());
                List<SysMenu> sysMenus1 = this.list(wrapper2);
                sysMenus.addAll(sysMenus1);
            });
        });
        return sysMenus;
    }
}
