package com.lmface.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjy on 16/8/15.
 *
 */
public class MyStoreOrderViewPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> datas;

    public MyStoreOrderViewPagerAdapter(FragmentManager fm) {
        super(fm);
        datas=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=StoreOrderListFragment.newInstance(position);
        boolean isHave=false;
        for(Fragment f:datas){
            if(f==fragment){
                isHave=true;
            }
        }
        if(!isHave) {
            datas.add(fragment);
        }
        ((StoreOrderListFragment)fragment).setOrderChangeLinsener(new StoreOrderListFragment.OrderChangeLinsener() {
            @Override
            public void orderChange() {
                for(Fragment f:datas){
                    ((StoreOrderListFragment)f).initData();
                }
            }
        });
        return fragment;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "全部";
            case 1:
                return "已取消";
            case 2:
                return "待确认";
            case 3:
                return "待发货";
            case 4:
                return "待收货";
            case 5:
                return "已完成";
        }
        return super.getPageTitle(position);
    }
}
