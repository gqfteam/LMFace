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

public class MyOrderListActivity extends AppCompatActivity {


    Realm realm;
   // CompositeSubscription mcompositeSubscription;
    @BindView(R.id.my_order_tablayout)
    TabLayout myOrderTablayout;
    @BindView(R.id.my_order_viewpager)
    ViewPager myOrderViewpager;
    @BindView(R.id.my_orders_toolbar)
    Toolbar myOrdersToolbar;

    public void setToolbar(String str) {
        myOrdersToolbar.setTitle(str);
        setSupportActionBar(myOrdersToolbar);
        myOrdersToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        myOrdersToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        //mcompositeSubscription = new CompositeSubscription();
        setToolbar("购买订单");
        MyOrderViewPagerAdapter adapter = new MyOrderViewPagerAdapter(getSupportFragmentManager());
        myOrderViewpager.setAdapter(adapter);
        myOrderTablayout.setupWithViewPager(myOrderViewpager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
