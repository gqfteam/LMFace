package com.lmface.signin.mySignHistory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmface.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 */

public class MtSignHistoryFragment  extends Fragment {
    private static final String ARG_TYPE = "type";
    private Realm realm;
    private Unbinder unbinder;
    private CompositeSubscription mcompositeSubscription;

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
        realm=Realm.getDefaultInstance();
        mcompositeSubscription=new CompositeSubscription();

        if(type==0){
            //查询我发起的临时签到

        }else{
            //查询我发起的日常签到

        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }





}
