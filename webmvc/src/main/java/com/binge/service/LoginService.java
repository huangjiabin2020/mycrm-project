package com.binge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.binge.common.async.AsyncFactory;
import com.binge.common.async.AsyncManager;
import com.binge.common.bean.LoginUser;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.AxiosStatus;
import com.binge.common.utils.RedisUtils;
import com.binge.common.utils.ServletUtils;
import com.binge.entity.SysLoginLog;
import com.binge.entity.SysMenu;
import com.binge.entity.SysUser;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
@Component
public class LoginService {
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    RedisUtils redisUtils;

    public AxiosResult doLogin(String userName, String password) {
        //        根据用户名获取用户
        SysUser oneUser = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUserName, userName));
        if (oneUser == null) {
            return AxiosResult.error(AxiosStatus.USER_NOT_FOUND);
        }
        //        开始记录日志
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(oneUser);
        SysLoginLog log = new SysLoginLog();

        String realIp = ServletUtils.getRealIp();
        log.setUserName(loginUser.getSysUser().getUserName());
        log.setIpaddr(realIp);
        log.setLoginTime(LocalDateTime.now());

        String header = ServletUtils.getRequest().getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        log.setBrowser(userAgent.getBrowser().getName());
        log.setOs(userAgent.getOperatingSystem().getName());

        if (ObjectUtils.isEmpty(userName)) {
//            记录日志操作放到异步任务
            log.setStatus("1");
            log.setMsg(AxiosStatus.USERNAME_EMPTY.getMessage());
            //        获取登录地址需要调用外网，耗时长，所以放到异步任务中去做
            AsyncManager.getInstance().executeTask(AsyncFactory.executeLoginLog(log));
            return AxiosResult.error(AxiosStatus.USERNAME_EMPTY);
        }
        if (ObjectUtils.isEmpty(password)) {
            log.setStatus("1");
            log.setMsg(AxiosStatus.PASSWORD_EMPTY.getMessage());
            AsyncManager.getInstance().executeTask(AsyncFactory.executeLoginLog(log));
            return AxiosResult.error(AxiosStatus.PASSWORD_EMPTY);
        }

        if (!oneUser.getStatus()) {
            log.setStatus("1");
            log.setMsg(AxiosStatus.USER_STATUS_WRONG.getMessage());
            AsyncManager.getInstance().executeTask(AsyncFactory.executeLoginLog(log));
            return AxiosResult.error(AxiosStatus.USER_STATUS_WRONG);
        }
//        验证密码
        if (!bCryptPasswordEncoder.matches(password, oneUser.getPassword())) {
            log.setStatus("1");
            log.setMsg(AxiosStatus.PASSWORD_WRONG.getMessage());
            AsyncManager.getInstance().executeTask(AsyncFactory.executeLoginLog(log));
            return AxiosResult.error(AxiosStatus.PASSWORD_WRONG);
        }

//        用户名，密码验证通过
        log.setStatus("0");
        log.setMsg(AxiosStatus.OK.getMessage());
//        记录登录日志
        AsyncManager.getInstance().executeTask(AsyncFactory.executeLoginLog(log));

//        准备好token，同时将用户信息封装到LoginUser对象，保存到redis中

        String token = tokenService.createTokenAndCacheLoginUser(loginUser);

        return AxiosResult.success(token);
    }

    public Map<String, Object> getUserInfo() {
        LoginUser loginUser = tokenService.getLoginUser();
        Long userId = loginUser.getSysUser().getUserId();

        List<SysMenu> router = iSysUserService.findUserRouterByUserId(userId);
        List<SysMenu> perm = iSysUserService.findUserBtnPermByUserId(userId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("router", router);
        map.put("perm", perm);
        map.put("user", loginUser.getSysUser());
//        返回给前端的同时，保存按钮权限到redis
        loginUser.setPerms(perm);
        redisUtils.setObj(loginUser.getUuid(), loginUser);
        return map;
    }

    /**
     * 根据header获取uuid，从而获取用户，让这个用户下线
     */
    public void doLogout() {
        tokenService.removeLoginUser();
    }
}
