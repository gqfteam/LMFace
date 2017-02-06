package com.lmface.network.api;

import com.lmface.pojo.ResultCode;
import com.lmface.pojo.order_goods_usermsg;

import java.math.BigDecimal;
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

public interface GoodsOrderService {

    //新增订单订单状态为00
    @FormUrlEncoded
    @POST("GoodsOrder/insertOrder")
    Observable<ResultCode> insertOrder(@Field("userId") Integer userId, @Field("storeUserId")  Integer storeUserId,
                                       @Field("orderPrice")  BigDecimal orderPrice, @Field("orderGoodsNum")  Integer orderGoodsNum,
                                       @Field("userAddressId")  Integer userAddressId, @Field("goodsId")  Integer goodsId, @Field("courierId")  Integer courierId);

    //批量新增订单状态为00
    @FormUrlEncoded
    @POST("GoodsOrder/insertOrders")
    Observable<ResultCode> insertOrders(@Field("orderJsons") String orderJsons );

    //修改订单只能修改订单状态为00的订单
    @FormUrlEncoded
    @POST("GoodsOrder/updateOrder")
    Observable<ResultCode> updateOrder(@Field("orderId") Integer orderId, @Field("userId") Integer userId, @Field("storeUserId")  Integer storeUserId,
                                       @Field("orderPrice") BigDecimal orderPrice, @Field("orderGoodsNum")  Integer orderGoodsNum,
                                       @Field("userAddressId")  Integer userAddressId, @Field("goodsId")  Integer goodsId);

    //删除订单只能删除订单状态为00的订单
    @FormUrlEncoded
    @POST("GoodsOrder/delectByOrderId")
    Observable<ResultCode> delectByOrderId(@Field("orderId") Integer orderId);

    //查找用户订单详情
    @GET("GoodsOrder/selectByUserId/{user_id}")
    Observable<List<order_goods_usermsg>> selectByUserId(@Path("user_id")int user_id);

    //查找卖家订单详情
    @GET("GoodsOrder/selectByStoreUserId/{store_user_id}")
    Observable<List<order_goods_usermsg>> selectByStoreUserId(@Path("store_user_id")int store_user_id);

    //修改订单状态为卖家确认订单01
    //修改订单状态为卖家取消订单-1,这时需要同时修改商品数量
    //修改订单状态为买家收货03
    @FormUrlEncoded
    @POST("GoodsOrder/updateOrderStatus")
    Observable<ResultCode> updateOrderStatus(@Field("orderId") Integer orderId,@Field("orderStatus") Integer orderStatus);

    //修改订单状态为卖家发货02,加入快递号
    @FormUrlEncoded
    @POST("GoodsOrder/updateOrderStatusAndCourierNum")
    Observable<ResultCode> updateOrderStatusAndCourierNum(@Field("orderId") Integer orderId,@Field("orderStatus") Integer orderStatus,@Field("courierNum") String courierNum);


}
