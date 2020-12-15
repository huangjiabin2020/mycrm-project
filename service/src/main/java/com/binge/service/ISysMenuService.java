package com.binge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.binge.entity.SysMenu;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
public interface ISysMenuService extends IService<SysMenu> {

    void getMenuChildren(SysMenu sysMenu, List<SysMenu> all);

    List<SysMenu> getRouterByUserId(Serializable userId);

    List<SysMenu> getSimpleMenuByUserId(Serializable userId);
}
