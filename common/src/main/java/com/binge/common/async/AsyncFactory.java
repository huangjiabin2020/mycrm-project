package com.binge.common.async;

import com.binge.common.mail.EmailSender;
import com.binge.common.spring.GetBean;
import com.binge.common.utils.ServletUtils;
import com.binge.entity.SysLoginLog;
import com.binge.service.ISysLoginLogService;

/**
 * @author JiaBin Huang
 * @date 2020/10/24
 **/
public class AsyncFactory {
    public static Runnable executeLoginLog(SysLoginLog log) {

        return () -> {
//        获取登录地址需要调用外网，耗时长，所以放到异步任务中去做
            log.setLoginLocation(ServletUtils.getLoginLocation(log.getIpaddr()));
            log.setStatus(log.getStatus());
            log.setMsg(log.getMsg());
//            注意不要循环依赖
            ISysLoginLogService iSysLoginLogService = GetBean.getBean(ISysLoginLogService.class);
            iSysLoginLogService.save(log);
        };
    }

    /**
     * 生产发送邮件的Runnable
     *
     * @param userEmailAddr
     * @param htmlStr
     * @return
     */
    public static Runnable executeEmail(String userEmailAddr, String htmlStr) {
        return () -> {
            EmailSender emailSender = GetBean.getBean(EmailSender.class);
            emailSender.sendEmail(userEmailAddr, htmlStr);
        };
    }


}
