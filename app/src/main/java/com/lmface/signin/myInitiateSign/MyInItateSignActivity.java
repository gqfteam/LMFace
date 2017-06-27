package com.lmface.signin.myInitiateSign;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.courseinfo;
import com.lmface.pojo.sign_user_msg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * 我发起的签到统计
 */

public class MyInItateSignActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.my_initiate_sign_toolbar)
    Toolbar myInitiateSignToolbar;
    @BindView(R.id.my_initiate_sign_tablayout)
    TabLayout myInitiateSignTablayout;
    @BindView(R.id.my_initiate_sign_viewpager)
    ViewPager myInitiateSignViewpager;

    MyInitiateSignViewPagerAdapter myInitiateSignViewPagerAdapter;
    @BindView(R.id.my_initiate_daily_sign_course_list)
    RecyclerView myInitiateDailySignCourseList;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    DailySignListAdapter dailySignListAdapter;

    public void setToolbar(String statu) {
        myInitiateSignToolbar.setTitle(statu);
        setSupportActionBar(myInitiateSignToolbar);
        myInitiateSignToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        myInitiateSignToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_initiate_sign);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        EventBus.getDefault().register(this);


        setToolbar("我发起的签到");
        myInitiateSignViewPagerAdapter = new MyInitiateSignViewPagerAdapter(getSupportFragmentManager());
        myInitiateSignViewpager.setAdapter(myInitiateSignViewPagerAdapter);
        myInitiateSignTablayout.setupWithViewPager(myInitiateSignViewpager);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDrawerLayout(courseinfo courseinfo) {
        Log.i("gqf", "courseinfo" + courseinfo.getCourseid());
        Subscription subscription = NetWork.getSignService().selectInitialsigninInfoByCourseIdAndDaily(courseinfo.getCourseid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<sign_user_msg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.getMessage());
                        Toast.makeText(getApplicationContext(), "请检查网络连接。。。", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<sign_user_msg> datas) {

                        Log.i("gqf","onNext"+datas.toString());
                        initDrawerLayoutList(datas);
                    }
                });
        mcompositeSubscription.add(subscription);

    }

    public void initDrawerLayoutList(List<sign_user_msg> datas){


        if(datas==null||datas.size()==0){
            Toast.makeText(getApplicationContext(), "当前签到事件下没有发起签到记录！", Toast.LENGTH_SHORT).show();
        }else{
            if(dailySignListAdapter==null){
                dailySignListAdapter=new DailySignListAdapter(this,datas);
                myInitiateDailySignCourseList.setLayoutManager(new LinearLayoutManager(this));
                myInitiateDailySignCourseList.setAdapter(dailySignListAdapter);

            }else {
                dailySignListAdapter.update(datas);

            }
            drawerLayout.openDrawer(myInitiateDailySignCourseList);
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeActivity(
            Intent intent) {
        Log.i("gqf","changeActivity");
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
        EventBus.getDefault().unregister(this);
    }


}
