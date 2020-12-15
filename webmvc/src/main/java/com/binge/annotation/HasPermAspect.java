package com.binge.annotation;

import com.binge.common.bean.LoginUser;
import com.binge.common.http.AxiosResult;
import com.binge.common.http.AxiosStatus;
import com.binge.common.utils.JsonUtils;
import com.binge.common.utils.ServletUtils;
import com.binge.entity.SysMenu;
import com.binge.exception.PrivilegeException;
import com.binge.service.TokenService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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
 * @date 2020年 10月28日
 **/
@Aspect
@Component
public class HasPermAspect {
    @Autowired
    TokenService tokenService;
    @Autowired
    JsonUtils jsonUtils;

    @Pointcut("@annotation(com.binge.annotation.HasPerm)")
    public void myPointcut() {
    }


    @Before(value = "myPointcut()")
    public void beforePerm(JoinPoint joinPoint) {
        System.err.println("精准切入自定义注解...");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        HasPerm hasPerm = method.getDeclaredAnnotation(HasPerm.class);
        if (hasPerm != null) {
//            需要的权限数组
            String[] needPerms = hasPerm.value();
//     todo 小bug      这里其实是从redis中获取的，即：如果权限被修改了，而redis没有及时刷新，进行权限认证的就还是之前的权限

            LoginUser loginUser = tokenService.getLoginUser();
//            已有的权限数组
            List<SysMenu> ownPerms = loginUser.getPerms();

//            判断 需要的权限数组 是否 完全被 已有的权限数组 包含
//            即：只要有一个不包含，就false
            Arrays.stream(needPerms).forEach(item -> {
                boolean flag = false;
                for (int i = 0; i < ownPerms.size(); i++) {
                    if (ownPerms.get(i).getPerms().equalsIgnoreCase(item)) {
                        flag = true;
                    }
                }
                if (!flag) {
//                    说明权限不足，返回前端相关状态码
//                    如果抛异常，就要在异常通知、最终通知进行捕获，
                    throw new PrivilegeException(AxiosStatus.PRIVILEGE_ERROR);
//                    ServletUtils.returnJsonStr(jsonUtils.obj2Str(AxiosResult.error(AxiosStatus.PRIVILEGE_ERROR)));
                }
            });

        }
    }
}
