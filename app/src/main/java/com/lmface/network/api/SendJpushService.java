package com.lmface.network.api;

import com.lmface.pojo.goods_msg;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/6.
 */

public interface SendJpushService {
    //发起推送签到
    @FormUrlEncoded
    @GET("Sign/initiateSign")
    Observable<List<goods_msg>> initiateSign(@Field("regeSterIds")List<String> regeSterIds,@Field("msgContent")String msgContent);



}
