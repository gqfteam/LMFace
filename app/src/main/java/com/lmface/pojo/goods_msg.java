package com.lmface.pojo;

import java.sql.Timestamp;

public class goods_msg {
    private Integer goodsid;

    private Integer userId;

    private String goodsname;

    private String goodsdetails;

    private String goodsimgaddress1;

    private String goodsimgaddress3;

    private String goodsimgaddress2;

    private String userphonenum;

    private Double goodsprice;

    private Timestamp shelvestime;

    private String goodscity;

    private Integer goodsnum;

    private String speciesname;

    private String campusname;

    private String goodsclassification;

    private String collegename;

    private String courierName;

    private Integer courierMoney;
    
    
    
    public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public Integer getCourierMoney() {
		return courierMoney;
	}

	public void setCourierMoney(Integer courierMoney) {
		this.courierMoney = courierMoney;
	}

	public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

   

    public Timestamp getShelvestime() {
		return shelvestime;
	}

	public void setShelvestime(Timestamp shelvestime) {
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
		return "goods_msg [goodsid=" + goodsid + ", userId=" + userId
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