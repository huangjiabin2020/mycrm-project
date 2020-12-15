package com.binge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author binge
 * @since 2020-10-24
 */
@Data

public class BaseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 父级id
     */
    private Integer pId;
    /**
     * 父类名称，自己添加的，不属于表的字段
     */
    private transient String pname;

    /**
     * 孩子类型
     */
    private transient List<BaseCategory> children;


}
