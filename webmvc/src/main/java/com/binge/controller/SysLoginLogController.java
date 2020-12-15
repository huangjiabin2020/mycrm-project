package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.SysLoginLog;
import com.binge.service.ISysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * <p>
 * 系统访问记录 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/loginlog")
public class SysLoginLogController {
    @Autowired
    ISysLoginLogService iSysLoginLogService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iSysLoginLogService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<SysLoginLog> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iSysLoginLogService.page(page).getRecords(), iSysLoginLogService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iSysLoginLogService.getById(id));
    }

    @PostMapping("add")
    public AxiosResult add(@RequestBody SysLoginLog SysLoginLog) {
        iSysLoginLogService.save(SysLoginLog);
        return AxiosResult.success();
    }

    @PutMapping("update")
    public AxiosResult update(@RequestBody SysLoginLog SysLoginLog) {
        iSysLoginLogService.updateById(SysLoginLog);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    public AxiosResult delete(@PathVariable Serializable id) {
        iSysLoginLogService.removeById(id);
        return AxiosResult.success();
    }
}
