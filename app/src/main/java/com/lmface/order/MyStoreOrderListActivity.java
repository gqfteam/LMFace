package com.lmface.order;

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

/**
 * Created by johe on 2017/2/6.
 */

public class MyStoreOrderListActivity extends AppCompatActivity {


    Realm realm;
    @BindView(R.id.my_store_orders_toolbar)
    Toolbar myStoreOrdersToolbar;
    @BindView(R.id.my_order_store_tablayout)
    TabLayout myOrderStoreTablayout;
    @BindView(R.id.my_order_store_viewpager)
    ViewPager myOrderStoreViewpager;
    // CompositeSubscription mcompositeSubscription;

    public void setToolbar(String str) {
        myStoreOrdersToolbar.setTitle(str);
        setSupportActionBar(myStoreOrdersToolbar);
        myStoreOrdersToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        myStoreOrdersToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store_order_list);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        //mcompositeSubscription = new CompositeSubscription();
        setToolbar("我的订单");
        MyStoreOrderViewPagerAdapter adapter = new MyStoreOrderViewPagerAdapter(getSupportFragmentManager());
        myOrderStoreViewpager.setAdapter(adapter);
        myOrderStoreTablayout.setupWithViewPager(myOrderStoreViewpager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
