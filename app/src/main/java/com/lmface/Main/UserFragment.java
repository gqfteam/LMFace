package com.lmface.Main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.User.MyGoodsListActivity;
import com.lmface.User.UserInfoActivity;
import com.lmface.pojo.user_msg;
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
    @BindView(R.id.userName_tv)
    TextView userNameTv;
    @BindView(R.id.userId_tv)
    TextView userIdTv;


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

    private user_msg userMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        userMsg = realm.where(user_msg.class).findFirst();
        Log.e("Daniel",userMsg.toString());
        if (userMsg.getNickname()==null) {
            userNameTv.setText(userMsg.getUserName());
        }else {

            userNameTv.setText(userMsg.getNickname());
        }
        userIdTv.setText("学生Id:"+userMsg.getUserId());

        return view;
    }


    @OnClick({R.id.my_goods_list_lin, R.id.my_shop_car_lin, R.id.my_order_list_lin,R.id.image, R.id.userName_tv, R.id.userId_tv})
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
            case R.id.image:
                mListener.changeActivity(UserInfoActivity.class);
                break;
            case R.id.userName_tv:
                mListener.changeActivity(UserInfoActivity.class);
                break;
            case R.id.userId_tv:
                mListener.changeActivity(UserInfoActivity.class);
                break;
        }
    }
}
