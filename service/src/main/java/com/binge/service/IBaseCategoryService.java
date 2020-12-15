package com.binge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.binge.entity.BaseCategory;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
public interface IBaseCategoryService extends IService<BaseCategory> {
    /**
     * 获得分类的树形结构
     *
     * @return
     */
    List<BaseCategory> getCategoryTree();
}
