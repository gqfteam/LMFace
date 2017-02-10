package com.lmface.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.goods_msg_car;
import com.lmface.pojo.user_msg;

import java.math.BigDecimal;
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
 * Created by johe on 2017/1/30.
 */

public class ShopCarActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.shop_car_toolbar)
    Toolbar shopCarToolbar;
    @BindView(R.id.shop_car_list)
    RecyclerView shopCarList;
    @BindView(R.id.shop_car_select_all)
    CheckBox shopCarSelectAll;
    @BindView(R.id.all_select_goods_price)
    TextView allSelectGoodsPrice;
    @BindView(R.id.all_select_goods_courier)
    TextView allSelectGoodsCourier;
    @BindView(R.id.all_price_lin)
    LinearLayout allPriceLin;

    ShopCarListAdapter shopCarListAdapter;

    boolean isEdi = false;
    @BindView(R.id.shop_car_price_lin)
    LinearLayout shopCarPriceLin;
    @BindView(R.id.shop_car_pay)
    Button shopCarPay;
    @BindView(R.id.shop_car_delect_all)
    Button shopCarDelectAll;

    public void setToolbar(String str) {
        shopCarToolbar.setTitle("购物车(" + str + ")");
        shopCarToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(shopCarToolbar);
        shopCarToolbar.setBackgroundResource(R.color.colorPrimary);
        shopCarToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        shopCarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("0");
        inifShopCarData();

        shopCarSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shopCarListAdapter.selectAll(isChecked);
                initPriceView();
            }
        });

    }

    public void initPriceView(){
        //获取全部价格
        BigDecimal bmoney=new BigDecimal(shopCarListAdapter.getSelectGoodsPrice()+shopCarListAdapter.getSelectGoodsCourier()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        allSelectGoodsPrice.setText("¥"+bmoney);
        //获取全部邮费
        allSelectGoodsCourier.setText("运费："+shopCarListAdapter.getSelectGoodsCourier()+"元");
    }
    public void inifShopCarData() {
        Subscription subscription = NetWork.getShopCarService().selectShopCarByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<goods_msg_car>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<goods_msg_car> goods_msg_cars) {
                        for(int i=0;i<goods_msg_cars.size();i++){
                            if(goods_msg_cars.get(i).getGoodsnum()<=0){
                                goods_msg_car gmc=goods_msg_cars.get(i);
                                goods_msg_cars.remove(i);
                                goods_msg_cars.add(gmc);
                            }
                        }

                        initListView(goods_msg_cars);
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void initListView(List<goods_msg_car> goods_msg_cars) {
        if (shopCarListAdapter == null) {
            shopCarListAdapter = new ShopCarListAdapter(this, goods_msg_cars);
            shopCarList.setLayoutManager(new LinearLayoutManager(this));
            shopCarList.setAdapter(shopCarListAdapter);
            shopCarListAdapter.setOnItemClickListener(new ShopCarListAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {

                }
                @Override
                public void changePrice() {
                    initPriceView();
                }
                @Override
                public void isSelectAll(Boolean is) {
                    Log.i("gqf",is+"shopCarSelectAll"+shopCarSelectAll.isChecked());
                    if(is){
                        if(shopCarSelectAll.isChecked()==false){
                            shopCarSelectAll.setChecked(true);
                        }
                    }
                    if(!is){
                        if(shopCarSelectAll.isChecked()==true){
                            shopCarSelectAll.setChecked(false);
                        }
                    }
                }
                @Override
                public void selectNum(int num) {
                    setToolbar(num+"");
                    shopCarPay.setText("结算("+num+")");
                }
            });
        } else {
            shopCarListAdapter.update(goods_msg_cars);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdi==false) {
            getMenuInflater().inflate(R.menu.shop_car_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.shop_car_menu2, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (shopCarListAdapter != null) {
            if (item.getItemId() == R.id.shop_car_editor) {
                //切换为编辑状态
                ediChange(true);
            } else if (item.getItemId() == R.id.shop_car_ok) {
                //切换为非编辑状态
                ediChange(false);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void ediChange(boolean is){
        isEdi = is;
        shopCarListAdapter.updataStatu(isEdi);
        //切换底部布局
        ediLayout(isEdi);
        invalidateOptionsMenu();
    }

    public void ediLayout(boolean is) {
        if (is) {
            shopCarPay.setVisibility(View.GONE);
            shopCarPriceLin.setVisibility(View.GONE);
            shopCarDelectAll.setVisibility(View.VISIBLE);
            allSelectGoodsCourier.setVisibility(View.GONE);
        }else{
            shopCarPay.setVisibility(View.VISIBLE);
            shopCarPriceLin.setVisibility(View.VISIBLE);
            shopCarDelectAll.setVisibility(View.GONE);
            allSelectGoodsCourier.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
        shopCarListAdapter.Destory();
    }


    @OnClick({R.id.shop_car_pay, R.id.shop_car_delect_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_car_pay:
                //跳转结算界面，获得所有选中的商品的购物车carid，isShopCar=true;
                Intent intent=new Intent(ShopCarActivity.this,OrderGoodsActivity.class);
                intent.putExtras(shopCarListAdapter.getIntent());
                startActivity(intent);
                break;
            case R.id.shop_car_delect_all:
                //调用adapter中的删除函数
                shopCarListAdapter.batchDelect();
                ediChange(false);
                shopCarSelectAll.setChecked(false);
                break;
        }
    }
}
