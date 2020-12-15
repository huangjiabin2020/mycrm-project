package com.binge.login;

import com.binge.common.http.AxiosResult;
import com.binge.entity.SysUser;
import com.binge.service.LoginService;
import com.binge.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖保佑             永无BUG
 * //          佛曰:
 * //                  写字楼里写字间，写字间里程序员；
 * //                  程序人员写程序，又拿程序换酒钱。
 * //                  酒醒只在网上坐，酒醉还来网下眠；
 * //                  酒醉酒醒日复日，网上网下年复年。
 * //                  但愿老死电脑间，不愿鞠躬老板前；
 * //                  奔驰宝马贵者趣，公交自行程序员。
 * //                  别人笑我忒疯癫，我笑自己命太贱；
 * //                  不见满街漂亮妹，哪个归得程序员？
 *
 * @author JiaBin Huang
 * @date 2020年 10月26日
 **/
@RestController
@RequestMapping("login")
public class LoginController {


    @Autowired
    LoginService loginService;
    @Autowired
    TokenService tokenService;

    @PostMapping
    public AxiosResult doLogin(@RequestBody SysUser sysUser) {

        AxiosResult axiosResult = loginService.doLogin(sysUser.getUserName(), sysUser.getPassword());

        return axiosResult;
    }

    /**
     * 获得用户信息 动态菜单 按钮权限
     *
     * @return
     */
    @GetMapping("getUserInfo")
    public AxiosResult getUserInfo() {

        Map<String, Object> userInfo = loginService.getUserInfo();


        return AxiosResult.success(userInfo);
    }


    @DeleteMapping
    public AxiosResult logout() {
        loginService.doLogout();
        return AxiosResult.success();
    }


}
