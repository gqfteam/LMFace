package com.lmface.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.lmface.R;
import com.lmface.store.ChooseStoreSwoListFragment;
import com.lmface.store.StoreListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by johe on 2017/1/5.
 */

public class StoreFragment extends Fragment {
    Realm realm;
    @BindView(R.id.CB_shop_city)
    CheckBox CBShopCity;
    @BindView(R.id.CB_shop_campus)
    CheckBox CBShopCampus;
    @BindView(R.id.CB_shop_classification)
    CheckBox CBShopClassification;
    @BindView(R.id.shop_off)
    CheckBox shopOff;
    @BindView(R.id.shop_rad_group)
    LinearLayout shopRadGroup;

    public static StoreFragment newInstance(String param1) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    private StoreListFragment storeListFragment;
    private ChooseStoreSwoListFragment chooseStoreSwoListFragment;
    private Intent mIntent;

    public interface mListener {
        public void changeActivity(
                Class activityClass);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    public void getByName(String edi){
        if(storeListFragment!=null){
            storeListFragment.getByName(edi);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();

        CBShopCampus.setChecked(false);
        CBShopClassification.setChecked(false);
        CBShopCity.setChecked(false);
        storeListFragment = StoreListFragment.newInstance("商品列表");
        getChildFragmentManager().beginTransaction()
                .add(R.id.sale_bottom_fragment, storeListFragment).commit();
        initChackBox();

        return view;
    }

    private void initChackBox() {
        CBShopCity.setText(ChooseStoreSwoListFragment.mSearchConditions.getCity());
        if(ChooseStoreSwoListFragment.mSearchConditions.getCampus().equals("全部")){
            CBShopCampus.setText(ChooseStoreSwoListFragment.mSearchConditions.getUniversity());
        }else {
            CBShopCampus.setText(ChooseStoreSwoListFragment.mSearchConditions.getCampus());
        }
        CBShopClassification.setText(ChooseStoreSwoListFragment.mSearchConditions.getClassification() + "/" + ChooseStoreSwoListFragment.mSearchConditions.getSpecies());
    }

    private void showChooseSaleListFragment(CheckBox cb, int id) {

        if (chooseStoreSwoListFragment == null) {
            chooseStoreSwoListFragment = ChooseStoreSwoListFragment.newInstance("条件选择");
            getChildFragmentManager().beginTransaction()
                    .add(R.id.sale_bottom_fragment, chooseStoreSwoListFragment).commit();
            chooseStoreSwoListFragment.setShow(true);
            chooseStoreSwoListFragment.setRadioBtnId(id);
            chooseStoreSwoListFragment.setoClose(new ChooseStoreSwoListFragment.onClose() {
                @Override
                public void CloseFragment() {
                    CBShopCity.setChecked(false);
                    CBShopCampus.setChecked(false);
                    CBShopClassification.setChecked(false);
                    initChackBox();
                    chooseStoreSwoListFragment.setShow(false);

                        storeListFragment.getByChoose(ChooseStoreSwoListFragment.mSearchConditions.getCity(),ChooseStoreSwoListFragment.mSearchConditions.getUniversity(),
                                ChooseStoreSwoListFragment.mSearchConditions.getCampus(),ChooseStoreSwoListFragment.mSearchConditions.getClassification(),ChooseStoreSwoListFragment.mSearchConditions.getSpecies());

                }
            });
        } else if (cb.isChecked() == false) {
            getChildFragmentManager().beginTransaction()
                    .hide(chooseStoreSwoListFragment).commit();
            chooseStoreSwoListFragment.setShow(false);
        } else {
            getChildFragmentManager().beginTransaction()
                    .show(chooseStoreSwoListFragment).commit();
            chooseStoreSwoListFragment.setRadioBtnId(id);
            chooseStoreSwoListFragment.setShow(true);
        }

    }

    @OnClick({R.id.CB_shop_city, R.id.CB_shop_campus, R.id.CB_shop_classification})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.CB_shop_city:
                CBShopCampus.setChecked(false);
                CBShopClassification.setChecked(false);
                showChooseSaleListFragment(CBShopCity, 0);
                break;
            case R.id.CB_shop_campus:

                CBShopCity.setChecked(false);
                CBShopClassification.setChecked(false);
                showChooseSaleListFragment(CBShopCampus, 1);
                break;
            case R.id.CB_shop_classification:

                CBShopCampus.setChecked(false);
                CBShopCity.setChecked(false);
                showChooseSaleListFragment(CBShopClassification, 2);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }



}
