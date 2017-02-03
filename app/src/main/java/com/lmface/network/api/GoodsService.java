package com.lmface.network.api;

import com.lmface.pojo.ResultCode;
import com.lmface.pojo.campusinfo;
import com.lmface.pojo.collegeinfo;
import com.lmface.pojo.goods_msg;
import com.lmface.pojo.goodsclassification;
import com.lmface.pojo.goodsspecies;

import java.sql.Timestamp;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by johe on 2017/1/20.
 */

public interface GoodsService {


    //根据名字模糊查询
    @GET("GoodsInfo/selectByGoodsName/{goodsname}")
    Observable<List<goods_msg>> selectByGoodsName(@Path("goodsname")String goodsname);

    //查询用户上架商品
    @GET("GoodsInfo/selectByUserId/{user_id}")
    Observable<List<goods_msg>> selectByUserId(@Path("user_id")int user_id);

    //根据选择项查询
    @FormUrlEncoded
    @POST("GoodsInfo/selectByChoose")
    Observable<List<goods_msg>> selectByChoose(@Field("goodscity") String goodscity,@Field("colloegename") String colloegename,@Field("campusname") String campusname,@Field("classificationname") String classificationname,@Field("speciesname") String speciesname);

    //上架商品
    @FormUrlEncoded
    @POST("GoodsInfo/insertGoods")
    Observable<ResultCode> insertGoods(@Field("userId") Integer userId,@Field("goodsname") String goodsname,@Field("goodsdetails") String goodsdetails,@Field("goodsimgaddress1") String goodsimgaddress1,
                                      @Field("goodsimgaddress2") String goodsimgaddress2,@Field("goodsimgaddress3") String goodsimgaddress3,@Field("userphonenum") String userphonenum,
                                      @Field("goodsprice") double goodsprice,@Field("goodscity") String goodscity,@Field("collegeid") Integer collegeid,@Field("campusid") Integer campusid,
                                      @Field("classificationid") Integer classificationid,@Field("speciesid") Integer speciesid,@Field("goodsnum") Integer goodsnum,@Field("courierId") Integer courierId);

    //修改商品
    @FormUrlEncoded
    @POST("GoodsInfo/updateGoodsByGoodsId")
    Observable<ResultCode> updateGoodsByGoodsId(@Field("goodsid") Integer goodsid, @Field("userId") Integer userId, @Field("goodsname") String goodsname, @Field("goodsdetails") String goodsdetails, @Field("goodsimgaddress1") String goodsimgaddress1,
                                                @Field("goodsimgaddress2") String goodsimgaddress2, @Field("goodsimgaddress3") String goodsimgaddress3, @Field("userphonenum") String userphonenum,
                                                @Field("goodsprice") double goodsprice, @Field("goodscity") String goodscity, @Field("collegeid") Integer collegeid, @Field("campusid") Integer campusid,
                                                @Field("classificationid") Integer classificationid, @Field("speciesid") Integer speciesid, @Field("goodsnum") Integer goodsnum, @Field("shelvestime") Timestamp shelvestime, @Field("courierId") Integer courierId);

    //删除商品
    @FormUrlEncoded
    @POST("GoodsInfo/delectGoodsById")
    Observable<ResultCode> delectGoodsById(@Field("goodsid") int goodsid);

    //批量删除商品
    @FormUrlEncoded
    @POST("GoodsInfo/delectGoodsByListId")
    Observable<ResultCode> delectGoodsByListId(@Field("goodsIds") List<Integer> goodsIds);

    //根据商品id查询
    @GET("GoodsInfo/selectByGoodsId/{goodsid}")
    Observable<goods_msg> selectByGoodsId(@Path("goodsid")int goodsid);

    //Classification查询
    @GET("GoodsInfo/getClassificationMsg")
    Observable<List<goodsclassification>> getClassificationMsg();
    //species查询
    @GET("GoodsInfo/getspeciesMsg/{classificationid}")
    Observable<List<goodsspecies>> getspeciesMsg(@Path("classificationid")int classificationid);
    //college查询
    @GET("GoodsInfo/getcollegeMsg/{cityId}")
    Observable<List<collegeinfo>> getcollegeMsg(@Path("cityId")String cityId);
    //campus查询
    @GET("GoodsInfo/getcampusMsg/{collegeid}")
    Observable<List<campusinfo>> getcampusMsg(@Path("collegeid")int collegeid);


    @Multipart
    @POST("GoodsInfo/photoUpload")
    Observable<ResultCode> photoUpload(@Part MultipartBody.Part file);

}
