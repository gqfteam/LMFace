package com.lmface;

import android.util.Log;

import com.google.gson.Gson;
import com.lmface.pojo.initialsignin_info;
import com.lmface.util.ConvertBaiduXY;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
   @Test
    public void addition_isCorrect() throws Exception {
     double a=  ConvertBaiduXY.getConvertBaiduXY().getDistanceFromTwoPoints(34.665688,112.380407,34.665888,113.330007);
       System.out.print(a);
   }
   /* @Test
    public void addition_isCorrect() throws Exception {
        Gson gson=new Gson();
        String  message="{\"addresslatitude\":\"34.3213231223\",\"addresslongitude\":\"33.3131432\",\"signaddress\":\"高科技和\",\"signcourseid\":2,\"signendtime\":\"2017-04-20 11:02:00\",\"signgoal\":\"不好看基本\",\"signintervaltime\":30,\"signscope\":150,\"signstarttime\":\"2017-04-20 10:32:00\",\"temporaryordaily\":2}";
        initialsignin_info  signin_info= gson.fromJson(message,initialsignin_info.class);
      System.out.print(signin_info.getAddresslatitude());

    }*/
}