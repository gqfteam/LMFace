package com.lmface.signin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.lmface.R;
import com.lmface.huanxin.DemoHelper;
import com.lmface.network.NetWork;
import com.lmface.network.api.LocationService;
import com.lmface.pojo.TemporarySignMsg;
import com.lmface.pojo.UserFriend;
import com.lmface.pojo.initialsignin_info;
import com.lmface.pojo.sign_user_msg;
import com.lmface.pojo.user_msg;
import com.lmface.util.in.srain.cube.views.ptr.PtrClassicFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrDefaultHandler;
import com.lmface.util.in.srain.cube.views.ptr.PtrFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrHandler;
import com.lmface.util.myPullToRefreshHeader;
import com.lmface.view.AutoHeightLayoutManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 * 签到详细信息页
 */

public class NowSignEndMsgActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.temporary_sign_list_courseName)
    TextView temporarySignListCourseName;
    @BindView(R.id.temporary_sign_list_user_name)
    TextView temporarySignListUserName;
    @BindView(R.id.temporary_sign_list_interval_time)
    TextView temporarySignListIntervalTime;
    @BindView(R.id.temporary_sign_commit_btn)
    Button temporarySignCommitBtn;
    @BindView(R.id.temporary_sign_list_purpose)
    TextView temporarySignListPurpose;
    @BindView(R.id.temporary_sign_list_address)
    TextView temporarySignListAddress;
    @BindView(R.id.temporary_sign_list_start_time)
    TextView temporarySignListStartTime;
    @BindView(R.id.sign_user_list)
    RecyclerView signUserList;
    @BindView(R.id.now_sign_end_msg_scroll)
    ScrollView nowSignEndMsgScroll;
    @BindView(R.id.now_sign_end_msg_sc_ptr)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.now_sign_end_msg_toolbar)
    Toolbar nowSignEndMsgToolbar;
    myPullToRefreshHeader header;

    sign_user_msg sign_user_msg;
    List<user_msg> signUserMsgs;
    TemporarySignMsg temporarySignMsg;
    ArrayList<UserFriend> users;
    int initiateSignId=0;
    initialsignin_info    signin_info;
    NoSignEndMsgListAdapter noSignEndMsgListAdapter;

    //百度定位

    public void setToolbar(String statu) {


        nowSignEndMsgToolbar.setTitle(statu);
        setSupportActionBar(nowSignEndMsgToolbar);
        nowSignEndMsgToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        nowSignEndMsgToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void initPullToRefresh() {
        header = new myPullToRefreshHeader(this);

        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                      //刷新
                        initSignUserId(sign_user_msg.getSigninfoid());
                    }
                }, 2000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        content, header);
            }
        });

        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_sign_end_msg);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("签到详情");
        signUserMsgs=new ArrayList<>();
        users=new ArrayList<>();
        temporarySignCommitBtn.setVisibility(View.GONE);
        Log.i("Jpush","获取数据");

        SharedPreferences preferences=getSharedPreferences("jpush_message", Context.MODE_PRIVATE);
        String message=preferences.getString("message", "0");


       /* initiateSignId=getIntent().getIntExtra("initiateSignId",0);

        String message=getIntent().getStringExtra("jpush_message");*/

        Log.i("Jpush","得到数据--"+message);
       // Intent intent=getIntent();

       if (message!=null&&!"".equals(message)){
           try {
               JSONObject   jsonObject = new JSONObject(message);
               /*initiateSignId =Integer.valueOf(jsonObject.get("signcourseid"));*/
               Log.i("Jpush","得到数据 signin_info--"+jsonObject.get("signcourseid").toString());
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

        initSignMsg(initiateSignId);



    }
    //初始化单条签到详情
 public void initSignMsg(int id){

        Subscription subscription = NetWork.getSignService().selectSignInfoById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<sign_user_msg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(sign_user_msg data) {
                   if(data!=null){


                            sign_user_msg=data;
                            initView(data);
                        }

                    }
                });
        mcompositeSubscription.add(subscription);


    }
    public void initView(sign_user_msg data){
        temporarySignListAddress.setText(data.getSignaddress());
        String name = "";
        name = data.getUserName();
        if (data.getNickname() != null) {
            if (!data.getNickname().equals("")) {
                name = data.getNickname();
            }
        }
        if (data.getRealname() != null) {
            if (!data.getRealname().equals("")) {
                name = data.getRealname();
            }
        }
        temporarySignListUserName.setText("发起人："+name);
         temporarySignListAddress.setText( data.getSignaddress());
         temporarySignListCourseName.setText( data.getCoursename());
         temporarySignListIntervalTime.setText("持续时间:" +  data.getSignintervaltime()+"分");
         temporarySignListStartTime.setText("开始时间" +  data.getSignstarttime());

         temporarySignListPurpose.setText( "签到目的："+data.getSigngoal());
        initSignUserId(data.getSigninfoid());


    }

    //初始化单次签到的被发起签到的用户签到id
    public void initSignUserId(int id){


        Subscription subscription = NetWork.getSignService().selectSignCountById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TemporarySignMsg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(TemporarySignMsg data) {
                        temporarySignMsg=data;
                        Log.i("gqf","onNext"+data.toString());
                        initSignUserMsg();

                    }
                });
        mcompositeSubscription.add(subscription);

    }
    DemoHelper demoHelper;
    //根据id查询用户
    //列表根据首字母快速查询
    public void initSignUserMsg(){
        Subscription subscription = NetWork.getUserService().selectUserByListId(temporarySignMsg.getNeedSignUserIds())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<user_msg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<user_msg> data) {

                        signUserMsgs=data;
                        //数据转换
                        Log.i("gqf","onNext"+data.toString());
                        users=new ArrayList<>();
                        for(user_msg user_msg:signUserMsgs){
                            String name = "";
                            name = user_msg.getUserName();
                            if (user_msg.getNickname() != null) {
                                if (!user_msg.getNickname().equals("")) {
                                    name = user_msg.getNickname();
                                }
                            }
                            if (user_msg.getRealname() != null) {
                                if (!user_msg.getRealname().equals("")) {
                                    name = user_msg.getRealname();
                                }
                            }
                            UserFriend userFriend=new UserFriend(name);
                            userFriend.setMsg(user_msg.getUserId(),user_msg.getNickname(),user_msg.getHeadimg(),user_msg.getSex(),user_msg.getPhone(),user_msg.getRealname());
                            users.add(userFriend);
                        }
                        //排序
                        demoHelper = DemoHelper.getInstance();
                        users = demoHelper.filledData(users);
                        initList(users);
                    }
                });
        mcompositeSubscription.add(subscription);

    }
    public void initList(ArrayList<UserFriend> data){
        if(noSignEndMsgListAdapter==null){
            noSignEndMsgListAdapter=new NoSignEndMsgListAdapter(this,data);
            noSignEndMsgListAdapter.setSignUserIds(temporarySignMsg.getSignUserIds());
            noSignEndMsgListAdapter.setSignInfoId(sign_user_msg.getSigninfoid());
            signUserList.setLayoutManager(new AutoHeightLayoutManager(this));
            signUserList.setAdapter(noSignEndMsgListAdapter);

            //下拉刷新
            initPullToRefresh();
        }else{
            noSignEndMsgListAdapter.setSignUserIds(temporarySignMsg.getSignUserIds());
            noSignEndMsgListAdapter.update(data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }



}
