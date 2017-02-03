package com.lmface.network.api;

import com.lmface.pojo.ResultCode;
import com.lmface.pojo.goods_msg_car;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by johe on 2017/1/20.
 */

public interface ShopCarService
{
    //查询用户的购物车
    @GET("ShopCar/selectShopCarByUserId/{user_id}")
    Observable<List<goods_msg_car>> selectShopCarByUserId(@Path("user_id")int user_id);

    //增加购物车
    @FormUrlEncoded
    @POST("ShopCar/insertShopCar")
    Observable<ResultCode> insertShopCar(@Field("userId") int userId,@Field("goodsId") int goodsId,@Field("carGoodsNum") int carGoodsNum);

    //删除购物车
    @FormUrlEncoded
    @POST("ShopCar/delectShopCar")
    Observable<ResultCode> delectShopCar(@Field("car_id") int car_id);

    //批量删除购物车
    @FormUrlEncoded
    @POST("ShopCar/delectShopCarByListCarId")
    Observable<ResultCode> delectShopCarByListCarId(@Field("shopCarIds")List<Integer> shopCarIds);

    //修改购物车商品数量
    @FormUrlEncoded
    @POST("ShopCar/updateShopCarNum")
    Observable<ResultCode> updateShopCarNum(@Field("car_id") int car_id,@Field("car_goods_num") int car_goods_num);

    //批量修改购物车商品数量
    @FormUrlEncoded
    @POST("ShopCar/updateListShopCarNum")
    Observable<ResultCode> updateListShopCarNum(@Field("ShopCarMsgs")String ShopCarMsgs);

}
