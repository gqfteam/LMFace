package com.lmface.signin.myInitiateSign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.courseinfo;
import com.lmface.pojo.user_msg;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 */

public class MtInitiateSignFragment  extends Fragment {
    private static final String ARG_TYPE = "type";
    private Realm realm;
    private Unbinder unbinder;
    private CompositeSubscription mcompositeSubscription;

    private int type;//0：临时，1：日常


    public static MtInitiateSignFragment newInstance(int type) {
        MtInitiateSignFragment fragment = new MtInitiateSignFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the food_listitem for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        realm=Realm.getDefaultInstance();
        mcompositeSubscription=new CompositeSubscription();

        if(type==0){
            //查询我发起的临时签到
            initTemporaryData();
        }else{
            //查询我发起的日常签到
            initDailyCourseData();
        }

        return view;
    }
    public void initTemporaryData(){
        Subscription subscription = NetWork.getSignService().selectCouresByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<courseinfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.getMessage());
                    }

                    @Override
                    public void onNext(List<courseinfo> datas) {

                        Log.i("gqf","onNext"+datas.toString());
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initDailyCourseData(){
        Subscription subscription = NetWork.getSignService().selectCouresByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<courseinfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.getMessage());
                    }

                    @Override
                    public void onNext(List<courseinfo> datas) {

                        Log.i("gqf","onNext"+datas.toString());
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }





}
