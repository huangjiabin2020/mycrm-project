package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.Admin;
import com.binge.service.IAdminService;
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
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    IAdminService iAdminService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iAdminService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<Admin> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iAdminService.page(page).getRecords(), iAdminService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iAdminService.getById(id));
    }

    @PostMapping("add")
    public AxiosResult add(@RequestBody Admin admin) {
        iAdminService.save(admin);
        return AxiosResult.success();
    }

    @PutMapping("update")
    public AxiosResult update(@RequestBody Admin admin) {
        iAdminService.updateById(admin);
//        iAdminService.test(admin);  测试事务
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    public AxiosResult delete(@PathVariable Serializable id) {
        iAdminService.removeById(id);
        return AxiosResult.success();
    }


}
