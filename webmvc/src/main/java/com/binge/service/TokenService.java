package com.binge.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.binge.common.bean.LoginUser;
import com.binge.common.http.AxiosStatus;
import com.binge.common.utils.RedisUtils;
import com.binge.common.utils.ServletUtils;
import com.binge.exception.JwtAuthorizationException;
import com.binge.exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

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
 * @date 2020年 10月27日
 **/
@Component
public class TokenService {
    /**
     * 距离到期时间的检查时间间隔
     */
    public static final int GapTIme = 15 * 1000;
    @Autowired
    RedisUtils redisUtils;

    private static final String SECRET = UUID.randomUUID().toString().replaceAll("-", "");
    public static final String ISSUER = "auth0";
    /**
     * 3天后过期
     */
    private static long expireTime = 1000 * 30 * 60;

    public String createToken(String uuid) {
        String token;
//        try {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        token = JWT.create()
                .withIssuer(ISSUER)
                .withClaim("uuid", uuid)
                .sign(algorithm);
//        } catch (JWTCreationException exception){
//            //Invalid Signing configuration / Couldn't convert Claims.
//        }
        return token;
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }

    /**
     * 更新到期时间 和 上一次操作时间
     *
     * @param loginUser
     */
    public void setExpireTimeAndCacheUser(LoginUser loginUser) {
//        更新上一次操作时间
        loginUser.setPreviousOperTime(System.currentTimeMillis());
//        更新token过期时间
        loginUser.setExpireTime(System.currentTimeMillis() + expireTime);

    }

    /**
     * 用户名，密码验证通过, 创建token，并将loginuser对象存到redis
     *
     * @param loginUser
     * @return
     */
    public String createTokenAndCacheLoginUser(LoginUser loginUser) {
//        更新登录时间（因为登录成功就调这个函数）
        loginUser.setLoginTime(System.currentTimeMillis());

//      更新到期时间 和 上一次操作时间
        setExpireTimeAndCacheUser(loginUser);
//        创建UUID
        loginUser.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
//        将更新后的数据写入redis
        redisUtils.setObj(loginUser.getUuid(), loginUser, loginUser.getExpireTime());
        return createToken(loginUser.getUuid());
    }


    /**
     * 获取LoginUser
     *
     * @return
     */
    public LoginUser getLoginUser() {
        String uuid = getUuidByToken(getToken());
        if (StringUtils.isEmpty(uuid)) {
            throw new LoginException(AxiosStatus.TOKEN_NOT_FOUND);
        }
        try {
            LoginUser loginUser = redisUtils.getObj(uuid, LoginUser.class);
            return loginUser;
//        redis可能清空了，或者设置了超时时间自动删除了数据，
        } catch (Exception e) {
            throw new LoginException(AxiosStatus.TOKEN_NOT_FOUND);
        }


    }

    /**
     * 最终是为了获取LoginUser
     *
     * @param token
     * @return
     */
    public String getUuidByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new LoginException(AxiosStatus.TOKEN_NOT_FOUND);
        }
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim("uuid").asString();
    }

    /**
     * 最终是为了获取LoginUser
     *
     * @return
     */
    public String getToken() {
        String authorization = ServletUtils.getRequest().getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            return "";
        }
        return authorization.split(" ")[1];
    }

    /**
     * 从JwtInteceptor中抽取出逻辑
     *
     * @return
     */
    public boolean tokenAuthorization() {
        boolean flag = true;
        try {
            String authorization = ServletUtils.getRequest().getHeader("Authorization");
            flag &= (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer"));
            flag &= (verifyToken(authorization.split(" ")[1]) != null);
        } catch (Exception e) {
            throw new JwtAuthorizationException(AxiosStatus.TOKEN_WRONG);
        }
        //        验证(如果有必要，刷新)过期时间
        boolean b1 = authorizationTime();
        return flag & b1;
    }

    /**
     * 验证是否超时
     *
     * @return
     */
    public boolean authorizationTime() {
//        拿到上一次操作产生的过期时间
        LoginUser loginUser = getLoginUser();
        long expireTime = loginUser.getExpireTime();
//        已经超过过期时间
        if (expireTime - System.currentTimeMillis() < 0) {
            System.err.println("已经超过过期时间");
            return false;
        }
//        如果当前时间距离过期时间小于一天，才用新的过期时间替换旧的过期时间（同时替换上一次操作时间）
        if (expireTime - System.currentTimeMillis() < GapTIme) {
            setExpireTimeAndCacheUser(loginUser);
//            同时更新redis
            redisUtils.setObj(loginUser.getUuid(), loginUser, loginUser.getExpireTime());
            System.err.println("redis更新了过期时间");
            return true;
        }
//        当前时间距离过期时间还早的很，不必频繁刷新操作时间
        System.err.println("  当前时间距离过期时间还早的很，不必频繁刷新操作时间");
        return true;
    }

    /**
     * 清除当前请求的用户在redis的缓存数据
     */
    public void removeLoginUser() {
        redisUtils.removeObj(getUuidByToken(getToken()));
    }


}
