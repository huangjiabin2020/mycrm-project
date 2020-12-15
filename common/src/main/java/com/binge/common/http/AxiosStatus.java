package com.binge.common.http;

/**
 * @author JiaBin Huang
 * @date 2020/10/24
 **/
public enum AxiosStatus {
    OK(20000, "操作成功"), ERROR(50000, "操作失败"),
    EXT_ERROR(30000, "上传格式不支持"),
    FILE_TOOLONG(30001, "文件太大"),
    NOT_IMAGE(30002, "上传的不是图片"),
    VALID_FAILURE(30003, "表单验证失败"),
    USERNAME_EMPTY(30004, "用户名不能为空！"),
    USER_NOT_FOUND(30005, "找不到该用户！"),
    USER_STATUS_WRONG(30006, "用户账号已经停用"),
    PASSWORD_EMPTY(30007, "密码不能为空！"),
    PASSWORD_WRONG(30008, "密码不对！"),

    TOKEN_WRONG(44444, "TOKEN不对！"),
    TOKEN_EXPIRED(30010, "TOKEN过期！"),
    TOKEN_NOT_FOUND(30023, "TOKEN不存在！"),
    PRIVILEGE_ERROR(30011, "权限不足！"),

    WEB_ERROR(55555, "前端错误");
    private int status;
    private String message;

    AxiosStatus() {
    }

    AxiosStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
