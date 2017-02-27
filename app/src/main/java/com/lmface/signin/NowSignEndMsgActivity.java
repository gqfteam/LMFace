package com.lmface.signin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.TemporarySignMsg;
import com.lmface.pojo.user_msg;
import com.lmface.util.in.srain.cube.views.ptr.PtrClassicFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrDefaultHandler;
import com.lmface.util.in.srain.cube.views.ptr.PtrFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrHandler;
import com.lmface.util.myPullToRefreshHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
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


    List<user_msg> signUserMsgs;
    TemporarySignMsg temporarySignMsg;

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
        //下拉刷新
        initPullToRefresh();


    }
    //初始化单条签到详情
    public void initSignMsg(){

    }
    //初始化单次签到的被发起签到的用户签到id
    //列表根据首字母快速查询
    public void initSignUserId(){

    }
    //根据id查询用户
    public void initSignUserMsg(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }
}
