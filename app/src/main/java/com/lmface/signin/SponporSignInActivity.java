package com.lmface.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
    private ArrayAdapter<String> mScope_arrayAdapter;
    private ArrayAdapter<String> mIntervalTime_arrayAdapter;
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


    }

    private void setAutoCompleteTextView() {
        mScope = getResources().getStringArray(R.array.scope);
        mIntervalTime = getResources().getStringArray(R.array.interval_time);
        mScope_arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mScope);
        mIntervalTime_arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mIntervalTime);
        selectScope.setInputType(InputType.TYPE_NULL);
        selectScope.setKeyListener(null);
        selectScope.setAdapter(mScope_arrayAdapter);
        selectIntervalTime.setInputType(InputType.TYPE_NULL);
        selectIntervalTime.setKeyListener(null);
        selectIntervalTime.setAdapter(mIntervalTime_arrayAdapter);
        selectTime.setKeyListener(null);
        selectCourseinfo.setKeyListener(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        //        users = new ArrayList<>();
        //        for (int i = 0; i < usernames.size(); i++) {
        //            Log.i("wjd", "friendName:" + usernames.get(i));
        //            UserFriend user = new UserFriend(usernames.get(i));
        //            user.setUserName(usernames.get(i));
        //            users.add(user);
        //        }
        //        demoHelper = DemoHelper.getInstance();
        //        users = demoHelper.filledData(users);
        //        listId = new ArrayList<>();
        //        //根据用户名查找用户信息
        //
        //        if (users.size() > 0) {
        //            initUserMsgList(users.get(0).getUserName());
        //        }
        //            initList();

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
                        //                        user_msgs.add(user_msg);
                        //                        users.get(user_msgs.size() - 1).setMsg(user_msg.getNickname(), user_msg.getHeadimg(), user_msg.getSex(), user_msg.getPhone());
                        //                        if (users.size() > listId.size()) {
                        //                            initUserMsgList(users.get(listId.size()).getUserName());
                        //                        } else {
                        //                            initList();
                        //                        }

                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @OnClick({R.id.select_scope, R.id.select_interval_time, R.id.select_time, R.id.signInPerson_btn, R.id.sign_commit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_scope:
                //                onOptionPicker(view,mScope);
                selectScope.showDropDown();
                break;
            case R.id.select_interval_time:
                //                onOptionPicker(view,mIntervalTime);
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

    private void setDate() {
        initialsignin_info initialsigninInfo = new initialsignin_info();
        initialsigninInfo.setTemporaryordaily(2);
        initialsigninInfo.setSignscope(Integer.parseInt(selectScope.getText() + ""));
        initialsigninInfo.setSignintervaltime(Integer.parseInt(selectIntervalTime.getText() + ""));

        Log.e("Daniel", "----开始时间---" + DateUtil.toMsDate(strMs));

        Log.e("Daniel", "----持续时间时间---" + Integer.parseInt(selectIntervalTime.getText() + "") * 60000);
        Log.e("Daniel", "----结束时间时间---" + (DateUtil.toMsDate(strMs) + (Integer.parseInt(selectIntervalTime.getText() + "") * 60000)));

        try {
            Log.e("Daniel", "----开始时间---" + DateUtil.string2Time(selectTime.getText() + ":00"));
            initialsigninInfo.setSignstarttime(DateUtil.string2Time(selectTime.getText() + ":00"));
            initialsigninInfo.setSignendtime(DateUtil.string2Time(TimeUtils.getFormaDatass((DateUtil.toMsDate(strMs) + (Integer.parseInt(selectIntervalTime.getText() + "") * 60000)))));
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
        String json = gson.toJson(initialsigninInfo);

        Log.e("Daniel", "----json---" + json);

        //                List<Integer> list = new ArrayList<>();
        //                list.add(34);
        //                list.add(35);
        //                list.add(36);
        Log.e("Daniel", "----list_userName---" + list_userName.size());
        if (listId == null) {
            listId = new ArrayList<>();
        }
        initList(list_userName);
        initiateSign(json, listId);
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

                    }

                    @DebugLog
                    @Override
                    public void onNext(ResultCode resultCode) {
                        Toast.makeText(SponporSignInActivity.this, "" + resultCode.getMsg(), Toast.LENGTH_SHORT).show();

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
