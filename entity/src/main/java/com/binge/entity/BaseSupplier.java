package com.binge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@Data
public class BaseSupplier implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 供应商位置
     */
    private String address;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 供应商公司名称
     */
    private String name;

    /**
     * 联系方式
     */
    private String phone;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "BaseSupplier{" +
                "id=" + id +
                ", address=" + address +
                ", contact=" + contact +
                ", name=" + name +
                ", phone=" + phone +
                ", remark=" + remark +
                "}";
    }
}
