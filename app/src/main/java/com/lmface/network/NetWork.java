package com.lmface.network;

import com.lmface.network.api.GoodsOrderService;
import com.lmface.network.api.GoodsService;
import com.lmface.network.api.ShopCarService;
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

    public static UserService getUserService() {
        if (userService == null) {
            Retrofit retrofit = getRetrofit();
            userService = retrofit.create(UserService.class);
        }
        return userService;
    }

    public static UserAddressService getUserAddressService() {
        if (userAddressService == null) {
            Retrofit retrofit = getRetrofit();
            userAddressService = retrofit.create(UserAddressService.class);
        }
        return userAddressService;
    }
    public static ShopCarService getShopCarService() {
        if (shopCarService == null) {
            Retrofit retrofit = getRetrofit();
            shopCarService = retrofit.create(ShopCarService.class);
        }
        return shopCarService;
    }
    public static GoodsService getGoodsService() {
        if (goodsService == null) {
            Retrofit retrofit = getRetrofit();
            goodsService = retrofit.create(GoodsService.class);
        }
        return goodsService;
    }

    public static GoodsOrderService getGoodsOrderService() {
        if (goodsOrderService == null) {
            Retrofit retrofit = getRetrofit();
            goodsOrderService = retrofit.create(GoodsOrderService.class);
        }
        return goodsOrderService;
    }

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:8080/mface/")
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }

}
