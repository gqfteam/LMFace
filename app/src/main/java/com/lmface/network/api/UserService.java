package com.lmface.network.api;

import com.lmface.pojo.ResultCode;
import com.lmface.pojo.user_msg;

import java.math.BigDecimal;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Observable<ResultCode> registeredUsers(@Field("useName") String useName, @Field("password") String password);

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
    Observable<ResultCode> updateUserInfoByUserId(@Field("userInfoJson") String userInfoJson);

    //增加或减少用户金钱
    @FormUrlEncoded
    @POST("User/updateUserInfoMoneyByUserId")
    Observable<ResultCode> updateUserInfoMoneyByUserId(@Field("userId") int userId,@Field("changeMoney") BigDecimal changeMoney);

    //修改用户密码
    @FormUrlEncoded
    @POST("User/updateUserPassword")
    Observable<ResultCode> updateUserPassword(@Field("userId") int userId,@Field("passWord") String passWord);

    //设置用户默认地址
    @FormUrlEncoded
    @POST("User/updateUserDefaultAddress")
    Observable<ResultCode> updateUserDefaultAddress(@Field("userId") int userId,@Field("addressId") int addressId);

    //根据用户名查询信息
    @FormUrlEncoded
    @POST("User/selectUserByName")
    Observable<user_msg> selectUserByName(@Field("userName") String userName);

    //修改用戶头像
    @POST("User/changeUserHeadImg2")
    Observable<String> changeUserHeadImg2(@Body MultipartBody body);
    //这里的userId是UiId
    @Multipart
    @POST("User/changeUserHeadImg")
    Observable<ResultCode> changeUserHeadImg(
            @Part MultipartBody.Part file,
            @Part("userId") Integer userId
            );

    //根据用户名集合查询用户
    @FormUrlEncoded
    @POST("User/selectUserByListName")
    Observable<List<user_msg>> selectUserByListName(@Field("userNames") List<String> userNames);

    //根据用户id集合查询用户
    @FormUrlEncoded
    @POST("User/selectUserByListId")
    Observable<List<user_msg>> selectUserByListId(@Field("userIds") List<Integer> userIds);
}
