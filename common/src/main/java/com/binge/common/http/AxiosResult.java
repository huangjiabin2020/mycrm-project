package com.binge.common.http;

import java.util.HashMap;

/**
 * @author JiaBin Huang
 * @date 2020/10/24
 **/
public class AxiosResult extends HashMap<String, Object> {
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String DATA = "data";


    public AxiosResult(AxiosStatus axiosStatus) {
        put(STATUS, axiosStatus.getStatus());
        put(MESSAGE, axiosStatus.getMessage());
    }

    public static AxiosResult success() {
        return new AxiosResult(AxiosStatus.OK);
    }

    public static AxiosResult success(AxiosStatus axiosStatus) {
        return new AxiosResult(axiosStatus);
    }

    public static AxiosResult success(Object data) {
        AxiosResult success = AxiosResult.success();
        success.put(DATA, data);
        return success;
    }


    public static AxiosResult error() {
        return new AxiosResult(AxiosStatus.ERROR);
    }

    public static AxiosResult error(AxiosStatus axiosStatus) {
        return new AxiosResult(axiosStatus);
    }

    public static AxiosResult error(Object data) {
        AxiosResult error = AxiosResult.error();
        error.put(DATA, data);
        return error;
    }


}
