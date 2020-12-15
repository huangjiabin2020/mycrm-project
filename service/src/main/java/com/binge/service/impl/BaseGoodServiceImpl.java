package com.binge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.BaseGood;
import com.binge.mapper.BaseGoodMapper;
import com.binge.service.IBaseGoodService;
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

public class BaseGoodServiceImpl extends ServiceImpl<BaseGoodMapper, BaseGood> implements IBaseGoodService {

}
