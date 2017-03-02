package com.lmface.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.lmface.R;
import com.lmface.huanxin.DemoHelper;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.UserFriend;
import com.lmface.pojo.initialsignin_info;
import com.lmface.pojo.user_msg;
import com.lmface.util.DateUtil;
import com.lmface.util.TimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func7;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Daniel on 2017/2/27.
 */

public class SponporSignInActivity extends AppCompatActivity {
    @BindView(R.id.edi_address_toolbar)
    Toolbar ediAddressToolbar;
    @BindView(R.id.select_courseinfo)
    AutoCompleteTextView selectCourseinfo;
    @BindView(R.id.select_scope)
    AutoCompleteTextView selectScope;
    @BindView(R.id.select_interval_time)
    AutoCompleteTextView selectIntervalTime;
    @BindView(R.id.select_time)
    AutoCompleteTextView selectTime;
    @BindView(R.id.edi_goal)
    EditText ediGoal;
    @BindView(R.id.longitude_txt)
    TextView longitudeTxt;
    @BindView(R.id.latitude_txt)
    TextView latitudeTxt;
    @BindView(R.id.sign_commit_btn)
    Button signCommitBtn;
    @BindView(R.id.edi_address)
    EditText ediAddress;
    @BindView(R.id.signInPerson_txt)
    TextView signInPersonTxt;
    @BindView(R.id.signInPerson_btn)
    Button signInPersonBtn;
    @BindView(R.id.signInPersonFlag_txt)
    TextView signInPersonFlagTxt;

