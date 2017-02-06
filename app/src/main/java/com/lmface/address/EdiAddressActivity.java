package com.lmface.address;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.address_model;
import com.lmface.pojo.user_address;
import com.lmface.pojo.user_msg;
import com.lmface.util.ReadJson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func6;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/3.
 */

public class EdiAddressActivity extends AppCompatActivity {


    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.edi_address_toolbar)
    Toolbar ediAddressToolbar;

    int addressID = 0;
    user_address userAddress;
    boolean isEdi = false;
    @BindView(R.id.edi_address_btn)
    Button ediAddressBtn;

    String[] streets = {"开元大道", "学府街", "古城路", "龙门大道"};

    List<address_model> addressModels;
    @BindView(R.id.select_address_provinces)
    AutoCompleteTextView selectAddressProvinces;
    @BindView(R.id.select_address_city)
    AutoCompleteTextView selectAddressCity;
    @BindView(R.id.select_address_county)
    AutoCompleteTextView selectAddressCounty;
    @BindView(R.id.select_address_street)
    AutoCompleteTextView selectAddressStreet;
    @BindView(R.id.edi_address_name)
    EditText ediAddressName;
    @BindView(R.id.edi_address_phone)
    EditText ediAddressPhone;
    @BindView(R.id.edi_address_txt)
    EditText ediAddressTxt;

    String provinces;
    String city;
    String county;
    String street;


    public void setToolbar(String str) {
        ediAddressToolbar.setTitle(getResources().getString(R.string.my_goods_list_title));
        setSupportActionBar(ediAddressToolbar);
        ediAddressToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        ediAddressToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edi_address);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("");
        initaddressModels();
        initBtn();
        addressID = getIntent().getIntExtra("addressId", 0);
        if (addressID == 0) {
            isEdi = false;
            ediAddressBtn.setText("添加");
        } else {
            isEdi = true;
            ediAddressBtn.setText("修改");
            initAddressData(addressID);
        }

        initPEdi();
    }

    ReadJson rj;

    public void initaddressModels() {
        addressModels = new ArrayList<>();
        String json = null;
        json = getString(R.string.p_c_c_json);
        rj = ReadJson.getInstance();

        addressModels = rj.getAddressModel(json);

    }

    public void initAddressData(int id){
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
                        userAddress=user_address;
                        initAddEdi(user_address);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public void initAddEdi(user_address user_address){
        ediAddressName.setText(user_address.getName());
        ediAddressPhone.setText(user_address.getPhone());
        ediAddressTxt.setText(user_address.getAddressTxt());

    }

    public void initPEdi() {
        String[] user_num;
        Log.i("gqf", "addressModels" + addressModels.size());
        user_num = new String[addressModels.size()];
        for (int i = 0; i < addressModels.size(); i++) {
            user_num[i] = addressModels.get(i).getP();
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, user_num);
        selectAddressProvinces.setAdapter(autoadapter);
        selectAddressProvinces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectAddressProvinces.showDropDown();
            }
        });
        selectAddressProvinces.setInputType(InputType.TYPE_NULL);
        selectAddressProvinces.setKeyListener(null);
        selectAddressProvinces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provinces = addressModels.get(position).getP();
                initCEdi(addressModels.get(position));
                selectAddressCounty.setAdapter(null);
                selectAddressStreet.setAdapter(null);
            }
        });
    }

    public void initCEdi(final address_model am) {
        String[] user_num;
        user_num = new String[am.getCity().size()];
        for (int i = 0; i < am.getCity().size(); i++) {
            user_num[i] = am.getCity().get(i).getName();
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, user_num);
        selectAddressCity.setAdapter(autoadapter);
        selectAddressCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectAddressCity.showDropDown();
            }
        });
        selectAddressCity.setInputType(InputType.TYPE_NULL);
        selectAddressCity.setKeyListener(null);
        selectAddressCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = am.getCity().get(position).getName();
                initCoEdi(am.getCity().get(position).getHave());
                selectAddressStreet.setAdapter(null);
            }
        });

    }

    String[] countys;

    public void initCoEdi(ArrayList<String> areas) {

        countys = new String[areas.size()];
        for (int i = 0; i < areas.size(); i++) {
            countys[i] = areas.get(i);
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, countys);
        selectAddressCounty.setAdapter(autoadapter);
        selectAddressCounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectAddressCounty.showDropDown();
            }
        });
        selectAddressCounty.setInputType(InputType.TYPE_NULL);
        selectAddressCounty.setKeyListener(null);
        selectAddressCounty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                county = countys[position];
                initSEdi();

            }
        });

    }

    public void initSEdi() {
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, streets);
        selectAddressStreet.setAdapter(autoadapter);
        selectAddressStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectAddressStreet.showDropDown();
            }
        });
        selectAddressStreet.setInputType(InputType.TYPE_NULL);
        selectAddressStreet.setKeyListener(null);
        selectAddressStreet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                street = streets[position];
            }
        });
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

    public void initBtn() {
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(selectAddressProvinces).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(selectAddressCity).skip(1);
        Observable<CharSequence> CharSequence3 = RxTextView.textChanges(selectAddressCounty).skip(1);
        Observable<CharSequence> CharSequence4 = RxTextView.textChanges(selectAddressStreet).skip(1);
        Observable<CharSequence> CharSequence5 = RxTextView.textChanges(ediAddressName).skip(1);
        Observable<CharSequence> CharSequence6 = RxTextView.textChanges(ediAddressPhone).skip(1);
        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence2, CharSequence3, CharSequence4, CharSequence5, CharSequence6, new Func6<CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, CharSequence charSequence5, CharSequence charSequence6) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                boolean B3 = !TextUtils.isEmpty(charSequence3);
                boolean B4 = !TextUtils.isEmpty(charSequence4);
                boolean B5 = !TextUtils.isEmpty(charSequence5);
                boolean B6 = !TextUtils.isEmpty(charSequence6);
                return Bl && B2 && B3 && B4 && B5 && B6;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @DebugLog
            @Override
            public void onNext(Boolean aBoolean) {
                Log.e("Daniel", "---aBoolean---" + aBoolean);
                ediAddressBtn.setEnabled(aBoolean);
            }
        });
        mcompositeSubscription.add(etSc);
    }

    public void addAddress() {
        Subscription subscription = NetWork.getUserAddressService().addUserAddress(realm.where(user_msg.class).findFirst().getUserId(),
                "中国", provinces, city, county, street, ediAddressTxt.getText().toString(), ediAddressName.getText().toString(), ediAddressPhone.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ediAddressBtn.setEnabled(true);
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        if (resultCode.getCode() == 10000) {
                            Toast.makeText(getApplicationContext(), "新增地址成功", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), resultCode.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        ediAddressBtn.setEnabled(true);
                    }
                });
        mcompositeSubscription.add(subscription);

    }
    public void updateAddress(){
        if(userAddress.getProvinces().equals(provinces)&&
                userAddress.getCity().equals(city)&&
                userAddress.getCounty().equals(county)&&
                userAddress.getStreet().equals(street)&&
                userAddress.getName().equals(ediAddressName.getText().toString())&&
                userAddress.getPhone().equals(ediAddressPhone.getText().toString())&&
                userAddress.getAddressTxt().equals(ediAddressTxt.getText().toString())){

            Toast.makeText(getApplicationContext(),"请修改信息后再更新",Toast.LENGTH_SHORT).show();
        }else {
            Subscription subscription = NetWork.getUserAddressService().updateUserAddress(addressID, realm.where(user_msg.class).findFirst().getUserId(),
                    "中国", provinces, city, county, street, ediAddressTxt.getText().toString(), ediAddressName.getText().toString(), ediAddressPhone.getText().toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ediAddressBtn.setEnabled(true);
                        }

                        @Override
                        public void onNext(ResultCode resultCode) {
                            if (resultCode.getCode() == 10000) {
                                Toast.makeText(getApplicationContext(), "修改地址成功", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getApplicationContext(), resultCode.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                            ediAddressBtn.setEnabled(true);
                        }
                    });
            mcompositeSubscription.add(subscription);
        }
    }



    @OnClick(R.id.edi_address_btn)
    public void onClick() {
        ediAddressBtn.setEnabled(false);
        if(isEdi==false){
            addAddress();
        }else{
            updateAddress();
        }

    }
}
