package com.lmface.network.api;

import com.lmface.pojo.ResultCode;
import com.lmface.pojo.TemporarySignMsg;
import com.lmface.pojo.UserDailySignMsg;
import com.lmface.pojo.temporary_sign_msg;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by johe on 2017/2/27.
 */

public interface SignService {

    //添加或修改用户课程或者团体事件
    @FormUrlEncoded
    @POST("Sign/updateUserInfoByUserId")
    Observable<ResultCode> insertOrUpdataCouresByUserId(@Field("couresName") String couresName,@Field("userId") int userId);

    //查询用户下所有正在使用的课程或团体事件名
    @GET("Sign/selectCouresByUserId/{userId}")
    Observable<ResultCode> selectCouresByUserId(@Path("userId")int userId);

    //删除用户课程或团体事件,修改statu为0
    @FormUrlEncoded
    @POST("Sign/delectCouresById")
    Observable<ResultCode> delectCouresById(@Field("couresrId") int couresrId);

    //用户发起签到
    @FormUrlEncoded
    @POST("Sign/initiateSign")
    Observable<ResultCode> initiateSign(@Field("initialsignin_infoJson") String initialsignin_infoJson,@Field("userIds") List<Integer> userIds);

    //获得一条用户发起签到信息
    @GET("Sign/selectSignInfoById/{signInfoId}")
    Observable<ResultCode> selectSignInfoById(@Path("signInfoId")int signInfoId);

    //用户签到
    @FormUrlEncoded
    @POST("Sign/sign")
    Observable<ResultCode> sign(@Field("user_signJson") String user_signJson);

    //签到事件过后返回本次签到人列表
    @GET("Sign/selectSignCountById/{signInfoId}")
    Observable<TemporarySignMsg> selectSignCountById(@Path("signInfoId")int signInfoId);

    //获取用户临时签到记录
    @GET("Sign/selectTemporarySignByUserId/{userId}")
    Observable<List<temporary_sign_msg>> selectTemporarySignByUserId(@Path("userId")int userId);

    //获取用户所有日常签到
    @GET("Sign/selectDailySignByUserId/{userId}")
    Observable<List<UserDailySignMsg>> selectDailySignByUserId(@Path("userId")int userId);

    //签到信息发起错误紧急删除
    @FormUrlEncoded
    @POST("Sign/delectSignInfo")
    Observable<ResultCode> delectSignInfo(@Field("signinfoid") String signinfoid);

    //获取用户发起的日常签到历史




}
