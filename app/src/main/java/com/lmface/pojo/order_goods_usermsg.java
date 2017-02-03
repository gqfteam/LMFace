package com.lmface.pojo;

import java.sql.Timestamp;
import java.util.Date;

public class order_goods_usermsg {
    private Integer orderId;

    private Integer userId;

    private Integer storeUserId;

    private Timestamp orderTime;

    private Integer orderPrice;

    private Integer orderGoodsNum;

    private Integer userAddressId;

    private Integer orderStatus;

    private Integer goodsId;

    private String courierNum;

    private String userphone;

    private String usernickname;

    private String storeusernickname;

    private String storeuserphone;

    private String goodsname;

    private String goodsdetails;

    private String goodsimgaddress1;

    private String goodsimgaddress3;

    private String goodsimgaddress2;

    private String userphonenum;

    private Double goodsprice;

    private Date shelvestime;

    private String goodscity;

    private Integer goodsnum;

    private String speciesname;

    private String campusname;

    private String goodsclassification;

    private String collegename;

    private Integer courierMoney;

    private String courierName;
    
    public Integer getCourierMoney() {
		return courierMoney;
	}

	public void setCourierMoney(Integer courierMoney) {
		this.courierMoney = courierMoney;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
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

    
    public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Integer orderPrice) {
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

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone == null ? null : userphone.trim();
    }

    public String getUsernickname() {
        return usernickname;
    }

    public void setUsernickname(String usernickname) {
        this.usernickname = usernickname == null ? null : usernickname.trim();
    }

    public String getStoreusernickname() {
        return storeusernickname;
    }

    public void setStoreusernickname(String storeusernickname) {
        this.storeusernickname = storeusernickname == null ? null : storeusernickname.trim();
    }

    public String getStoreuserphone() {
        return storeuserphone;
    }

    public void setStoreuserphone(String storeuserphone) {
        this.storeuserphone = storeuserphone == null ? null : storeuserphone.trim();
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname == null ? null : goodsname.trim();
    }

    public String getGoodsdetails() {
        return goodsdetails;
    }

    public void setGoodsdetails(String goodsdetails) {
        this.goodsdetails = goodsdetails == null ? null : goodsdetails.trim();
    }

    public String getGoodsimgaddress1() {
        return goodsimgaddress1;
    }

    public void setGoodsimgaddress1(String goodsimgaddress1) {
        this.goodsimgaddress1 = goodsimgaddress1 == null ? null : goodsimgaddress1.trim();
    }

    public String getGoodsimgaddress3() {
        return goodsimgaddress3;
    }

    public void setGoodsimgaddress3(String goodsimgaddress3) {
        this.goodsimgaddress3 = goodsimgaddress3 == null ? null : goodsimgaddress3.trim();
    }

    public String getGoodsimgaddress2() {
        return goodsimgaddress2;
    }

    public void setGoodsimgaddress2(String goodsimgaddress2) {
        this.goodsimgaddress2 = goodsimgaddress2 == null ? null : goodsimgaddress2.trim();
    }

    public String getUserphonenum() {
        return userphonenum;
    }

    public void setUserphonenum(String userphonenum) {
        this.userphonenum = userphonenum == null ? null : userphonenum.trim();
    }

    public Double getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(Double goodsprice) {
        this.goodsprice = goodsprice;
    }

    public Date getShelvestime() {
        return shelvestime;
    }

    public void setShelvestime(Date shelvestime) {
        this.shelvestime = shelvestime;
    }

    public String getGoodscity() {
        return goodscity;
    }

    public void setGoodscity(String goodscity) {
        this.goodscity = goodscity == null ? null : goodscity.trim();
    }

    public Integer getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(Integer goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getSpeciesname() {
        return speciesname;
    }

    public void setSpeciesname(String speciesname) {
        this.speciesname = speciesname == null ? null : speciesname.trim();
    }

    public String getCampusname() {
        return campusname;
    }

    public void setCampusname(String campusname) {
        this.campusname = campusname == null ? null : campusname.trim();
    }

    public String getGoodsclassification() {
        return goodsclassification;
    }

    public void setGoodsclassification(String goodsclassification) {
        this.goodsclassification = goodsclassification == null ? null : goodsclassification.trim();
    }

    public String getCollegename() {
        return collegename;
    }

    public void setCollegename(String collegename) {
        this.collegename = collegename == null ? null : collegename.trim();
    }

	@Override
	public String toString() {
		return "order_goods_usermsg [orderId=" + orderId + ", userId=" + userId
				+ ", storeUserId=" + storeUserId + ", orderTime=" + orderTime
				+ ", orderPrice=" + orderPrice + ", orderGoodsNum="
				+ orderGoodsNum + ", userAddressId=" + userAddressId
				+ ", orderStatus=" + orderStatus + ", goodsId=" + goodsId
				+ ", courierNum=" + courierNum + ", userphone=" + userphone
				+ ", usernickname=" + usernickname + ", storeusernickname="
				+ storeusernickname + ", storeuserphone=" + storeuserphone
				+ ", goodsname=" + goodsname + ", goodsdetails=" + goodsdetails
				+ ", goodsimgaddress1=" + goodsimgaddress1
				+ ", goodsimgaddress3=" + goodsimgaddress3
				+ ", goodsimgaddress2=" + goodsimgaddress2 + ", userphonenum="
				+ userphonenum + ", goodsprice=" + goodsprice
				+ ", shelvestime=" + shelvestime + ", goodscity=" + goodscity
				+ ", goodsnum=" + goodsnum + ", speciesname=" + speciesname
				+ ", campusname=" + campusname + ", goodsclassification="
				+ goodsclassification + ", collegename=" + collegename + "]";
	}
    
}