    private String[] mScope;
    private String[] mIntervalTime;
    private String[] mCourseinfo;
    private ArrayAdapter<String> mScope_arrayAdapter;
    private ArrayAdapter<String> mIntervalTime_arrayAdapter;
    private ArrayAdapter<String> mselectCourseinfo_arrayAdapter;
    private CompositeSubscription mCompositeSubscription;
    private String strMs;
    public static List<String> list_userName = new ArrayList<>();
    private List<Integer> listId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_sign);
        ButterKnife.bind(this);
        mCompositeSubscription = new CompositeSubscription();
        setAutoCompleteTextView();
        initSubmit();
        setToolbar("发起签到");

        //kkkkk


    }

    private void setToolbar(String toolstr) {
        ediAddressToolbar.setTitle(toolstr);
        ediAddressToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ediAddressToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        setSupportActionBar(ediAddressToolbar);
        ediAddressToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /*提交按钮控制*/
    private void initSubmit() {
        Observable<CharSequence> Courseinfo = RxTextView.textChanges(selectCourseinfo).skip(1);
        Observable<CharSequence> Scope = RxTextView.textChanges(selectScope).skip(1);
        Observable<CharSequence> IntervalTime = RxTextView.textChanges(selectIntervalTime).skip(1);
        Observable<CharSequence> Time = RxTextView.textChanges(selectTime).skip(1);
        Observable<CharSequence> Address = RxTextView.textChanges(ediAddress).skip(1);
        Observable<CharSequence> Goal = RxTextView.textChanges(ediGoal).skip(1);
        Observable<CharSequence> InPersonFlagTxt = RxTextView.textChanges(signInPersonFlagTxt).skip(1);
        Observable.combineLatest(Courseinfo, Scope, IntervalTime, Time, Address, Goal,InPersonFlagTxt, new Func7<CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence,CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, CharSequence charSequence5, CharSequence charSequence6,CharSequence charSequence7) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                boolean B3 = !TextUtils.isEmpty(charSequence3);
                boolean B4 = !TextUtils.isEmpty(charSequence4);
                boolean B5 = !TextUtils.isEmpty(charSequence5);
                boolean B6 = !TextUtils.isEmpty(charSequence6);
                Log.e("Daniel","---charSequence7---"+charSequence7.toString());
                boolean B7 = charSequence7.toString().equals("已选择");
                Log.e("Daniel","---B7---"+B7);
                return Bl && B2 && B3 && B4 && B5 && B6 && B7;
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
                Log.e("Daniel","---aBoolean---"+aBoolean);
                signCommitBtn.setEnabled(aBoolean);
            }
        });
    }

    private void setAutoCompleteTextView() {
        mScope = getResources().getStringArray(R.array.scope);
        mIntervalTime = getResources().getStringArray(R.array.interval_time);
        mCourseinfo = getResources().getStringArray(R.array.Courseinfo);

        mScope_arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mScope);
        mIntervalTime_arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mIntervalTime);
        mselectCourseinfo_arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCourseinfo);

        selectScope.setInputType(InputType.TYPE_NULL);
        selectScope.setKeyListener(null);
        selectScope.setAdapter(mScope_arrayAdapter);

        selectIntervalTime.setInputType(InputType.TYPE_NULL);
        selectIntervalTime.setKeyListener(null);
        selectIntervalTime.setAdapter(mIntervalTime_arrayAdapter);

        selectCourseinfo.setInputType(InputType.TYPE_NULL);
        selectCourseinfo.setKeyListener(null);
        selectCourseinfo.setAdapter(mselectCourseinfo_arrayAdapter);
        selectTime.setKeyListener(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("gqf", "---list_userName.size()---" + list_userName.size());
        if (list_userName.size() != 0) {
            signInPersonFlagTxt.setText("已选择");
        } else {
            signInPersonFlagTxt.setText("未选择");
        }
    }

    /**
     * 年月日选择器
     */
    public void onYearMonthDayTimePicker() {
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_OF_DAY);
        picker.setRange(2000, 2030);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                //                showToast(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                selectTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                strMs = year + month + day + hour + minute + "00";

            }
        });
        picker.show();
    }

    ArrayList<UserFriend> users;
    private DemoHelper demoHelper;

    public void initList(final List<String> usernames) {
        for (int i = 0; i < usernames.size(); i++) {
            initUserMsgList(usernames.get(i));
        }

    }

    //    List<user_msg> user_msgs;

    public void initUserMsgList(String userName) {

        //多次查询后刷新list
        Subscription subscription = NetWork.getUserService().selectUserByName(userName)
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
                        Log.i("gqf", "user_msg" + user_msg.toString());
                        listId.add(user_msg.getUserId());

                        initiateSign(mGson, listId);

                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @OnClick({R.id.select_courseinfo,R.id.select_scope, R.id.select_interval_time, R.id.select_time, R.id.signInPerson_btn, R.id.sign_commit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_courseinfo:
                selectCourseinfo.showDropDown();
                break;
            case R.id.select_scope:
                selectScope.showDropDown();
                break;
            case R.id.select_interval_time:
                selectIntervalTime.showDropDown();

                break;
            case R.id.select_time:
                onYearMonthDayTimePicker();

                break;
            case R.id.signInPerson_btn:
                startActivity(new Intent(SponporSignInActivity.this, SignInPersonActivity.class));
                break;

            case R.id.sign_commit_btn:
                setDate();
                break;
        }
    }
private String mGson;
    private void setDate() {
        initialsignin_info initialsigninInfo = new initialsignin_info();
        initialsigninInfo.setTemporaryordaily(2);

        initialsigninInfo.setSignscope(Integer.parseInt(selectScope.getText().toString().substring(0,selectScope.getText().toString().length()-1)));
        Integer intervaltime = Integer.parseInt(selectIntervalTime.getText().toString().substring(0,selectIntervalTime.getText().toString().length()-2));
        initialsigninInfo.setSignintervaltime(intervaltime);

        Log.e("Daniel", "----开始时间---" + DateUtil.toMsDate(strMs));
        try {
            Log.e("Daniel", "----开始时间---" + DateUtil.string2Time(selectTime.getText() + ":00"));
            initialsigninInfo.setSignstarttime(DateUtil.string2Time(selectTime.getText() + ":00"));
            initialsigninInfo.setSignendtime(DateUtil.string2Time(TimeUtils.getFormaDatass((DateUtil.toMsDate(strMs) + (intervaltime* 60000)))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initialsigninInfo.setSignaddress(ediAddress.getText() + "");
        initialsigninInfo.setSigngoal(ediGoal.getText() + "");
        initialsigninInfo.setAddresslatitude("34.3213231223");
        initialsigninInfo.setAddresslongitude("33.3131432");
        initialsigninInfo.setSigncourseid(2);
        Log.e("Daniel", "----开始时间---" + initialsigninInfo.getSignstarttime());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
         mGson = gson.toJson(initialsigninInfo);

        Log.e("Daniel", "----json---" + mGson);
        Log.e("Daniel", "----list_userName---" + list_userName.size());
        if (listId == null) {
            listId = new ArrayList<>();
        }
        initList(list_userName);

    }

    private void initiateSign(String json, List<Integer> list) {
        Subscription initiateSign = NetWork.getSignService().initiateSign(json, list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SponporSignInActivity.this, "发起签到失败！" , Toast.LENGTH_SHORT).show();
                    }

                    @DebugLog
                    @Override
                    public void onNext(ResultCode resultCode) {
                        Toast.makeText(SponporSignInActivity.this, "已成功发起签到！" , Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
        mCompositeSubscription.add(initiateSign);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}
