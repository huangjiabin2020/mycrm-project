package com.binge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.Admin;
import com.binge.mapper.AdminMapper;
import com.binge.service.IAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Override
    public void test(Admin admin) {
        this.updateById(admin);
        int i = 1 / 0;
        admin.setName("模拟事务中出现问题");
        this.updateById(admin);
    }
}
