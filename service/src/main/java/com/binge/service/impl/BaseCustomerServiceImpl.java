package com.binge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.BaseCustomer;
import com.binge.mapper.BaseCustomerMapper;
import com.binge.service.IBaseCustomerService;
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

public class BaseCustomerServiceImpl extends ServiceImpl<BaseCustomerMapper, BaseCustomer> implements IBaseCustomerService {

}
