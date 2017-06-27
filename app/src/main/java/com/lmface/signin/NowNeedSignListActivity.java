package com.lmface.signin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.lmface.R;
import com.lmface.application.LMFaceApplication;
import com.lmface.network.NetWork;
import com.lmface.network.api.LocationService;
import com.lmface.pojo.sign_user_msg;
import com.lmface.pojo.user_msg;
import com.lmface.signin.myinterface.LocationInterface;
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
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 * 点击签到按钮跳转页，显示需要签到的列表   显示可签到页面
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


    private LocationService locationService;
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;

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

    @Override
    protected void onStart() {
        super.onStart();
        //坐标定位
        getPersimmions();
        locationService = ((LMFaceApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());

    }

    public void initData(){

        Log.i("gqf","用户IDs--"+realm.where(user_msg.class).findFirst().getUserId());

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

                        Log.i("gqf","获取签到信息"+datas.toString());
                        initListView(datas);
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void initListView(List<sign_user_msg> datas){
        if(signListAdapter==null){
            signListAdapter=new SignListAdapter(this,datas);
            signListAdapter.setOncompareLocationDistanceListener(new LocationInterface() {
                @Override
                public double compareLocationDistance(double lng, double lat) {


                    return 0;
                }
            });
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



    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);

                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度

               // logMsg(location.getLongitude()+"",location.getLatitude()+"");
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}