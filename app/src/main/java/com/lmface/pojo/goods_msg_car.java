package com.lmface.pojo;

import java.util.Date;

public class goods_msg_car {
    private Integer goodsid;

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

    private String collegename;

    private String goodsclassification;

    private String speciesname;

    private String campusname;

    private Integer carGoodsNum;

    private Integer carId;

    private String courierName;

    private Integer courierMoney;

    private String nickname;

    private String userName;

    private Integer userId;

    @Override
    public String toString() {
        return "goods_msg_car{" +
                "goodsid=" + goodsid +
                ", goodsname='" + goodsname + '\'' +
                ", goodsdetails='" + goodsdetails + '\'' +
                ", goodsimgaddress1='" + goodsimgaddress1 + '\'' +
                ", goodsimgaddress3='" + goodsimgaddress3 + '\'' +
                ", goodsimgaddress2='" + goodsimgaddress2 + '\'' +
                ", userphonenum='" + userphonenum + '\'' +
                ", goodsprice=" + goodsprice +
                ", shelvestime=" + shelvestime +
                ", goodscity='" + goodscity + '\'' +
                ", goodsnum=" + goodsnum +
                ", collegename='" + collegename + '\'' +
                ", goodsclassification='" + goodsclassification + '\'' +
                ", speciesname='" + speciesname + '\'' +
                ", campusname='" + campusname + '\'' +
                ", carGoodsNum=" + carGoodsNum +
                ", carId=" + carId +
                ", courierName='" + courierName + '\'' +
                ", courierMoney=" + courierMoney +
                ", nickname='" + nickname + '\'' +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                '}';
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
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

    public String getCollegename() {
        return collegename;
    }

    public void setCollegename(String collegename) {
        this.collegename = collegename == null ? null : collegename.trim();
    }

    public String getGoodsclassification() {
        return goodsclassification;
    }

    public void setGoodsclassification(String goodsclassification) {
        this.goodsclassification = goodsclassification == null ? null : goodsclassification.trim();
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

    public Integer getCarGoodsNum() {
        return carGoodsNum;
    }

    public void setCarGoodsNum(Integer carGoodsNum) {
        this.carGoodsNum = carGoodsNum;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName == null ? null : courierName.trim();
    }

    public Integer getCourierMoney() {
        return courierMoney;
    }

    public void setCourierMoney(Integer courierMoney) {
        this.courierMoney = courierMoney;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}