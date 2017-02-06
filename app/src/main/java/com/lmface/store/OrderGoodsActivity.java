package com.lmface.store;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lmface.R;
import com.lmface.address.SelectAddressActivity;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.goods_msg;
import com.lmface.pojo.goods_order;
import com.lmface.pojo.user_address;
import com.lmface.pojo.user_msg;
import com.lmface.view.AutoHeightLayoutManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/4.
 */

public class OrderGoodsActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.order_goods_toolbar)
    Toolbar orderGoodsToolbar;
    @BindView(R.id.order_goods_left_img)
    ImageView orderGoodsLeftImg;
    @BindView(R.id.user_name_txt)
    TextView userNameTxt;
    @BindView(R.id.user_phone_txt)
    TextView userPhoneTxt;
    @BindView(R.id.User_address_msg_lin)
    LinearLayout UserAddressMsgLin;
    @BindView(R.id.order_address_msg_txt)
    TextView orderAddressMsgTxt;
    @BindView(R.id.order_goods_right_img)
    ImageView orderGoodsRightImg;
    @BindView(R.id.select_address_rel)
    RelativeLayout selectAddressRel;
    @BindView(R.id.goods_order_list)
    RecyclerView goodsOrderList;
    @BindView(R.id.order_goods_pay)
    Button orderGoodsPay;

    OrderGoodsAdapter orderGoodsAdapter;
    @BindView(R.id.order_money_txt)
    TextView orderMoneyTxt;
    double orderMoney = 0;

    int addressId;

    public static int address_result_code = 1234;
    @BindView(R.id.behavior_goods_name)
    TextView behaviorGoodsName;
    @BindView(R.id.behavior_close)
    ImageView behaviorClose;
    @BindView(R.id.behavior_commit)
    Button behaviorCommit;
    @BindView(R.id.order_behavior_layout)
    LinearLayout orderBehaviorLayout;
    @BindView(R.id.tetroot)
    CoordinatorLayout tetroot;
    @BindView(R.id.user_password_edi)
    EditText userPasswordEdi;

    public void setToolbar(String str) {
        orderGoodsToolbar.setTitle("提交订单");
        orderGoodsToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(orderGoodsToolbar);
        orderGoodsToolbar.setBackgroundResource(R.color.colorPrimary);
        orderGoodsToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        orderGoodsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    ArrayList<Integer> goodsIds;
    ArrayList<Integer> goodsNums;
    List<goods_msg> goodsMsgs;

    List<goods_order> goodsOrders;
    BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_order);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("0");
        Bundle bundle = getIntent().getExtras();
        goodsIds = bundle.getIntegerArrayList("goodsIds");
        goodsNums = bundle.getIntegerArrayList("goodsNums");
        goodsMsgs = new ArrayList<>();
        goodsOrders = new ArrayList<>();
        getDatas(0);
        addressId = realm.where(user_msg.class).findFirst().getDefaultaddress();
        initAddressData(addressId);
        mBottomSheetBehavior = BottomSheetBehavior.from(this.findViewById(R.id.order_behavior_layout));
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setHideable(true);

    }

    public void initAddressData(int id) {
        Subscription subscription = NetWork.getUserAddressService().selectUserAddressById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<user_address>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(user_address user_address) {
                        initAddressLin(user_address);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initAddressLin(user_address user_address) {
        userNameTxt.setText(user_address.getName());
        userPhoneTxt.setText(user_address.getPhone());
        orderAddressMsgTxt.setText(user_address.getProvinces() + user_address.getCity() + user_address.getCounty() + user_address.getStreet() + user_address.getAddressTxt());
        selectAddressRel.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void getDatas(int index) {
        Subscription subscription = NetWork.getGoodsService().selectByGoodsId(goodsIds.get(index))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<goods_msg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(goods_msg goods_msg) {
                        //加入显示商品列表
                        goodsMsgs.add(goods_msg);
                        //增加订单
                        goods_order go = new goods_order();
                        go.setUserId(realm.where(user_msg.class).findFirst().getUserId());
                        go.setCourierId(goods_msg.getCourierId());
                        go.setOrderGoodsNum(goodsNums.get(goodsMsgs.size() - 1));
                        go.setOrderPrice(new BigDecimal(goods_msg.getGoodsprice() * goodsNums.get(goodsMsgs.size() - 1)));
                        go.setStoreUserId(goods_msg.getUserId());
                        go.setUserAddressId(addressId);
                        go.setGoodsId(goods_msg.getGoodsid());
                        goodsOrders.add(go);
                        orderMoney = orderMoney + goods_msg.getGoodsprice() * goodsNums.get(goodsMsgs.size() - 1);
                        if (goodsMsgs.size() == goodsIds.size()) {
                            //初始化列表
                            initList();
                        }
                        Log.i("gqf", "id" + goodsIds.get(goodsMsgs.size() - 1) + "num" + goodsNums.get(goodsMsgs.size() - 1));
                        if (goodsMsgs.size() < goodsIds.size()) {
                            //循环加载数据
                            getDatas(goodsMsgs.size());
                        }
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initList() {
        if (orderGoodsAdapter == null) {
            orderGoodsAdapter = new OrderGoodsAdapter(this, goodsMsgs, goodsNums);
            goodsOrderList.setLayoutManager(new AutoHeightLayoutManager(this));
            goodsOrderList.setAdapter(orderGoodsAdapter);
        } else {
            orderGoodsAdapter.update(goodsMsgs);
        }
        BigDecimal bmoney = new BigDecimal(orderMoney).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        orderMoneyTxt.setText(bmoney.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("gqf", "requestCode" + requestCode);
        if (requestCode == 0) {
            if (resultCode == address_result_code) {
                addressId = data.getIntExtra("addressId", realm.where(user_msg.class).findFirst().getDefaultaddress());
                Log.i("gqf", "addressId" + addressId);
                for (int i = 0; i < goodsOrders.size(); i++) {
                    goodsOrders.get(i).setUserAddressId(addressId);
                }
                initAddressData(addressId);
            }
        }
    }

    @OnClick({R.id.select_address_rel, R.id.order_goods_pay, R.id.behavior_close, R.id.behavior_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_address_rel:
                //跳转选择收货地址界面
                selectAddressRel.setEnabled(false);
                startActivityForResult(new Intent(OrderGoodsActivity.this, SelectAddressActivity.class), 0);
                break;
            case R.id.order_goods_pay:
                orderGoodsPay.setEnabled(false);
                //弹出底部栏输入密码进行支付
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.behavior_close:
                orderGoodsPay.setEnabled(true);
                //关闭
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.behavior_commit:
                //提交订单
                commitOrder();
                break;
        }
    }

    public void commitOrder() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if(!realm.where(user_msg.class).findFirst().getUserPassword().equals(userPasswordEdi.getText().toString())){
            //密码不正确，弹出dialog
            orderGoodsPay.setEnabled(true);
            createDialog("密码不正确",0);
        }else{
            Gson g=new Gson();
            Subscription subscription = NetWork.getGoodsOrderService().insertOrders(g.toJson(goodsOrders))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("gqf","onError"+e.getMessage());
                            orderGoodsPay.setEnabled(true);
                            createDialog("订单提交失败",0);
                        }
                        @Override
                        public void onNext(ResultCode resultCode) {
                            if(resultCode.getCode()==10000){
                                //提示成功
                                createDialog("订单提交成功",resultCode.getCode());
                            }else{
                                //提示失败
                                createDialog(resultCode.getMsg(),resultCode.getCode());
                            }
                            orderGoodsPay.setEnabled(true);
                        }
                    });
            mcompositeSubscription.add(subscription);
        }
    }

    public void createDialog(String msg,final int code){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        userPasswordEdi.setText("");
                        dialog.dismiss();
                        if(code==10000){
                            onBackPressed();
                        }
                    }
                });
        alert.create().show();
    }
}
