package com.lmface.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lmface.R;
import com.lmface.pojo.SaleChooseModel;
import com.lmface.pojo.SearchConditions;
import com.lmface.util.ReadJson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by johe on 2017/1/20.
 */

public class ChooseStoreSwoListFragment extends Fragment {
    Realm realm;
    @BindView(R.id.choseListViewOne)
    ListView choseListViewOne;
    @BindView(R.id.choseListViewTwo)
    ListView choseListViewTwo;
    @BindView(R.id.chose_popu_lin)
    LinearLayout chosePopuLin;
    @BindView(R.id.chose_popu_back)
    View chosePopuBack;

    boolean isShow=false;

    private String nowChoose;
    private int RadioBtnId = 0;
    private ReadJson rj;
    private ChooseSaleOneAdapter mChooseSaleOneAdapter;
    private ChooseSaleTwoAdapter mChooseSaleTwoAdapter;
    ArrayList<SaleChooseModel> datas;
    SaleChooseModel have;
    public static SearchConditions mSearchConditions=new SearchConditions();
    View view;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void setRadioBtnId(int radioBtnId) {
        RadioBtnId = radioBtnId;
        if (view != null) {
            if (mChooseSaleTwoAdapter != null) {
                mChooseSaleTwoAdapter.update(null);
            }
            initList(RadioBtnId);
        }
    }

    public static ChooseStoreSwoListFragment newInstance(String param1) {
        ChooseStoreSwoListFragment fragment = new ChooseStoreSwoListFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choose_sale_two_list, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        initList(RadioBtnId);
        return view;
    }
    public void initList(int id) {
        //读取json,加载数据
        String json = null;
        String nowStr=null;
        if (id == 0) {
            json = getActivity().getString(R.string.city_json);
            nowStr=mSearchConditions.getProvince();
        } else if (id == 1) {
            json = getActivity().getString(R.string.university_json);
            nowStr=mSearchConditions.getUniversity();
        } else {
            json = getActivity().getString(R.string.classification_json);
            nowStr=mSearchConditions.getClassification();
        }
        rj = ReadJson.getInstance();
        datas = rj.readSaleTopChooseJson(json);
        mChooseSaleOneAdapter = new ChooseSaleOneAdapter(getContext(), datas);
        mChooseSaleOneAdapter.setChooseid(RadioBtnId);
        choseListViewOne.setAdapter(mChooseSaleOneAdapter);

        for(int i=0;i<datas.size();i++){
            if(datas.get(i).getName().equals(nowStr)){
                choseListViewOne.setSelection(i);
                initTwoListByPosition(i);
            }
        }
        choseListViewOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initTwoListByPosition(position);
            }
        });

    }

    public void initTwoListByPosition(int position){
        have = datas.get(position);

        if (RadioBtnId == 0) {
            mChooseSaleOneAdapter.setProvince(datas.get(position).getName());
            nowChoose = ChooseStoreSwoListFragment.mSearchConditions.getProvince();
        } else if (RadioBtnId == 1) {
            mChooseSaleOneAdapter.setUniversity(datas.get(position).getName());
            nowChoose = ChooseStoreSwoListFragment.mSearchConditions.getUniversity();
        } else {
            mChooseSaleOneAdapter.setClassification(datas.get(position).getName());
            nowChoose = ChooseStoreSwoListFragment.mSearchConditions.getClassification();
        }
        mChooseSaleOneAdapter.update(datas);
        initTwoList(have, nowChoose);
    }

    public void initTwoList(SaleChooseModel datas, String nowChoose) {
        if (mChooseSaleTwoAdapter == null) {
            mChooseSaleTwoAdapter = new ChooseSaleTwoAdapter(getContext(), datas);

            mChooseSaleTwoAdapter.setChooseid(RadioBtnId);
            mChooseSaleTwoAdapter.setmSaleChooseModelName(nowChoose);
            choseListViewTwo.setAdapter(mChooseSaleTwoAdapter);
            choseListViewTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (RadioBtnId == 0) {
                        mChooseSaleTwoAdapter.setCity(have.getHave().get(position));
                        ChooseStoreSwoListFragment.mSearchConditions.setCity(have.getHave().get(position));
                        ChooseStoreSwoListFragment.mSearchConditions.setProvince(have.getName());
                        //可对学校数据联动


                    } else if (RadioBtnId == 1) {
                        mChooseSaleTwoAdapter.setCampus(have.getHave().get(position));
                        ChooseStoreSwoListFragment.mSearchConditions.setCampus(have.getHave().get(position));
                        ChooseStoreSwoListFragment.mSearchConditions.setUniversity(have.getName());
                    } else {
                        mChooseSaleTwoAdapter.setSpecies(have.getHave().get(position));
                        ChooseStoreSwoListFragment.mSearchConditions.setSpecies(have.getHave().get(position));
                        ChooseStoreSwoListFragment.mSearchConditions.setClassification(have.getName());
                    }
                    mChooseSaleTwoAdapter.setmSaleChooseModelName(have.getName());
                    mChooseSaleTwoAdapter.update(have);
                    //关闭当前fragment
                    hide();
                }
            });
        } else {
            mChooseSaleTwoAdapter.setmSaleChooseModelName(nowChoose);
            mChooseSaleTwoAdapter.setChooseid(RadioBtnId);
            mChooseSaleTwoAdapter.update(have);
        }
    }
    @OnClick(R.id.chose_popu_back)
    public void onClick() {
        //点击隐藏当前条件选择fragment
        hide();
    }

    public void hide() {
        getParentFragment().getChildFragmentManager().beginTransaction()
                .hide(this).commit();
        if (oClose != null) {
            oClose.CloseFragment();
        }
    }

    public onClose getoClose() {
        return oClose;
    }

    public void setoClose(onClose oClose) {
        this.oClose = oClose;
    }

    onClose oClose;

    public interface onClose {
        public void CloseFragment();
    }
}
