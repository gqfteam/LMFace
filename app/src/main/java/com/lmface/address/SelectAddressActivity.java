package com.lmface.address;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.user_address;
import com.lmface.pojo.user_msg;
import com.lmface.store.OrderGoodsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/5.
 */

public class SelectAddressActivity extends AppCompatActivity {


    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.order_address_list_toolbar)
    Toolbar orderAddressListToolbar;
    @BindView(R.id.select_address_list)
    RecyclerView selectAddressList;

    SelectAddressAdapter selectAddressAdapter;

    public void setToolbar(String str) {
        orderAddressListToolbar.setTitle(str);
        setSupportActionBar(orderAddressListToolbar);
        orderAddressListToolbar.setBackgroundResource(R.color.colorPrimary);
        orderAddressListToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        orderAddressListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_select_address_list);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("选择购物地址");
        initAddress();
    }
    public void initAddress(){
        Subscription subscription = NetWork.getUserAddressService().selectUserAddressByUserId(realm.where(user_msg.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<user_address>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<user_address> user_addresses) {
                        initList(user_addresses);
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void initList(List<user_address> user_addresses){
        if(user_addresses.size()==0){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("当前用户没有地址")
                    .setMessage("是否去设置收货地址")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(SelectAddressActivity.this, AddressListActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("否",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            dialog.dismiss();
                        }
                    });
            alert.create().show();
        }
        if(selectAddressAdapter==null){
            selectAddressAdapter=new SelectAddressAdapter(this,user_addresses);
            selectAddressList.setLayoutManager(new LinearLayoutManager(this));
            selectAddressList.setAdapter(selectAddressAdapter);
            selectAddressAdapter.setOnItemClickListener(new SelectAddressAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    setResult(OrderGoodsActivity.address_result_code,new Intent().putExtra("addressId",selectAddressAdapter.getDataItem(position).getAddressId()));
                    finish();
                }
            });
        }else{
            selectAddressAdapter.update(user_addresses);
        }
    }
}
