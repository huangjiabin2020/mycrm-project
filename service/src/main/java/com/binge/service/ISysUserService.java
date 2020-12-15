package com.binge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.binge.entity.SysMenu;
import com.binge.entity.SysUser;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
public interface ISysUserService extends IService<SysUser> {

    List<SysMenu> findUserRouterByUserId(Serializable userId);

    List<SysMenu> findUserBtnPermByUserId(Serializable userId);
}
