package com.lmface.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.order_goods_usermsg;
import com.lmface.pojo.user_msg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/7.
 */

public class StoreOrderListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";
    @BindView(R.id.order_list_RecyclerView)
    RecyclerView orderListRecyclerView;
    private int type;
    private Realm realm;
    private Unbinder unbinder;
    private CompositeSubscription mcompositeSubscription;
    private MyStoreOrderListAdapter myOrderListAdapter;



    public static StoreOrderListFragment newInstance(int type) {
        StoreOrderListFragment fragment = new StoreOrderListFragment();
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    public void initData(){
        Subscription subscription = NetWork.getGoodsOrderService().selectByStoreUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                //列表类型转化
                .flatMap(new Func1<List<order_goods_usermsg>, Observable<order_goods_usermsg>>() {
                    @Override
                    public Observable<order_goods_usermsg> call(List<order_goods_usermsg> seats) {

                        return Observable.from(seats);
                    }
                })
                //过滤
                .filter(new Func1<order_goods_usermsg, Boolean>() {
                    @Override
                    public Boolean call(order_goods_usermsg ogms) {
                        switch (type) {
                            case 1://statu-1
                                return ogms.getOrderStatus()==-1;
                            case 2://statu0
                                return ogms.getOrderStatus()==0;
                            case 3://statu1
                                return ogms.getOrderStatus()==1;
                            case 4://statu2
                                return ogms.getOrderStatus()==2;
                            case 5://statu3
                                return ogms.getOrderStatus()==3;
                        }
                        return true;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<order_goods_usermsg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<order_goods_usermsg> order_goods_usermsgs) {
                        initList(order_goods_usermsgs);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initList(List<order_goods_usermsg> ogus) {
        myOrderListAdapter = new MyStoreOrderListAdapter(getContext(), ogus);
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderListRecyclerView.setAdapter(myOrderListAdapter);
        myOrderListAdapter.setOnItemClickListener(new MyStoreOrderListAdapter.MyItemClickListener() {
            @Override
            public void onChangeStatu() {
                initData();
            }

            @Override
            public void showDialog(final int position) {
                CourierDialogFragment dialog = new CourierDialogFragment();
                dialog.show(getChildFragmentManager(), "CourierDialogFragment");
                dialog.setmLinsener(new CourierDialogFragment.linsener() {
                    @Override
                    public void courierNum(String num) {
                        myOrderListAdapter.setCourierNum(num,position);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();


    }
}
