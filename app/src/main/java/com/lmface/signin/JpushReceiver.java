package com.lmface.signin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.lmface.pojo.initialsignin_info;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/3/12.
 */

public class JpushReceiver extends BroadcastReceiver{
    private initialsignin_info signin_info=null;
    private String message=null;
    @Override
    public void onReceive(Context context, Intent intent) {
        //如果这两个相等，则说明应用已接收到自定义消息


     if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
            //获取message里的内容
            Bundle bundle=intent.getExtras();
            String title=bundle.getString(JPushInterface.EXTRA_TITLE);
             message=bundle.getString(JPushInterface.EXTRA_MESSAGE);
        /* Log.i("Jpush_Tag","title1111-------"+title);
         Log.i("Jpush_Tag","message1111-------"+message);
    if (message!=null){
         Log.i("Jpush_Tag","message1111-------"+message);
             Gson gson = new Gson();
          signin_info= gson.fromJson(message,initialsignin_info.class);
         Log.i("Jpush_Tag","解析成功"+signin_info.getSigncourseid());
         }*/
        /*    Log.i("Jpush_Tag","title-------"+title);
            Log.i("Jpush_Tag","message-------"+message);*/
        /*    jstxt=message;
            //initiateSigninMsgGson = gson.toJson(initiateSigninMsg);
            //BaseApplication.json=gson.fromJson(message, InitiateSigninMsg.class);
            Toast.makeText(context,"Message title:"+title+" context:"+message, Toast.LENGTH_LONG).show();
*/        }
        else if(intent.getAction().equals((JPushInterface.ACTION_NOTIFICATION_OPENED))){
        Log.i("Jpush_TAG","跳转到签到");
            // 在这里可以自己写代码去定义用户点击后的行为   跳转到签到页面
       /*  Bundle bundle=intent.getExtras();
         String title=bundle.getString(JPushInterface.EXTRA_TITLE);
         message=bundle.getString(JPushInterface.EXTRA_MESSAGE);

         Log.i("Jpush_Tag","title222-------"+title);
         Log.i("Jpush_Tag","message222-------"+message);

*/
            Intent i = new Intent(context,NowSignEndMsgActivity.class);//自定义打开的界面
         if(message!=null){
                   i.putExtra("jpush_message",message);
                 /*  i.putExtra("initiateSignId",signin_info.getSigninfoid());*/
               }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

         Log.i("Jpush_TAG","跳转");
            context.startActivity(i);
        }
    }
}
