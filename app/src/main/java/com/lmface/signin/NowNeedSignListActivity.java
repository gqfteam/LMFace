package com.lmface.signin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.sign_user_msg;
import com.lmface.pojo.user_msg;
import com.lmface.util.in.srain.cube.views.ptr.PtrClassicFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrDefaultHandler;
import com.lmface.util.in.srain.cube.views.ptr.PtrFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrHandler;
import com.lmface.util.myPullToRefreshHeader;

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
 * 点击签到按钮跳转页，显示需要签到的列表
 */

public class NowNeedSignListActivity extends AppCompatActivity {


    Realm realm;
    CompositeSubscription mcompositeSubscription;

    @BindView(R.id.now_need_sign_list_ptr)
    PtrClassicFrameLayout mPtrFrame;
    myPullToRefreshHeader header;
    @BindView(R.id.now_need_sign_list_toolbar)
    Toolbar nowNeedSignListToolbar;
    @BindView(R.id.now_need_sign_list)
    RecyclerView nowNeedSignList;

    SignListAdapter signListAdapter;

    public void setToolbar(String statu) {

        nowNeedSignListToolbar.setTitle(statu);
        setSupportActionBar(nowNeedSignListToolbar);
        nowNeedSignListToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        nowNeedSignListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
                        initData();
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
        setContentView(R.layout.activity_now_need_sign_list);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("当前可签");

        initData();

    }
    public void initData(){
        Subscription subscription = NetWork.getSignService().selectSignUserMagByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<sign_user_msg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.getMessage());
                    }

                    @Override
                    public void onNext(List<sign_user_msg> datas) {

                        Log.i("gqf","onNext"+datas.toString());
                        initListView(datas);
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void initListView(List<sign_user_msg> datas){
        if(signListAdapter==null){
            signListAdapter=new SignListAdapter(this,datas);
            nowNeedSignList.setLayoutManager(new LinearLayoutManager(this));
            nowNeedSignList.setAdapter(signListAdapter);
            //下拉刷新
            initPullToRefresh();
        }else{
            signListAdapter.update(datas);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }
}