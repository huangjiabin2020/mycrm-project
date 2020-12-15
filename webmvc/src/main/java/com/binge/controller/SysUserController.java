package com.binge.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binge.common.async.AsyncFactory;
import com.binge.common.async.AsyncManager;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.PageResult;
import com.binge.common.mail.EmailSender;
import com.binge.common.valid.group.AddGroup;
import com.binge.entity.SysRole;
import com.binge.entity.SysUser;
import com.binge.entity.SysUserRole;
import com.binge.service.ISysRoleService;
import com.binge.service.ISysUserRoleService;
import com.binge.service.ISysUserService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@RestController
@RequestMapping("main/sysuser")
public class SysUserController {
    @Autowired
    ISysUserService iSysUserService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    EmailSender emailSender;
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    ISysUserRoleService iSysUserRoleService;//用户角色中间表
    @Autowired
    ISysRoleService iSysRoleService;

    @GetMapping("findAll")
    public AxiosResult findAll() {
        return AxiosResult.success(iSysUserService.list());
    }

    @GetMapping("page")
    public AxiosResult page(int currentPage, int pageSize) {
        Page<SysUser> page = new Page<>(currentPage, pageSize);
        PageResult result = PageResult.instance(iSysUserService.page(page).getRecords(), iSysUserService.page(page).getTotal());
        return AxiosResult.success(result);
    }

    @GetMapping("{id}")
    public AxiosResult findById(@PathVariable Serializable id) {
        return AxiosResult.success(iSysUserService.getById(id));
    }

    @GetMapping("{userId}/roles")
    public AxiosResult findRolesByUserId(@PathVariable Serializable userId) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUserRole::getUserId, userId);
        List<SysUserRole> roleList = iSysUserRoleService.list(wrapper);
//        中间表查完了,再根据中间表的roleId查角色
        ArrayList<SysRole> sysRoles = new ArrayList<>();
        roleList.forEach(item -> {
            SysRole byId = iSysRoleService.getById(item.getRoleId());
            sysRoles.add(byId);
        });
        return AxiosResult.success(sysRoles);

    }

    @PostMapping
    /**
     * @Valid 使用 hibernate valid注解（非分组校验）
     * @Validated(AddGroup.class) 使用分组校验，指定添加时使用AddGroup的校验注解
     * 分组校验时，所有校验注解都必须添加分组，不然没有校验的效果
     * 如果参数有BindingResult bindingResult，即使验证不通过也不会报错
     *
     */
    public AxiosResult add(@Validated(AddGroup.class) @RequestBody SysUser sysUser) {
       /*仅用于测试验证，实际不会使用BindingResult参数，让异常爆出来，转换成自己的异常
        if (bindingResult.hasFieldErrors()){
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            for (int i = 0; i < allErrors.size(); i++) {
                System.err.println(allErrors.get(i).getField());
                System.err.println(allErrors.get(i).getDefaultMessage());
            }
        }*/
        String[] roleIds = sysUser.getRoleIds().split("A");

//       添加的用户默认密码：111
        sysUser.setPassword(passwordEncoder.encode("111"));
//        配置模板引擎
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template;
        try {
            template = configuration.getTemplate("active.ftl", "utf-8");
            HashMap<String, String> map = new HashMap<>();
            map.put("username", sysUser.getUserName());
            map.put("nickname", sysUser.getNickName());
            map.put("password", "111");
//        渲染模板数据
            StringWriter stringWriter = new StringWriter();
            template.process(map, stringWriter);
            //        发送邮件告诉这个用户的密码
//            emailSender.sendUserNameAndPassWord(sysUser, stringWriter.toString());
//            使用全局统一异步任务管理器进行发邮件
            AsyncManager.getInstance().executeTask(AsyncFactory.executeEmail(sysUser.getEmail(), stringWriter.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        保存用户时，一同添加用户_角色中间表
        iSysUserService.save(sysUser);
        for (int i = 0; i < roleIds.length; i++) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getUserId());
            sysUserRole.setRoleId(Long.valueOf(roleIds[i]));
            iSysUserRoleService.save(sysUserRole);
        }
        return AxiosResult.success();
    }

    @PutMapping("update")
    public AxiosResult update(@RequestBody SysUser sysUser) {
        String[] idsStr = sysUser.getRoleIds().split("A");
//        先删除用户_角色中间表，该用户所有的角色，再添加角色
        iSysUserRoleService.remove(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, sysUser.getUserId()));
        Arrays.stream(idsStr).forEach(id -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getUserId());
            sysUserRole.setRoleId(Long.valueOf(id));
            iSysUserRoleService.save(sysUserRole);
        });
        iSysUserService.updateById(sysUser);
        return AxiosResult.success();
    }

    @DeleteMapping("{id}")
    public AxiosResult delete(@PathVariable Serializable id) {
        //        你不能真正的删除用户，只能修改删除标志
//        iSysUserService.removeById(id);
        SysUser byId = iSysUserService.getById(id);
//        下面应该写在service中的同一个方法里面，才有事务
        byId.setDelFlag(2);
        iSysUserService.updateById(byId);
        return AxiosResult.success();
    }

    @DeleteMapping("{userId}/role/{roleId}")
    public AxiosResult deleteRoleByRoleIdAndUserId(@PathVariable Serializable userId, @PathVariable Serializable roleId) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUserRole::getUserId, userId).eq(SysUserRole::getRoleId, roleId);
        iSysUserRoleService.remove(wrapper);
        return AxiosResult.success();
    }
}
