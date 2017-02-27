package com.lmface.signin.mySignHistory;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lmface.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 * 签到历史
 */

public class MySignHistoryActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.my_initiate_sign_toolbar)
    Toolbar myInitiateSignToolbar;
    @BindView(R.id.my_initiate_sign_tablayout)
    TabLayout myInitiateSignTablayout;
    @BindView(R.id.my_initiate_sign_viewpager)
    ViewPager myInitiateSignViewpager;

    MySignHistoryViewPagerAdapter myInitiateSignViewPagerAdapter;

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
        setToolbar("签到历史");
        myInitiateSignViewPagerAdapter  = new MySignHistoryViewPagerAdapter(getSupportFragmentManager());
        myInitiateSignViewpager.setAdapter(myInitiateSignViewPagerAdapter);
        myInitiateSignTablayout.setupWithViewPager(myInitiateSignViewpager);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }
}
