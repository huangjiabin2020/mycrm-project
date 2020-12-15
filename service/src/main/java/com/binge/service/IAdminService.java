package com.binge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.binge.entity.Admin;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
public interface IAdminService extends IService<Admin> {
    void test(Admin admin);
}
