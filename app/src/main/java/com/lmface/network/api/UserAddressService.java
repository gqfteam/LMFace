package com.lmface.network.api;

import com.lmface.pojo.ResultCode;
import com.lmface.pojo.user_address;

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

public interface UserAddressService {

    //增加新地址
    @FormUrlEncoded
    @POST("UserAddress/addUserAddress")
    Observable<ResultCode> addUserAddress(@Field("userId") int userId,@Field("countries") String countries,@Field("provinces") String provinces,
                          @Field("city") String city,@Field("county")  String county,
                          @Field("street") String street,@Field("addressTxt") String addressTxt,
                          @Field("name") String name,@Field("phone") String phone);

    //修改地址
    @FormUrlEncoded
    @POST("UserAddress/updateUserAddress")
    Observable<ResultCode> updateUserAddress(@Field("addressId") int addressId, @Field("userId") int userId, @Field("countries") String countries, @Field("provinces") String provinces,
                                             @Field("city") String city, @Field("county")  String county,
                                             @Field("street") String street, @Field("addressTxt") String addressTxt,
                                             @Field("name") String name, @Field("phone") String phone);

    //删除地址
    @FormUrlEncoded
    @POST("UserAddress/delectUserAddress")
    Observable<ResultCode> delectUserAddress(@Field("addressId")int addressId);

    //批量删除地址
    @FormUrlEncoded
    @POST("UserAddress/delectAddressByListAddressId")
    Observable<ResultCode> delectAddressByListAddressId(@Field("addressIds")List<Integer> addressIds);

    //查询用户的所有收货地址
    @GET("UserAddress/selectUserAddressByUserId/{userId}")
    Observable<List<user_address>> selectUserAddressByUserId(@Path("userId")int userId);


    //查询单条地址
    @GET("UserAddress/selectUserAddressById/{addressId}")
    Observable<user_address> selectUserAddressById(@Path("addressId")int addressId);
}
