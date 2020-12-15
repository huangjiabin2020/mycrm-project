package com.binge.entity;

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
public class BaseGood implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品编码 采用UUId
     */
    private String goodCode;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 商品型号
     */
    private String goodModel;

    /**
     * 类型id
     */
    private Integer typeId;

    /**
     * 商品单位
     */
    private String goodUnit;

    /**
     * 采购价格 成本价 如果价格有浮动 则取平均值
     */
    private Double purchasingPrice;

    /**
     * 上次采购价格
     */
    private Double lastPurchasingPrice;

    /**
     * 售价
     */
    private Double sellPrice;

    /**
     * 库存数量
     */
    private Integer storeNum;

    /**
     * 库存下限
     */
    private Integer minStoreNum;

    /**
     * 0 表示初始化状态 1表示期初库存入仓库 2 表示有进货或者出库单据
     */
    private Integer state;

    /**
     * 生产厂商
     */
    private String producer;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 自己加的字段，为了前端展示
     */
    private transient String typeName;

    /**
     * 自己加的字段，为了前端展示
     */
    private transient String stateName;


    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodModel() {
        return goodModel;
    }

    public void setGoodModel(String goodModel) {
        this.goodModel = goodModel;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getGoodUnit() {
        return goodUnit;
    }

    public void setGoodUnit(String goodUnit) {
        this.goodUnit = goodUnit;
    }

    public Double getPurchasingPrice() {
        return purchasingPrice;
    }

    public void setPurchasingPrice(Double purchasingPrice) {
        this.purchasingPrice = purchasingPrice;
    }

    public Double getLastPurchasingPrice() {
        return lastPurchasingPrice;
    }

    public void setLastPurchasingPrice(Double lastPurchasingPrice) {
        this.lastPurchasingPrice = lastPurchasingPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(Integer storeNum) {
        this.storeNum = storeNum;
    }

    public Integer getMinStoreNum() {
        return minStoreNum;
    }

    public void setMinStoreNum(Integer minStoreNum) {
        this.minStoreNum = minStoreNum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "BaseGood{" +
                "goodCode=" + goodCode +
                ", goodName=" + goodName +
                ", goodModel=" + goodModel +
                ", typeId=" + typeId +
                ", goodUnit=" + goodUnit +
                ", purchasingPrice=" + purchasingPrice +
                ", lastPurchasingPrice=" + lastPurchasingPrice +
                ", sellPrice=" + sellPrice +
                ", storeNum=" + storeNum +
                ", minStoreNum=" + minStoreNum +
                ", state=" + state +
                ", producer=" + producer +
                ", remarks=" + remarks +
                "}";
    }
}
