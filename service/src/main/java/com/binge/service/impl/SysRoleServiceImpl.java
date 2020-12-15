package com.binge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.SysRole;
import com.binge.mapper.SysRoleMapper;
import com.binge.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@Service
@Transactional

public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

}
