package com.lmface.address;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.user_address;
import com.lmface.pojo.user_msg;

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
 * Created by johe on 2017/2/3.
 */

public class AddressListActivity extends AppCompatActivity {


    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.address_list_toolbar)
    Toolbar addressListToolbar;
    @BindView(R.id.address_list)
    RecyclerView addressList;

    AddressListAdapter addressListAdapter;
    @BindView(R.id.add_address_btn)
    Button addAddressBtn;

    public void setToolbar(String str) {
        addressListToolbar.setTitle(str);
        setSupportActionBar(addressListToolbar);
        addressListToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        addressListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("管理收货地址");

    }

    @Override
    protected void onStart() {
        super.onStart();
        initAddressData();
    }

    public void initAddressData() {
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
                        Log.i("gqf","user_addresses"+user_addresses.toString());
                        initListView(user_addresses);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initListView(List<user_address> user_addresses) {
        if (addressListAdapter == null) {
            addressListAdapter = new AddressListAdapter(this, user_addresses);
            addressList.setLayoutManager(new LinearLayoutManager(this));
            addressList.setAdapter(addressListAdapter);
            addressListAdapter.setOnItemClickListener(new AddressListAdapter.MyItemClickListener() {
                @Override
                public void changeActivity(int addressId) {
                    Intent intent = new Intent(AddressListActivity.this, EdiAddressActivity.class);
                    intent.putExtra("addressId", addressId);
                    startActivity(intent);
                }
                @Override
                public void ediDefaultAddress(final int addressId) {
                    String str="";
                    if(addressId==0){
                        str="是否取消默认地址";
                    }else{
                        str="是否设置为默认地址";
                    }
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddressListActivity.this);
                    alert.setTitle(str)
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    addressListAdapter.setUserDefultAddressId(addressId);
                                }
                            })
                            .setNegativeButton("否",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    addressListAdapter.update(addressListAdapter.getDatas());
                                    dialog.dismiss();
                                }
                            });
                    alert.create().show();

                }
            });
        } else {
            addressListAdapter.update(user_addresses);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
        realm.close();
        if (mcompositeSubscription != null && !mcompositeSubscription.isUnsubscribed()) {
            mcompositeSubscription.unsubscribe();
        }
    }

    @OnClick(R.id.add_address_btn)
    public void onClick() {
        startActivity(new Intent(AddressListActivity.this,EdiAddressActivity.class));
    }
}
