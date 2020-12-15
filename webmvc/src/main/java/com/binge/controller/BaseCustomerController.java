package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.annotation.HasPerm;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.BaseCustomer;
import com.binge.service.IBaseCustomerService;
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
@RequestMapping("main/customer")
public class BaseCustomerController {
    @Autowired
    IBaseCustomerService iBaseCustomerService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iBaseCustomerService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<BaseCustomer> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iBaseCustomerService.page(page).getRecords(), iBaseCustomerService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    @HasPerm("base:customer:findById")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iBaseCustomerService.getById(id));
    }

    @PostMapping("add")
    @HasPerm("base:customer:add")
    public AxiosResult add(@RequestBody BaseCustomer BaseCustomer) {
        iBaseCustomerService.save(BaseCustomer);
        return AxiosResult.success();
    }

    @PutMapping("update")
    @HasPerm("base:customer:update")
    public AxiosResult update(@RequestBody BaseCustomer BaseCustomer) {
        iBaseCustomerService.updateById(BaseCustomer);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    @HasPerm("base:customer:delete")
    public AxiosResult delete(@PathVariable Serializable id) {
        iBaseCustomerService.removeById(id);
        return AxiosResult.success();
    }

}
