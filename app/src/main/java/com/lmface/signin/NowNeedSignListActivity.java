package com.lmface.signin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lmface.R;
import com.lmface.util.in.srain.cube.views.ptr.PtrClassicFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrDefaultHandler;
import com.lmface.util.in.srain.cube.views.ptr.PtrFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrHandler;
import com.lmface.util.myPullToRefreshHeader;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
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
        setToolbar("签到详情");
        //下拉刷新
        initPullToRefresh();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }
}