package com.binge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binge.entity.BaseCategory;
import com.binge.mapper.BaseCategoryMapper;
import com.binge.service.IBaseCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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

public class BaseCategoryServiceImpl extends ServiceImpl<BaseCategoryMapper, BaseCategory> implements IBaseCategoryService {

    @Override
    public List<BaseCategory> getCategoryTree() {
        List<BaseCategory> all = this.list();
        //获得所有的一级分类
        List<BaseCategory> collect = all.stream().filter(category -> {
            return category.getPId() == 0;
        }).collect(Collectors.toList());

        collect.forEach(item -> {
            getCategoryChild(item, all);
        });
        return collect;
    }

    public void getCategoryChild(BaseCategory category, List<BaseCategory> all) {
        List<BaseCategory> collect = all.stream().filter(item -> item.getPId().equals(category.getId())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
//            设置孩子类型
            category.setChildren(collect);
            collect.forEach(item -> {
                getCategoryChild(item, all);
            });
        }

    }
}
