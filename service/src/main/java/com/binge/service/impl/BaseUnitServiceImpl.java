package com.binge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.BaseUnit;
import com.binge.mapper.BaseUnitMapper;
import com.binge.service.IBaseUnitService;
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

public class BaseUnitServiceImpl extends ServiceImpl<BaseUnitMapper, BaseUnit> implements IBaseUnitService {

}
