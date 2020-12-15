package com.binge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.BaseSupplier;
import com.binge.mapper.BaseSupplierMapper;
import com.binge.service.IBaseSupplierService;
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

public class BaseSupplierServiceImpl extends ServiceImpl<BaseSupplierMapper, BaseSupplier> implements IBaseSupplierService {

}
