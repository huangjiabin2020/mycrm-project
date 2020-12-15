package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.annotation.HasPerm;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.BaseSupplier;
import com.binge.service.IBaseSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/supplier")
public class BaseSupplierController {
    @Autowired
    IBaseSupplierService iBaseSupplierService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iBaseSupplierService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<BaseSupplier> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iBaseSupplierService.page(page).getRecords(), iBaseSupplierService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    @HasPerm("base:supplier:findById")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iBaseSupplierService.getById(id));
    }

    @PostMapping("add")
    @HasPerm("base:supplier:add")
    public AxiosResult add(@RequestBody BaseSupplier BaseSupplier) {
        iBaseSupplierService.save(BaseSupplier);
        return AxiosResult.success();
    }

    @PutMapping("update")
    @HasPerm("base:supplier:update")
    public AxiosResult update(@RequestBody BaseSupplier BaseSupplier) {
        iBaseSupplierService.updateById(BaseSupplier);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    @HasPerm("base:supplier:delete")
    public AxiosResult delete(@PathVariable Serializable id) {
        iBaseSupplierService.removeById(id);
        return AxiosResult.success();
    }
}
