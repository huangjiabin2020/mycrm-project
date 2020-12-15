package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.annotation.HasPerm;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.BaseUnit;
import com.binge.service.IBaseUnitService;
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
@RequestMapping("main/unit")
public class BaseUnitController {
    @Autowired
    IBaseUnitService iBaseUnitService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iBaseUnitService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<BaseUnit> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iBaseUnitService.page(page).getRecords(), iBaseUnitService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    @HasPerm("base:unit:findById")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iBaseUnitService.getById(id));
    }

    @PostMapping("add")
    @HasPerm("base:unit:add")
    public AxiosResult add(@RequestBody BaseUnit BaseUnit) {
        iBaseUnitService.save(BaseUnit);
        return AxiosResult.success();
    }

    @PutMapping("update")
    @HasPerm("base:unit:update")
    public AxiosResult update(@RequestBody BaseUnit BaseUnit) {
        iBaseUnitService.updateById(BaseUnit);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    @HasPerm("base:unit:delete")
    public AxiosResult delete(@PathVariable Serializable id) {
        iBaseUnitService.removeById(id);
        return AxiosResult.success();
    }
}
