package com.lmface.network;

import com.lmface.network.api.GoodsOrderService;
import com.lmface.network.api.GoodsService;
import com.lmface.network.api.SendJpushService;
import com.lmface.network.api.ShopCarService;
import com.lmface.network.api.SignService;
import com.lmface.network.api.UserAddressService;
import com.lmface.network.api.UserService;

import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wjy on 16/9/1.
 */
public class NetWork {
    private static UserService userService;
    private static UserAddressService userAddressService;
    private static ShopCarService shopCarService;
    private static GoodsService goodsService;
    private static GoodsOrderService goodsOrderService;
    private static SignService signService;
    private static SendJpushService sendJpushService;

    private static final int URL_TYPE_MFACE=1;
    private static final int URL_TYPE_SIGN_FACE=2;


    public static SignService getSignService() {
        if (signService == null) {
            Retrofit retrofit = getRetrofit(URL_TYPE_SIGN_FACE);
            signService = retrofit.create(SignService.class);
        }
        return signService;
    }
    public static UserService getUserService() {
        if (userService == null) {
            Retrofit retrofit = getRetrofit(URL_TYPE_MFACE);
            userService = retrofit.create(UserService.class);
        }
        return userService;
    }

    public static UserAddressService getUserAddressService() {
        if (userAddressService == null) {
            Retrofit retrofit = getRetrofit(URL_TYPE_MFACE);
            userAddressService = retrofit.create(UserAddressService.class);
        }
        return userAddressService;
    }
    public static ShopCarService getShopCarService() {
        if (shopCarService == null) {
            Retrofit retrofit = getRetrofit(URL_TYPE_MFACE);
            shopCarService = retrofit.create(ShopCarService.class);
        }
        return shopCarService;
    }
    public static GoodsService getGoodsService() {
        if (goodsService == null) {
            Retrofit retrofit = getRetrofit(URL_TYPE_MFACE);
            goodsService = retrofit.create(GoodsService.class);
        }
        return goodsService;
    }
    public static  SendJpushService getSendJpushService(){
        if (sendJpushService==null){
            Retrofit retrofit=getRetrofit(URL_TYPE_SIGN_FACE);
            sendJpushService=retrofit.create(SendJpushService.class);
        }
        return sendJpushService;
    }
    public static GoodsOrderService getGoodsOrderService() {
        if (goodsOrderService == null) {
            Retrofit retrofit = getRetrofit(URL_TYPE_MFACE);
            goodsOrderService = retrofit.create(GoodsOrderService.class);
        }
        return goodsOrderService;
    }

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static Retrofit getRetrofit(int index) {
        String url="";
        if(index==URL_TYPE_MFACE){
            url="http://101.201.116.99:8081/mface/";
        }else if(index==URL_TYPE_SIGN_FACE){
            url="http://101.201.116.99:8081/signface/";
        }

//        if(index==URL_TYPE_MFACE){
//            url="http://192.168.56.1:8080/mface/";
//        }else if(index==URL_TYPE_SIGN_FACE){
//            url="http://192.168.56.1:8080/signface/";
//        }

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }
}
