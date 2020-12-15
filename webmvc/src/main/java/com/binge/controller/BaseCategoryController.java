package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.annotation.HasPerm;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.BaseCategory;
import com.binge.service.IBaseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/category")
public class BaseCategoryController {
    @Autowired
    IBaseCategoryService iBaseCategoryService;


    @GetMapping("categoryTree")
    public AxiosResult getCategoryTree() {
        List<BaseCategory> baseCategories = iBaseCategoryService.getCategoryTree();
        return AxiosResult.success(baseCategories);
    }


    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iBaseCategoryService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<BaseCategory> page = new Page<>(currentPage, pageSize);
        Page<BaseCategory> categoryPage = iBaseCategoryService.page(page);
        categoryPage.getRecords().forEach(item -> {
            Integer pId = item.getPId();
            if (pId.equals(0)) {
//                说明本身是一级分类，没有父类
                item.setPname("没有父类");
            } else {
                BaseCategory byId = iBaseCategoryService.getById(pId);
                item.setPname(byId.getName());
            }
        });
        PageResult result = PageResult.instance(categoryPage.getRecords(), categoryPage.getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    @HasPerm("base:type:findById")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iBaseCategoryService.getById(id));
    }

    @PostMapping("add")
    @HasPerm("base:type:add")
    public AxiosResult add(@RequestBody BaseCategory BaseCategory) {
        iBaseCategoryService.save(BaseCategory);
        return AxiosResult.success();
    }

    @PutMapping("update")
    @HasPerm("base:type:update")
    public AxiosResult update(@RequestBody BaseCategory BaseCategory) {
        iBaseCategoryService.updateById(BaseCategory);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    @HasPerm("base:type:delete")
    public AxiosResult delete(@PathVariable Serializable id) {
        iBaseCategoryService.removeById(id);
        return AxiosResult.success();
    }


}
