package com.lmface.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.goods_msg;
import com.lmface.pojo.user_msg;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
 * Created by johe on 2017/1/29.
 */

public class GoodsDetailsActivity extends AppCompatActivity {


    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.goods_details_toolbar)
    Toolbar goodsDetailsToolbar;
    @BindView(R.id.goods_details_img)
    ImageView goodsDetailsImg;
    @BindView(R.id.goods_details_img_name)
    TextView goodsDetailsImgName;
    @BindView(R.id.goods_details_top_lin)
    LinearLayout goodsDetailsTopLin;
    @BindView(R.id.goods_details_num_img)
    ImageView goodsDetailsNumImg;
    @BindView(R.id.goods_details_num)
    TextView goodsDetailsNum;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.goods_details_courier_img)
    ImageView goodsDetailsCourierImg;
    @BindView(R.id.goods_details_courier)
    TextView goodsDetailsCourier;
    @BindView(R.id.goods_details_classfication_img)
    ImageView goodsDetailsClassficationImg;
    @BindView(R.id.goods_details_classfication)
    TextView goodsDetailsClassfication;
    @BindView(R.id.goods_details_username_img)
    ImageView goodsDetailsUsernameImg;
    @BindView(R.id.goods_details_username)
    TextView goodsDetailsUsername;
    @BindView(R.id.goods_details_phone_img)
    ImageView goodsDetailsPhoneImg;
    @BindView(R.id.goods_details_phone)
    TextView goodsDetailsPhone;
    @BindView(R.id.goods_details_loaction_img)
    ImageView goodsDetailsLoactionImg;
    @BindView(R.id.goods_details_loaction)
    TextView goodsDetailsLoaction;
    @BindView(R.id.goods_details_imgs_img)
    ImageView goodsDetailsImgsImg;
    @BindView(R.id.goods_details_imgs)
    TextView goodsDetailsImgs;
    @BindView(R.id.goods_details_img1)
    ImageView goodsDetailsImg1;
    @BindView(R.id.goods_details_img2)
    ImageView goodsDetailsImg2;
    @BindView(R.id.goods_details_img3)
    ImageView goodsDetailsImg3;
    @BindView(R.id.goods_details_txt_img)
    ImageView goodsDetailsTxtImg;
    @BindView(R.id.goods_details_txt)
    TextView goodsDetailsTxt;
    @BindView(R.id.goods_details_txt_details)
    TextView goodsDetailsTxtDetails;
    @BindView(R.id.DampView)
    com.lmface.view.DampView DampView;

    int goodsId = -1;
    goods_msg goodsMsg;
    @BindView(R.id.goods_details_price)
    TextView goodsDetailsPrice;
    @BindView(R.id.goods_details_atgoodsuser)
    LinearLayout goodsDetailsAtgoodsuser;
    @BindView(R.id.goods_details_foundlike)
    LinearLayout goodsDetailsFoundlike;
    @BindView(R.id.goods_details_to_top)
    LinearLayout goodsDetailsToTop;
    @BindView(R.id.goods_details_addshopcar)
    Button goodsDetailsAddshopcar;
    @BindView(R.id.goods_details_buynow)
    Button goodsDetailsBuynow;


    BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.behavior_goods_name)
    TextView behaviorGoodsName;
    @BindView(R.id.behavior_close)
    ImageView behaviorClose;
    @BindView(R.id.behavior_add)
    Button behaviorAdd;
    @BindView(R.id.behavior_jian)
    Button behaviorJian;
    @BindView(R.id.behavior_commit)
    Button behaviorCommit;

    int buyNum = 1;
    @BindView(R.id.behavior_goods_num)
    TextView behaviorGoodsNum;
    @BindView(R.id.behavior_buy_num)
    TextView behaviorBuyNum;

    public void setToolbar(String str) {
        goodsDetailsToolbar.setTitle("商品详情");
        goodsDetailsToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(goodsDetailsToolbar);
        goodsDetailsToolbar.setNavigationIcon(R.drawable.travel__poi_weak_deal_back);
        goodsDetailsToolbar.setBackgroundResource(R.color.whitesmoke);
        goodsDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(this.findViewById(R.id.tab_layout));
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setHideable(true);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("");

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        DampView.setImageView(goodsDetailsImg);
        goodsId = getIntent().getIntExtra("goodsId", 0);
        initData(goodsId);

    }

    public void initData(int id) {
        Subscription subscription = NetWork.getGoodsService().selectByGoodsId(id)
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
                        goodsMsg = goods_msg;
                        initViewMsg(goods_msg);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initViewMsg(goods_msg goods_msg) {
        goodsDetailsImgName.setText(goods_msg.getGoodsname());
        goodsDetailsCourier.setText(goods_msg.getCourierName() + goods_msg.getCourierMoney() + "元");
        goodsDetailsClassfication.setText("分类：" + goods_msg.getGoodsclassification() + "/" + goods_msg.getSpeciesname());
        goodsDetailsNum.setText("数量：" + goods_msg.getGoodsnum());
        goodsDetailsLoaction.setText(goods_msg.getGoodscity() + "/" + goods_msg.getCollegename() + "/" + goods_msg.getCampusname());
        goodsDetailsPhone.setText(goods_msg.getUserphonenum() + "");
        goodsDetailsTxt.setText(goods_msg.getGoodsdetails());
        goodsDetailsPrice.setText(goods_msg.getGoodsprice() + "元");
        behaviorGoodsNum.setText("库存：" + goods_msg.getGoodsnum());
        behaviorGoodsName.setText(goods_msg.getGoodsname());
        if (goods_msg.getGoodsimgaddress1() != null) {
            if (!goods_msg.getGoodsimgaddress1().equals("")) {
                Picasso.with(this).load(goods_msg.getGoodsimgaddress1())
                        .placeholder(R.drawable.user_bg)
                        .error(R.drawable.user_bg)
                        .into(goodsDetailsImg);
                Picasso.with(this).load(goods_msg.getGoodsimgaddress1())
                        .placeholder(R.drawable.user_bg)
                        .error(R.drawable.user_bg)
                        .into(goodsDetailsImg1);
            }
        }
        if (goods_msg.getGoodsimgaddress2() != null) {
            if (!goods_msg.getGoodsimgaddress2().equals("")) {
                Picasso.with(this).load(goods_msg.getGoodsimgaddress2())
                        .placeholder(R.drawable.user_bg)
                        .error(R.drawable.user_bg)
                        .into(goodsDetailsImg2);
            }
        }
        if (goods_msg.getGoodsimgaddress3() != null) {
            if (!goods_msg.getGoodsimgaddress3().equals("")) {
                Picasso.with(this).load(goods_msg.getGoodsimgaddress3())
                        .placeholder(R.drawable.user_bg)
                        .error(R.drawable.user_bg)
                        .into(goodsDetailsImg3);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }

    @OnClick({R.id.behavior_close, R.id.behavior_add, R.id.behavior_jian, R.id.behavior_commit, R.id.goods_details_atgoodsuser, R.id.goods_details_foundlike, R.id.goods_details_to_top, R.id.goods_details_addshopcar, R.id.goods_details_buynow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_details_atgoodsuser:
                //跳转聊天页面
                break;
            case R.id.goods_details_foundlike:
                //跳转主页，并传值gqf
                break;
            case R.id.goods_details_to_top:
                //返回顶部
                DampView.fullScroll(ScrollView.FOCUS_UP);
                break;
            case R.id.goods_details_addshopcar:
                //弹出popuwindow，购物车
                showBottomSheetBehavior(1);
                break;
            case R.id.goods_details_buynow:
                //弹出popuwindow，购买
                showBottomSheetBehavior(2);
                break;
            case R.id.behavior_close:
                Log.i("gqf", "behavior_close");
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.behavior_add:
                addOrJian(1);
                break;
            case R.id.behavior_jian:
                addOrJian(2);
                break;
            case R.id.behavior_commit:
                behaviorCommit.setEnabled(false);
                if (statu == 1) {
                    addShopCar(goodsId,buyNum);
                } else if (statu == 2) {
                    buyNow();
                }
                break;
        }
    }

    public void addOrJian(int s) {
        if (s == 1) {
            buyNum++;
        } else {
            buyNum--;
        }
        if (buyNum > 1 && buyNum < goodsMsg.getGoodsnum()) {
            behaviorJian.setEnabled(true);
            behaviorAdd.setEnabled(true);
        }

        if (buyNum == 1) {
            behaviorJian.setEnabled(false);
        }
        if (buyNum == goodsMsg.getGoodsnum()) {

            behaviorAdd.setEnabled(false);
        }


        behaviorBuyNum.setText("" + buyNum);
    }

    int statu = 0;

    public void showBottomSheetBehavior(int s) {
        Log.i("gqf", "showBottomSheetBehavior");
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (s == 1) {
            //加入购物车
            statu = 1;
        } else {
            //购买
            statu = 2;
        }
        if (goodsMsg.getGoodsnum() == 0) {
            behaviorJian.setEnabled(false);
            behaviorAdd.setEnabled(false);
            buyNum = 0;

        } else {
            buyNum = 1;
        }
        behaviorBuyNum.setText("" + buyNum);
    }

    public void addShopCar(int goodsId,int num) {
        //加入购物车，然后关闭mBottomSheetBehavior
        Subscription subscription = NetWork.getShopCarService().insertShopCar(realm.where(user_msg.class).findFirst().getUserId(),goodsId,num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        behaviorCommit.setEnabled(true);
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        if(resultCode.getCode()==10000){
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),resultCode.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                        behaviorCommit.setEnabled(true);
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void buyNow() {

        //跳转购买界面，传递goodsid，购买数量，isShopCar=false
        Intent intent=new Intent(GoodsDetailsActivity.this,OrderGoodsActivity.class);
        Bundle bundle=new Bundle();
        ArrayList<Integer> goodsIds=new ArrayList<>();
        goodsIds.add(goodsId);
        bundle.putIntegerArrayList("goodsIds",goodsIds);
        ArrayList<Integer> goodsNums=new ArrayList<>();
        goodsNums.add(buyNum);
        bundle.putIntegerArrayList("goodsNums",goodsNums);
        intent.putExtras(bundle);
        behaviorCommit.setEnabled(true);
        startActivity(intent);
    }
}
