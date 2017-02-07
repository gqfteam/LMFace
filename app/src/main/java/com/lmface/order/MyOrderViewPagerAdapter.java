package com.lmface.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by wjy on 16/8/15.
 *
 */
public class MyOrderViewPagerAdapter extends FragmentPagerAdapter {
    public MyOrderViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return OrderListFragment.newInstance(position);
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
