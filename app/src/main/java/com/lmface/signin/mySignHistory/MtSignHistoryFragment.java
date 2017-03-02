package com.lmface.signin.mySignHistory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.UserDailySignMsg;
import com.lmface.pojo.temporary_sign_msg;
import com.lmface.pojo.user_msg;

import java.util.List;

import butterknife.BindView;
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

public class MtSignHistoryFragment extends Fragment {
    private static final String ARG_TYPE = "type";
    @BindView(R.id.order_list_RecyclerView)
    RecyclerView orderListRecyclerView;
    private Realm realm;
    private Unbinder unbinder;
    private CompositeSubscription mcompositeSubscription;
    private DailySignHistoryAdapter mDailySignHistoryAdapter;
    private TemporarySignHistoryAdapter mTemporarySignHistoryAdapter;

    private int type;//0：临时，1：日常
    public static MtSignHistoryFragment newInstance(int type) {
        MtSignHistoryFragment fragment = new MtSignHistoryFragment();
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
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();

        if (type == 0) {
            //查询我被发起的临时签到历史
            initTemporaryData();
        } else {
            //查询我被发起的日常签到历史
            initDailyCourseData();
        }

        return view;
    }

    public void initTemporaryData() {
        Subscription subscription = NetWork.getSignService().selectTemporarySignByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<temporary_sign_msg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<temporary_sign_msg> datas) {
                        Log.i("gqf", "onNext" + datas.toString());
                        setUserTemporarySignMsgAdapter(datas);

                    }
                });
        mcompositeSubscription.add(subscription);
    }

    private void setUserTemporarySignMsgAdapter(List<temporary_sign_msg> datas) {
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTemporarySignHistoryAdapter = new TemporarySignHistoryAdapter(datas, getActivity());
        orderListRecyclerView.setAdapter(mTemporarySignHistoryAdapter);
    }


    public void initDailyCourseData() {
        Subscription subscription = NetWork.getSignService().selectDailySignByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserDailySignMsg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<UserDailySignMsg> datas) {
                        Log.i("gqf", "onNext" + datas.toString());
                        setUserDailySignMsgAdapter(datas);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    private void setUserDailySignMsgAdapter(List<UserDailySignMsg> datas) {
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDailySignHistoryAdapter = new DailySignHistoryAdapter(datas, getActivity());
        orderListRecyclerView.setAdapter(mDailySignHistoryAdapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }


}
