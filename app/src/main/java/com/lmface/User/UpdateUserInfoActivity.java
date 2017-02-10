package com.lmface.User;

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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.address_model;
import com.lmface.pojo.collegeinfo;
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
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/8.
 */

public class UpdateUserInfoActivity extends AppCompatActivity {


    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.update_user_info_toolbar)
    Toolbar updateUserInfoToolbar;
    @BindView(R.id.select_update_user_info_provinces)
    AutoCompleteTextView selectUpdateUserInfoProvinces;
    @BindView(R.id.select_update_user_info_city)
    AutoCompleteTextView selectUpdateUserInfoCity;
    @BindView(R.id.select_update_user_info_college)
    AutoCompleteTextView selectUpdateUserInfoCollege;
    @BindView(R.id.update_user_info_nickname)
    EditText updateUserInfoNickname;
    @BindView(R.id.update_user_info_man)
    RadioButton updateUserInfoMan;
    @BindView(R.id.update_user_info_woman)
    RadioButton updateUserInfoWoman;
    @BindView(R.id.update_user_info_phone)
    EditText updateUserInfoPhone;
    @BindView(R.id.update_user_info_txt)
    EditText updateUserInfoTxt;
    @BindView(R.id.update_user_info_btn)
    Button updateUserInfoBtn;

    int sexNum=1;
    String provinces;
    String city;
    int collegeId=0;
    List<collegeinfo> collegeInfos;
    public void setToolbar(String str) {
        updateUserInfoToolbar.setTitle(str);
        setSupportActionBar(updateUserInfoToolbar);
        updateUserInfoToolbar.setBackgroundResource(R.color.colorPrimary);
        updateUserInfoToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        updateUserInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("用户信息更新");
        initaddressModels();
        initView();
    }

    public void initView(){
        initButton();
        user_msg umsg=realm.where(user_msg.class).findFirst();
        updateUserInfoTxt.setText(umsg.getPersonalnote());
        updateUserInfoNickname.setText(umsg.getNickname());
        updateUserInfoPhone.setText(umsg.getPhone());
        if(umsg.getSex()!=null){
            if(!umsg.getSex().equals("")){
                sexNum=Integer.parseInt(umsg.getSex());
            }
        }
        if(sexNum==1){
            //男
            updateUserInfoMan.setChecked(true);
        }else{
            updateUserInfoWoman.setChecked(true);
        }
        if(umsg.getCollegeid()!=null) {
            if(!umsg.getCollegeid().equals("")) {
                collegeId = Integer.parseInt(umsg.getCollegeid());
            }
        }

        initPEdi();
    }


    ReadJson rj;

    List<address_model> addressModels;
    public void initaddressModels() {
        addressModels = new ArrayList<>();
        String json = null;
        json = getString(R.string.p_c_c_json);
        rj = ReadJson.getInstance();

        addressModels = rj.getAddressModel(json);

    }
    public void initPEdi() {
        String[] user_num;
        Log.i("gqf", "addressModels" + addressModels.size());
        user_num = new String[addressModels.size()];
        for (int i = 0; i < addressModels.size(); i++) {
            user_num[i] = addressModels.get(i).getP();
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, user_num);
        selectUpdateUserInfoProvinces.setAdapter(autoadapter);
        selectUpdateUserInfoProvinces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectUpdateUserInfoProvinces.showDropDown();
            }
        });
        selectUpdateUserInfoProvinces.setInputType(InputType.TYPE_NULL);
        selectUpdateUserInfoProvinces.setKeyListener(null);
        selectUpdateUserInfoProvinces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provinces = addressModels.get(position).getP();
                initCEdi(addressModels.get(position));
                selectUpdateUserInfoCollege.setAdapter(null);
                selectUpdateUserInfoCity.setText("");
                collegeId=0;
                selectUpdateUserInfoCollege.setText("");
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
        selectUpdateUserInfoCity.setAdapter(autoadapter);
        selectUpdateUserInfoCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectUpdateUserInfoCity.showDropDown();
            }
        });
        selectUpdateUserInfoCity.setInputType(InputType.TYPE_NULL);
        selectUpdateUserInfoCity.setKeyListener(null);
        selectUpdateUserInfoCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = am.getCity().get(position).getName();
                collegeId=0;
                selectUpdateUserInfoCollege.setText("");
                initCollege(am.getCity().get(position).getName());
            }
        });

    }

    public void initCollege(String city){
        Subscription subscription = NetWork.getGoodsService().getcollegeMsg(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<collegeinfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<collegeinfo> collegeinfos) {
                        collegeInfos=collegeinfos;
                        String[] user_num;
                        user_num = new String[collegeinfos.size()];
                        for (int i = 0; i < collegeinfos.size(); i++) {
                            user_num[i] = collegeinfos.get(i).getCollegename();

                        }
                        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, user_num);
                        selectUpdateUserInfoCollege.setAdapter(autoadapter);
                        selectUpdateUserInfoCollege.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击显示
                                selectUpdateUserInfoCollege.showDropDown();
                            }
                        });
                        selectUpdateUserInfoCollege.setInputType(InputType.TYPE_NULL);
                        selectUpdateUserInfoCollege.setKeyListener(null);
                        selectUpdateUserInfoCollege.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                collegeId=collegeInfos.get(position).getCollegeid();
                            }
                        });
                    }
                });
        mcompositeSubscription.add(subscription);

    }

    @OnClick({R.id.select_update_user_info_provinces, R.id.select_update_user_info_city, R.id.select_update_user_info_college, R.id.update_user_info_man, R.id.update_user_info_woman, R.id.update_user_info_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_user_info_man:
                sexNum=1;
                break;
            case R.id.update_user_info_woman:
                sexNum=0;
                break;
            case R.id.update_user_info_btn:
                if(collegeId>0) {
                    updateUserInfoBtn.setEnabled(false);
                    updateUserInfo();
                }else{
                    Toast.makeText(getApplicationContext(),"请选择大学",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void initButton(){
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(updateUserInfoNickname).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(updateUserInfoPhone).skip(1);
        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence2, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                return Bl && B2;
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
                updateUserInfoBtn.setEnabled(aBoolean);
            }
        });
        mcompositeSubscription.add(etSc);
    }

    String phone;
    String nickName;
    String personalnote;
    public void updateUserInfo(){
        phone=updateUserInfoPhone.getText().toString();
        nickName=updateUserInfoNickname.getText().toString();
        personalnote=updateUserInfoTxt.getText().toString();
        user_msg user_msg=new user_msg();
        user_msg.setUserId(realm.where(user_msg.class).findFirst().getUserId());
        user_msg.setPhone(phone);
        user_msg.setNickname(nickName);
        user_msg.setPersonalnote(personalnote);
        user_msg.setSex(sexNum+"");
        user_msg.setCollegeid(collegeId+"");
        Gson g=new Gson();
        Subscription subscription = NetWork.getUserService().updateUserInfoByUserId(g.toJson(user_msg))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        if(resultCode.getCode()==10000){
                            Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                            user_msg user_msg=realm.where(com.lmface.pojo.user_msg.class).findFirst();
                            realm.beginTransaction();
                            user_msg.setPhone(phone);
                            user_msg.setNickname(nickName);
                            user_msg.setPersonalnote(personalnote);
                            user_msg.setSex(sexNum+"");
                            user_msg.setCollegeid(collegeId+"");
                            realm.copyToRealmOrUpdate(user_msg);
                            realm.commitTransaction();
                            UpdateUserInfoActivity.this.finish();
                        }else{
                            Toast.makeText(getApplicationContext(),resultCode.getMsg(),Toast.LENGTH_SHORT).show();
                            updateUserInfoBtn.setEnabled(true);
                        }

                    }
                });
        mcompositeSubscription.add(subscription);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }
}
