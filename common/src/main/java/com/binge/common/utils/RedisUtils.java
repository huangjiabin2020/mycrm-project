package com.binge.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
public class RedisUtils {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    JsonUtils jsonUtils;

    public void setObj(String key, Object obj) {
        String str = jsonUtils.obj2Str(obj);
        redisTemplate.opsForValue().set(key, str);
    }

    public void setObj(String key, Object obj, long expireTime) {
        String str = jsonUtils.obj2Str(obj);
        redisTemplate.opsForValue().set(key, str, expireTime, TimeUnit.MILLISECONDS);
    }

    public <T> T getObj(String key, Class<T> clazz) {
        String str = redisTemplate.opsForValue().get(key);
        return jsonUtils.str2Obj(str, clazz);
    }


    public void removeObj(String key) {
        redisTemplate.delete(key);
    }


}
