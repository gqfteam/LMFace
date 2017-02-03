package com.lmface.pojo;

public class shop_car {
    private Integer carId;

    private Integer userId;

    private Integer goodsId;

    private Integer carGoodsNum;

    
    public shop_car(){}
    public shop_car(Integer userId, Integer goodsId, Integer carGoodsNum) {
		super();
		this.userId = userId;
		this.goodsId = goodsId;
		this.carGoodsNum = carGoodsNum;
	}

	public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCarGoodsNum() {
        return carGoodsNum;
    }

    public void setCarGoodsNum(Integer carGoodsNum) {
        this.carGoodsNum = carGoodsNum;
    }

	@Override
	public String toString() {
		return "shop_car [carId=" + carId + ", userId=" + userId + ", goodsId="
				+ goodsId + ", carGoodsNum=" + carGoodsNum + "]";
	}
    
    
}