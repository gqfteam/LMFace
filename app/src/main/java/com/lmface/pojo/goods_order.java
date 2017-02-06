package com.lmface.pojo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class goods_order {
    private Integer orderId;

    private Integer userId;

    private Integer storeUserId;

    private Timestamp orderTime;

    private BigDecimal orderPrice;

    private Integer orderGoodsNum;

    private Integer userAddressId;

    private Integer orderStatus;

    private Integer goodsId;

    private String courierNum;

    
    private Integer courierId;
    
    
    
    public goods_order(){}
    
    
    
    public goods_order(Integer orderId,Integer userId, Integer storeUserId,
    		BigDecimal orderPrice, Integer orderGoodsNum,
			Integer userAddressId, Integer orderStatus, Integer goodsId) {
		super();
		this.orderId=orderId;
		this.userId = userId;
		this.storeUserId = storeUserId;
		this.orderPrice = orderPrice;
		this.orderGoodsNum = orderGoodsNum;
		this.userAddressId = userAddressId;
		this.orderStatus = orderStatus;
		this.goodsId = goodsId;
	}
    public goods_order(Integer userId, Integer storeUserId,
			Timestamp orderTime, BigDecimal orderPrice, Integer orderGoodsNum,
			Integer userAddressId, Integer orderStatus, Integer goodsId) {
		super();
		this.userId = userId;
		this.storeUserId = storeUserId;
		this.orderTime = orderTime;
		this.orderPrice = orderPrice;
		this.orderGoodsNum = orderGoodsNum;
		this.userAddressId = userAddressId;
		this.orderStatus = orderStatus;
		this.goodsId = goodsId;
	}

	

	



	public goods_order( Integer userId,  Integer storeUserId,Timestamp orderTime,
			BigDecimal orderPrice,  Integer orderGoodsNum,
			  Integer userAddressId,Integer orderStatus,  Integer goodsId,  Integer courierId) {
		super();
		this.userId = userId;
		this.storeUserId = storeUserId;
		this.orderTime = orderTime;
		this.orderPrice = orderPrice;
		this.orderGoodsNum = orderGoodsNum;
		this.userAddressId = userAddressId;
		this.orderStatus = orderStatus;
		this.goodsId = goodsId;
		this.courierId = courierId;
	}



	public Integer getCourierId() {
		return courierId;
	}



	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}



	public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(Integer storeUserId) {
        this.storeUserId = storeUserId;
    }

   

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getOrderGoodsNum() {
        return orderGoodsNum;
    }

    public void setOrderGoodsNum(Integer orderGoodsNum) {
        this.orderGoodsNum = orderGoodsNum;
    }

    public Integer getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(Integer userAddressId) {
        this.userAddressId = userAddressId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getCourierNum() {
        return courierNum;
    }

    public void setCourierNum(String courierNum) {
        this.courierNum = courierNum == null ? null : courierNum.trim();
    }

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	@Override
	public String toString() {
		return "goods_order [orderId=" + orderId + ", userId=" + userId
				+ ", storeUserId=" + storeUserId + ", orderTime=" + orderTime
				+ ", orderPrice=" + orderPrice + ", orderGoodsNum="
				+ orderGoodsNum + ", userAddressId=" + userAddressId
				+ ", orderStatus=" + orderStatus + ", goodsId=" + goodsId
				+ ", courierNum=" + courierNum + "]";
	}
    
}