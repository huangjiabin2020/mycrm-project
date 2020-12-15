package com.binge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.SysOperLog;
import com.binge.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * <p>
 * 操作日志记录 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/operlog")
public class SysOperLogController {
    @Autowired
    ISysOperLogService iSysOperLogService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iSysOperLogService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<SysOperLog> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iSysOperLogService.page(page).getRecords(), iSysOperLogService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iSysOperLogService.getById(id));
    }

    @PostMapping("add")
    public AxiosResult add(@RequestBody SysOperLog SysOperLog) {
        iSysOperLogService.save(SysOperLog);
        return AxiosResult.success();
    }

    @PutMapping("update")
    public AxiosResult update(@RequestBody SysOperLog SysOperLog) {
        iSysOperLogService.updateById(SysOperLog);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    public AxiosResult delete(@PathVariable Serializable id) {
        iSysOperLogService.removeById(id);
        return AxiosResult.success();
    }
}
