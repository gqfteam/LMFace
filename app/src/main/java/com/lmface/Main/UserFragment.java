package com.lmface.Main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lmface.R;
import com.lmface.User.MyGoodsListActivity;
import com.lmface.address.AddressListActivity;
import com.lmface.store.ShopCarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

/**
 * Created by johe on 2017/1/5.
 */

public class UserFragment extends Fragment {
    Realm realm;
    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.my_goods_list_lin)
    LinearLayout myGoodsListLin;
    @BindView(R.id.my_shop_car_lin)
    LinearLayout myShopCarLin;
    @BindView(R.id.my_order_list_lin)
    LinearLayout myOrderListLin;

    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
    public interface mListener {
        public void changeActivity(
                Class activityClass);
    }

    private StoreFragment.mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (StoreFragment.mListener) activity;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();

        return view;
    }

    @OnClick({R.id.my_address_list_lin,R.id.my_goods_list_lin, R.id.my_shop_car_lin, R.id.my_order_list_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_goods_list_lin:
                mListener.changeActivity(MyGoodsListActivity.class);
                break;
            case R.id.my_shop_car_lin:
                mListener.changeActivity(ShopCarActivity.class);
                break;
            case R.id.my_order_list_lin:
                break;
            case R.id.my_address_list_lin:
                mListener.changeActivity(AddressListActivity.class);
                break;
        }
    }
}
