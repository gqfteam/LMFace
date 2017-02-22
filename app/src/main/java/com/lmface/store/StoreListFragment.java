package com.lmface.store;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.goods_msg;
import com.lmface.util.in.srain.cube.views.ptr.PtrClassicFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrDefaultHandler;
import com.lmface.util.in.srain.cube.views.ptr.PtrFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrHandler;
import com.lmface.util.myPullToRefreshHeader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/1/20.
 */

public class StoreListFragment extends Fragment {
    Realm realm;
    StoreListAdapter storeListAdapter = null;
    @BindView(R.id.sale_fragment_list)
    RecyclerView saleFragmentList;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.hp_sc_ptr)
    PtrClassicFrameLayout mPtrFrame;
    myPullToRefreshHeader header;
    @BindView(R.id.shop_list_one_runing)
    TextView shopListOneRuning;
    @BindView(R.id.shop_list_lin_anim)
    LinearLayout shopListLinAnim;

    List<goods_msg> goodsMsgs;

    public static StoreListFragment newInstance(String param1) {
        StoreListFragment fragment = new StoreListFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        getByChoose(ChooseStoreSwoListFragment.mSearchConditions.getCity(), ChooseStoreSwoListFragment.mSearchConditions.getUniversity(),
                ChooseStoreSwoListFragment.mSearchConditions.getCampus(), ChooseStoreSwoListFragment.mSearchConditions.getClassification(), ChooseStoreSwoListFragment.mSearchConditions.getSpecies());


        return view;
    }

    public void initPullToRefresh() {
        header = new myPullToRefreshHeader(getActivity());

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
                        getByChoose(ChooseStoreSwoListFragment.mSearchConditions.getCity(), ChooseStoreSwoListFragment.mSearchConditions.getUniversity(),
                                ChooseStoreSwoListFragment.mSearchConditions.getCampus(), ChooseStoreSwoListFragment.mSearchConditions.getClassification(), ChooseStoreSwoListFragment.mSearchConditions.getSpecies());

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

    AnimationDrawable animation;

    public void runingAnim() {
        shopListLinAnim.setVisibility(View.VISIBLE);
        saleFragmentList.setVisibility(View.INVISIBLE);
        shopListOneRuning.setBackgroundResource(R.drawable.shop_list_one_anim);
        animation = (AnimationDrawable) shopListOneRuning.getBackground();
        animation.start();
    }

    Observer<List<goods_msg>> observer = new Observer<List<goods_msg>>() {
        @Override
        public void onCompleted() {
            Log.i("gqf", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.i("gqf", "onError" + e.toString());
        }

        @Override
        public void onNext(List<goods_msg> goods_msgs) {
            Log.i("gqf", "onNext" + goods_msgs.toString());
            goodsMsgs =goods_msgs;
            initList(goods_msgs);
        }
    };

    public void initList(List<goods_msg> goods_msgs) {
        shopListLinAnim.setVisibility(View.GONE);
        saleFragmentList.setVisibility(View.VISIBLE);
        if (storeListAdapter == null) {
            storeListAdapter = new StoreListAdapter(getActivity(), goods_msgs);
            saleFragmentList.setLayoutManager(new LinearLayoutManager(getActivity()));
            saleFragmentList.setAdapter(storeListAdapter);

            initPullToRefresh();
        } else {
            storeListAdapter.update(goods_msgs);
        }
    }

    public void getByChoose(String goodscity, String colloegename, String campusname, String classificationname, String speciesname) {
        runingAnim();
        Subscription subscription = NetWork.getGoodsService().selectByChoose(goodscity, colloegename, campusname, classificationname, speciesname)
                .subscribeOn(Schedulers.io())
                //列表类型转化
                .flatMap(new Func1<List<goods_msg>, Observable<goods_msg>>() {
                    @Override
                    public Observable<goods_msg> call(List<goods_msg> seats) {

                        return Observable.from(seats);
                    }
                })
                //过滤
                .filter(new Func1<goods_msg, Boolean>() {
                    @Override
                    public Boolean call(goods_msg ogms) {
                       if(ogms.getGoodsnum()==-1){
                           return false;
                       }
                        return true;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mcompositeSubscription.add(subscription);
    }


    public void getByName(String name) {
        Subscription subscription = NetWork.getGoodsService().selectByGoodsName(name)
                .subscribeOn(Schedulers.io())
                //列表类型转化
                .flatMap(new Func1<List<goods_msg>, Observable<goods_msg>>() {
                    @Override
                    public Observable<goods_msg> call(List<goods_msg> seats) {

                        return Observable.from(seats);
                    }
                })
                //过滤
                .filter(new Func1<goods_msg, Boolean>() {
                    @Override
                    public Boolean call(goods_msg ogms) {
                        if(ogms.getGoodsnum()==-1){
                            return false;
                        }
                        return true;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mcompositeSubscription.add(subscription);
    }
}
