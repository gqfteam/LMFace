package com.lmface.network.api;

import com.lmface.pojo.ResultCode;
import com.lmface.pojo.user_msg;

import java.math.BigDecimal;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by johe on 2017/1/5.
 */

public interface UserService {

    //登录
    @FormUrlEncoded
    @POST("User/loginUsers")
    Observable<user_msg> loginUsers(@Field("userName") String user_name, @Field("userPassword") String user_password);

    //注册
    @FormUrlEncoded
    @POST("User/registeredUsers")
    Observable<user_msg> registeredUsers(@Field("userName") String user_name, @Field("userPassword") String user_password);

    //注册名字查重
    @GET("User/selectByUserName/{userName}")
    Observable<ResultCode> selectByUserName(@Path("userName")String userName);

    //登出
    @FormUrlEncoded
    @POST("User/logoutUsers")
    Observable<ResultCode> logoutUsers(@Field("user_id") int user_id);

    //修改用户详情信息
    @FormUrlEncoded
    @POST("User/updateUserInfoByUserId")
    Observable<ResultCode> updateUserInfoByUserId(@Field("userId") Integer userId,@Field("realname") String realname,
                                  @Field("age") Integer age,@Field("userName") String userName,@Field("sex") String sex,@Field("headimg") String headimg,
                                  @Field("usertype") Integer usertype,@Field("studentnum") String studentnum,@Field("personalnote") String personalnote,
                                  @Field("classid") String classid,@Field("phone") String phone);

    //增加或减少用户金钱
    @FormUrlEncoded
    @POST("User/updateUserInfoMoneyByUserId")
    Observable<ResultCode> updateUserInfoMoneyByUserId(@Field("user_id") int user_id,@Field("changeMoney") BigDecimal changeMoney);


}
