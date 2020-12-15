package com.binge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.binge.common.valid.annotaion.SexValues;
import com.binge.common.valid.group.AddGroup;
import com.binge.common.valid.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    //    分组校验，添加时id必须为空
    @Null(groups = {AddGroup.class}, message = "分组校验，添加时id必须为空")
//    分组校验，修改时id必须非空
    @NotNull(groups = {UpdateGroup.class}, message = "分组校验，修改时id必须非空")
    private Long userId;

    /**
     * 用户账号
     */
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_]{5,19}", message = "不符合6~20位，字母开头，仅含字母下划线数字", groups = {AddGroup.class, UpdateGroup.class})
    //6~10位，字母开头，仅含字母下划线数字
    private String userName;

    /**
     * 用户昵称
     */
    @NotBlank(message = "你丫的昵称没传值", groups = {AddGroup.class, UpdateGroup.class})
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}", groups = {AddGroup.class, UpdateGroup.class})
    private String phone;

    /**
     * 用户性别（0男 1女 2未知）
     */
    //    使用自定义注解，表示sex只能是0或1
    @SexValues(values = {"0", "1"}, groups = {AddGroup.class, UpdateGroup.class})
    private String sex;

    /**
     * 头像地址
     */
    @URL(message = "必须是URL地址", groups = {AddGroup.class, UpdateGroup.class})
    @NotBlank(message = "头像地址不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态（1正常 0停用）
     */
    private Boolean status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对应的角色数组
     */
    private transient String roleIds;

    /**
     * jwt令牌
     */
    private transient String token;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "userId=" + userId +
                ", userName=" + userName +
                ", nickName=" + nickName +
                ", email=" + email +
                ", phone=" + phone +
                ", sex=" + sex +
                ", avatar=" + avatar +
                ", password=" + password +
                ", status=" + status +
                ", delFlag=" + delFlag +
                ", remark=" + remark +
                "}";
    }
}
