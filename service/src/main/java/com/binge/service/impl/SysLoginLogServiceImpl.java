package com.binge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.SysLoginLog;
import com.binge.mapper.SysLoginLogMapper;
import com.binge.service.ISysLoginLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@Service
@Transactional

public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

}
