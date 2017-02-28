package com.lmface.Main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lmface.R;
import com.lmface.User.EdiDialogFragment;
import com.lmface.User.MyGoodsListActivity;
import com.lmface.User.UserInfoActivity;
import com.lmface.address.AddressListActivity;
import com.lmface.huanxin.AddFriendsListActivity;
import com.lmface.huanxin.ContactActivity;
import com.lmface.network.NetWork;
import com.lmface.order.MyOrderListActivity;
import com.lmface.order.MyStoreOrderListActivity;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.user_msg;
import com.lmface.store.ShopCarActivity;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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

    CompositeSubscription compositeSubscription;
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

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

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
        compositeSubscription=new CompositeSubscription();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        userMsg = realm.where(user_msg.class).findFirst();
        userNameTv.setText(userMsg.getNickname());
        userIdTv.setText(userMsg.getPersonalnote());
        if (userMsg.getHeadimg() != null) {
            if (!userMsg.getHeadimg().equals("")) {
                Picasso.with(getActivity()).load(userMsg.getHeadimg())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(image);
            }
        }
    }

    public void updataUser(){
        Subscription editNickname = NetWork.getUserService().selectUserByName(realm.where(user_msg.class).findFirst().getUserName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<user_msg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(user_msg user_msg) {

                        realm.beginTransaction();
                        userMsg.setUsermoney(user_msg.getUsermoney());
                        realm.copyToRealmOrUpdate(userMsg);
                        realm.close();
                    }
                });

        compositeSubscription.add(editNickname);
    }

    @OnClick({R.id.my_add_friends_lin,R.id.my_friends_list_lin, R.id.my_top_up_lin,R.id.my_address_list_lin, R.id.my_goods_list_lin, R.id.my_shop_car_lin, R.id.my_order_list_lin, R.id.my_store_order_list_lin, R.id.image, R.id.userName_tv, R.id.userId_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_goods_list_lin:
                mListener.changeActivity(MyGoodsListActivity.class);
                break;
            case R.id.my_shop_car_lin:
                mListener.changeActivity(ShopCarActivity.class);
                break;
            case R.id.my_order_list_lin:
                mListener.changeActivity(MyOrderListActivity.class);
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
            case R.id.my_store_order_list_lin:
                mListener.changeActivity(MyStoreOrderListActivity.class);
                break;
            case R.id.my_address_list_lin:
                mListener.changeActivity(AddressListActivity.class);
                break;
            case R.id.my_friends_list_lin:
                mListener.changeActivity(ContactActivity.class);
                break;
            case R.id.my_top_up_lin:
                //充值
                editNameDialog = new EdiDialogFragment();
                editNameDialog.setTitle("充值");
                editNameDialog.setInputType( InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editNameDialog.show(getChildFragmentManager(), "EditNameDialog");
                editNameDialog.setDimssLinsener(new EdiDialogFragment.DimssLinsener() {
                    @Override
                    public void fragmentDimss() {
                        updataUser();
                        editNameDialog=null;
                    }

                    @Override
                    public void onOk(String ediTxt) {
                        changeMoney(ediTxt);
                    }
                });
                break;
            case R.id.my_add_friends_lin:
                mListener.changeActivity(AddFriendsListActivity.class);
                break;

        }
    }
    EdiDialogFragment editNameDialog;
    public void changeMoney(String ediTxt) {


        BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(ediTxt));

        Subscription editNickname = NetWork.getUserService().updateUserInfoMoneyByUserId(realm.where(user_msg.class).findFirst().getUserId(), bigDecimal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();

                    }

                    @DebugLog
                    @Override
                    public void onNext(ResultCode resultCode) {

                        if(editNameDialog!=null) {
                            if (resultCode.getCode() == 10000) {
                                Toast.makeText(getActivity(),"充值成功",Toast.LENGTH_SHORT).show();
                                editNameDialog.setEnd(true);
                            } else {
                                editNameDialog.setEnd(false);
                            }
                        }

                    }
                });

        compositeSubscription.add(editNickname);


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }
}
