package com.lmface.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.lmface.R;


/**
 * Created by ice on 2015/12/24.
 * 消失
 */
public class VisibilityUtil {
    public static void gone(final View view, final long time){
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
               view.setVisibility(View.GONE);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=Message.obtain();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(message);
            }
        }).start();
    }
    public static void setNoMore(Context context,TextView textView,String string){
        textView.setVisibility(View.VISIBLE);
        textView.setText(string);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.search_nomoredata);
        textView.startAnimation(animation);
        gone(textView, 5000);
    }
}
