package com.binge.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.stereotype.Component;

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
 * @date 2020年 10月27日
 **/
@Component
public class JsonUtils {

    private ObjectMapper objectMapper = new ObjectMapper();

    public String obj2Str(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        String s = "";
        try {
            s = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public <T> T str2Obj(String jsonStr, Class<T> t) {
        try {
            return objectMapper.readValue(jsonStr, t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> str2List(String listJsonStr, Class<T> t) {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, t);
        try {
            return objectMapper.readValue(listJsonStr, collectionType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 字符串转Map
     * */

    public <T, K> Map<T, K> str2Map(String mapJsonStr, Class<T> keyClass, Class<K> valueClass) {
        MapType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        try {
            Map<T, K> map = objectMapper.readValue(mapJsonStr, mapType);
            return map;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
