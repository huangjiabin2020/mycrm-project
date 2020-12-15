package com.binge.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.annotation.HasPerm;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.entity.BaseGood;
import com.binge.service.IBaseCategoryService;
import com.binge.service.IBaseGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/good")
public class BaseGoodController {
    @Autowired
    IBaseGoodService iBaseGoodService;
    @Autowired
    IBaseCategoryService iBaseCategoryService;
    public static Map<String, String> statusMap = new HashMap<>();

    static {
        statusMap.put("0", "初始化状态");
        statusMap.put("1", "期初库存入仓库");
        statusMap.put("2", "有进货或者出库单据");
    }

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iBaseGoodService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<BaseGood> page = new Page<>(currentPage, pageSize);
        List<BaseGood> records = iBaseGoodService.page(page).getRecords();
        records.forEach(item -> {
//            额外设置类型名字
            item.setTypeName(iBaseCategoryService.getById(item.getTypeId()).getName());
            item.setStateName(statusMap.get(item.getState() + ""));
        });
        PageResult result = PageResult.instance(records, iBaseGoodService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    /**
     * 这个id其实是code
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @HasPerm("base:good:findById")
    public AxiosResult findById(@PathVariable Serializable id) {
        BaseGood baseGood = iBaseGoodService.getOne(new QueryWrapper<BaseGood>().lambda().eq(BaseGood::getGoodCode, id));
        //设置类型名字
        baseGood.setTypeName(iBaseCategoryService.getById(baseGood.getTypeId()).getName());
        //            再设置一下状态的名字
        baseGood.setStateName(statusMap.get(baseGood.getState() + ""));
        return AxiosResult.success(baseGood);
    }

    @PostMapping("add")
    @HasPerm("base:good:add")
    public AxiosResult add(@RequestBody BaseGood BaseGood) {
        iBaseGoodService.save(BaseGood);
        return AxiosResult.success();
    }

    @PutMapping("update")
    @HasPerm({"base:good:update", "base:good:findById"})
    public AxiosResult update(@RequestBody BaseGood baseGood) {
        iBaseGoodService.update(baseGood, new QueryWrapper<BaseGood>().lambda().eq(com.binge.entity.BaseGood::getGoodCode, baseGood.getGoodCode()));
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    @HasPerm("base:good:delete")
    public AxiosResult delete(@PathVariable Serializable id) {
        iBaseGoodService.removeById(id);
        return AxiosResult.success();
    }
}
