package com.lmface.signin.mySignHistory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lmface.signin.myInitiateSign.MtInitiateSignFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjy on 16/8/15.
 *
 */
public class MySignHistoryViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment>  datas;

    public MySignHistoryViewPagerAdapter(FragmentManager fm) {
        super(fm);
        datas=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment= MtInitiateSignFragment.newInstance(position);

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "临时签到统计";
            case 1:
                return "日常签到统计";

        }
        return super.getPageTitle(position);
    }
}
