package com.lmface.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.lmface.R;
import com.lmface.huanxin.AddFriendsListActivity;
import com.lmface.huanxin.ContactActivity;
import com.lmface.pojo.AddFriendMsg;
import com.lmface.util.CommonUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by johe on 2017/1/5.
 */

public class LMFaceApplication extends Application {
    private static List<Activity> mList ;
    private static LMFaceApplication app;
    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;
    // 获取到主线程
    private static Thread mMainThead = null;
    // 获取到主线程的id
    private static int mMainTheadId;
    //获取集合size
    public  int getListSize(){
        return mList.size();
    }

    /**
     * add Activity
     * @param activity
     */
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 遍历退出activity
     */
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * OnLowMemory是Android提供的API，在系统内存不足，
     * 所有后台程序（优先级为background的进程，不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //垃圾回收
        System.gc();
    }

    Context appContext;
    //Realm初始化
    public static String username;

    Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        //        LeakCanary.install(this);
        mList = new ArrayList<>();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).schemaVersion(2).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfig);
        realm=Realm.getDefaultInstance();
        EMOptions options = new EMOptions();
        //自动登录
        options.setAutoLogin(false);
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);


        appContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e("gqf", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);


        // 创建主线程handler
        this.mMainThreadHandler = new Handler();
        // 获取当前线程
        this.mMainThead = Thread.currentThread();
        // 获取进程id
        this.mMainTheadId = android.os.Process.myTid();
        app = this;




        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());





    }
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EMClient.getInstance().logout(true);

    }


    //    public static Picasso getPicasso(){
    //        return Picasso.with(app);
    //    }
    public static LMFaceApplication getInstance() {
        return app;
    }
    public static LMFaceApplication getContext() {
        return app;
    }

    //    public RequestQueue getmRequestQueue() {
    //        return mRequestQueue;
    //    }
    // 获取主线程handler
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }
    // 获取主线程
    public static Thread getMainThread() {
        return mMainThead;
    }

    // 获取主线程ID
    public static int getMainThreadId() {
        return mMainTheadId;
    }



    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            CommonUtil.runOnUIThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
//                        if (NetUtils.hasNetwork(MainActivity.this))
//                        //连接不到聊天服务器
//                        else
                        //当前网络不可用，请检查网络设置
                    }
                }
            });
        }
    }

}
