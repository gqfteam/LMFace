package com.lmface.signin.myInitiateSign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.lmface.R;
import com.lmface.huanxin.DemoHelper;
import com.lmface.network.NetWork;
import com.lmface.pojo.UserDailySignMsg;
import com.lmface.pojo.UserFriend;
import com.lmface.pojo.user_msg;

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
 * 我发起的日常签到详细信息页
 */

public class DaliySignEndMsgActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.daily_sign_end_msg_toolbar)
    Toolbar dailySignEndMsgToolbar;
    @BindView(R.id.daily_sign_end_msg_list)
    RecyclerView dailySignEndMsgList;

    List<UserDailySignMsg> userDailySignMsgs;

    ArrayList<UserFriend> userFriends;
    DailySignEndMsgListAdapter dailySignEndMsgListAdapter;
    int courseId=0;

    public void setToolbar(String statu) {

        dailySignEndMsgToolbar.setTitle(statu);
        setSupportActionBar(dailySignEndMsgToolbar);
        dailySignEndMsgToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        dailySignEndMsgToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sign_end_msg);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        String title=getIntent().getStringExtra("courseName");
        courseId=getIntent().getIntExtra("courseId",0);
        setToolbar(title);

        initData();



    }

    //查询接收到这个签到事件下所有用户的签到情况
    public void initData(){

        Subscription subscription = NetWork.getSignService().selectSignUserByCourseidAndDaily(courseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserDailySignMsg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<UserDailySignMsg> datas) {
                        userDailySignMsgs=datas;
                        Log.i("gqf", "onNext" + datas.toString());

                        List<Integer> userIds=new ArrayList<Integer>();
                        for(UserDailySignMsg userDailySignMsg:datas){
                             userIds.add(userDailySignMsg.getUserId());
                        }
                        initUserMsg(userIds);

                    }
                });
        mcompositeSubscription.add(subscription);

    }
    DemoHelper demoHelper;
    public void initUserMsg(List<Integer> userIds){
        Subscription subscription = NetWork.getUserService().selectUserByListId(userIds)
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
                        Log.i("gqf","onNext"+data.toString());
                        userFriends=new ArrayList<UserFriend>();
                        //首字母拼音
                        int i=0;
                        for(user_msg user_msg:data){
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
                            //载入数据
                            userFriend.setMsg(user_msg.getUserId(),user_msg.getNickname(),user_msg.getHeadimg(),user_msg.getSex(),user_msg.getPhone(),user_msg.getRealname());
                            userFriend.setSignNum( userDailySignMsgs.get(i).getSignNum());
                            userFriend.setNeedSignNum(userDailySignMsgs.get(i).getNeedSignNum());
                            i++;
                            userFriends.add(userFriend);
                        }
                        //
                        demoHelper = DemoHelper.getInstance();
                        userFriends = demoHelper.filledData(userFriends);
                        initList(userFriends);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initList(ArrayList<UserFriend> data){

        if(dailySignEndMsgListAdapter==null){
            dailySignEndMsgListAdapter=new DailySignEndMsgListAdapter(this,data);
            dailySignEndMsgList.setLayoutManager(new LinearLayoutManager(this));
            dailySignEndMsgList.setAdapter(dailySignEndMsgListAdapter);
        }else{
            dailySignEndMsgListAdapter.update(data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }
}
