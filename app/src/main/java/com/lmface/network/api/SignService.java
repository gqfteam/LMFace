package com.lmface.network.api;

import com.lmface.pojo.ResultCode;
import com.lmface.pojo.TemporarySignMsg;
import com.lmface.pojo.UserDailySignMsg;
import com.lmface.pojo.courseinfo;
import com.lmface.pojo.sign_user_msg;
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
    Observable<List<courseinfo>> selectCouresByUserId(@Path("userId")int userId);

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
    Observable<sign_user_msg> selectSignInfoById(@Path("signInfoId")int signInfoId);

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
    Observable<ResultCode> delectSignInfo(@Field("signinfoid") int signinfoid);

    //获取该用户在当前时间之后的所有未签到信息
    @GET("Sign/selectSignUserMagByUserId/{userId}")
    Observable<List<sign_user_msg>> selectSignUserMagByUserId(@Path("userId")int userId);

    //获取当前用户发起的（发起签到人，签到事件）一个签到课程下面所有的发起过的签到信息(日常)
    @GET("Sign/selectInitialsigninInfoByCourseIdAndDaily/{courseId}")
    Observable<List<sign_user_msg>> selectInitialsigninInfoByCourseIdAndDaily(@Path("courseId")int courseId);

    //获取当前用户发起的所有详细信息(临时)
    @GET("Sign/selectInitialsigninInfoByUserIdAndTemporary/{userId}")
    Observable<List<sign_user_msg>> selectInitialsigninInfoByUserIdAndTemporary(@Path("userId")int userId);


    //签到事件下总统计
    @GET("Sign/selectSignUserByCourseidAndDaily/{courseId}")
    Observable<List<UserDailySignMsg>> selectSignUserByCourseidAndDaily(@Path("courseId")int courseId);


}
