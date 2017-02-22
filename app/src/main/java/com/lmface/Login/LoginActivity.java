package com.lmface.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.lmface.Main.MainActivity;
import com.lmface.R;
import com.lmface.application.LMFaceApplication;
import com.lmface.network.NetWork;
import com.lmface.pojo.user_msg;
import com.lmface.util.SettingsUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/1/5.
 */

public class LoginActivity extends AppCompatActivity {


    CompositeSubscription compositeSubscription;
    String name;
    String password;
    @BindView(R.id.login_name_et)
    EditText loginNameEt;
    @BindView(R.id.login_password_et)
    EditText loginPasswordEt;
    @BindView(R.id.login_rememberPassword)
    CheckBox loginRememberPassword;
    @BindView(R.id.login_auto_login)
    CheckBox loginAutoLogin;
    @BindView(R.id.login_bt)
    Button loginBt;
    @BindView(R.id.home_regist_btn)
    TextView homeRegistBtn;
    @BindView(R.id.login_form)
    ScrollView loginForm;
    @BindView(R.id.login_progress)
    ProgressBar loginProgress;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStatusBar(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginBt.setEnabled(false);
        //如果activity集合size不为0则遍历退出activity
        if (((LMFaceApplication) getApplication()).getListSize() != 0) {
            ((LMFaceApplication) getApplication()).exit();
        }
        compositeSubscription = new CompositeSubscription();
        realm = Realm.getDefaultInstance();
        initLoginSetting();
        initLogin();
        initLoginBt();


    }

    /**
     * 登录设置
     */
    private void initLoginSetting() {
        loginAutoLogin.setChecked(SettingsUtils.isAutoLogin(getApplicationContext()));
        loginRememberPassword.setChecked(SettingsUtils.isRememberPassword(getApplicationContext()));
        RxCompoundButton.checkedChanges(loginRememberPassword)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        SettingsUtils.setPrefRememberPassword(getApplicationContext(), aBoolean);
                        if (aBoolean == false) {
                            loginAutoLogin.setChecked(false);
                            SettingsUtils.setPrefAutoLogin(getApplicationContext(), aBoolean);
                        }
                    }
                });

        RxCompoundButton.checkedChanges(loginAutoLogin)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        SettingsUtils.setPrefAutoLogin(getApplicationContext(), aBoolean);
                        if (aBoolean == true) {
                            SettingsUtils.setPrefRememberPassword(getApplicationContext(), aBoolean);
                            loginRememberPassword.setChecked(true);
                        }
                    }
                });


                if (SettingsUtils.isRememberPassword(getApplicationContext())) {
                    user_msg userInfo = realm.where(user_msg.class).findFirst();
                    name = userInfo.getUserName();
                    LMFaceApplication.username = name;
                    password = userInfo.getUserPassword();
                    loginNameEt.setText(name);
                    loginPasswordEt.setText(password);
                    loginBt.setEnabled(true);
                    if (SettingsUtils.isAutoLogin(getApplicationContext())) {

                        doLogin();
                    }
                }

    }


    public void hxLogin(){
       EMClient.getInstance().login(name,ui_msg.getHxpassword(),new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                Message msg=new Message();
                msg.what=3;
                mHandler.sendMessage(msg);

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Message msg=new Message();
                msg.what=2;
                mHandler.sendMessage(msg);
            }
        });
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             if(msg.what==2){
                Log.d("gqf", "登录聊天服务器失败！");
                Toast.makeText(getApplicationContext(),"登录失败hx",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==3){
                //环信登录成功后
                user_msg user = realm.where(user_msg.class).findFirst();
                if (user != null) {
                    if (user.getUserId() != ui_msg.getUserId()) {

                    } else {
                        //如果是子自动登录，保存账户密码
                        name = user.getUserName();
                        password = user.getUserPassword();
                    }
                    //删除本地之前保存的用户信息
                    realm.beginTransaction();
                    user.deleteFromRealm();
                    realm.commitTransaction();
                }
                LMFaceApplication.username = name;
                realm.beginTransaction();
                 ui_msg.setUserPassword(password);
                 ui_msg.setUserName(name);
                Log.i("gqf", "userInfo" + ui_msg.toString());
                realm.copyToRealmOrUpdate(ui_msg);
                realm.commitTransaction();
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }
        }
    };

    /**
     * 对输入框是否为null进行控制
     */
    private void initLogin() {

        Observable<CharSequence> usernameOs = RxTextView.textChanges(loginNameEt).skip(1);
        final Observable<CharSequence> passwordOs = RxTextView.textChanges(loginPasswordEt).skip(1);

        Subscription etSc = Observable.combineLatest(usernameOs, passwordOs, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                boolean usernameBl = !TextUtils.isEmpty(charSequence);
                boolean passwordBl = !TextUtils.isEmpty(charSequence2);
                if (!usernameBl) {
                    loginNameEt.setError("请输入用户名");
                } else {
                    loginNameEt.setError(null);
                }

                if (!passwordBl)
                    loginPasswordEt.setError("请输入密码");
                else
                    loginPasswordEt.setError(null);

                return usernameBl && passwordBl;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

                name = loginNameEt.getText().toString();
                password = loginPasswordEt.getText().toString();
                loginBt.setEnabled(aBoolean);
            }
        });

        compositeSubscription.add(etSc);
    }

    private void deletUser() {
        realm.beginTransaction();
        user_msg userInfo = realm.where(user_msg.class).findFirst();
        if (userInfo != null) {
            userInfo.deleteFromRealm();
        }
        realm.commitTransaction();
        SettingsUtils.setPrefAutoLogin(getApplicationContext(), false);
        SettingsUtils.setPrefRememberPassword(getApplicationContext(), false);
    }


    private void initLoginBt() {

        Subscription mloginBt = RxView.clicks(loginBt).throttleFirst(400, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {
                        doLogin();
                    }
                });
        compositeSubscription.add(mloginBt);
    }

    user_msg ui_msg;
    private void doLogin() {
        Log.i("gqf", name + password);
        Subscription logSc = NetWork.getUserService().loginUsers(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<user_msg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                        Toast.makeText(LoginActivity.this, "登录失败，服务器响应失败", Toast.LENGTH_SHORT).show();
                        deletUser();
                    }

                    @Override
                    public void onNext(user_msg userInfo) {
                        if (userInfo.getUserId() == 0) {
                            Toast.makeText(LoginActivity.this, "登录失败,用户名密码错误", Toast.LENGTH_SHORT).show();
                            deletUser();
                        } else {
                            ui_msg=userInfo;
                           hxLogin();
                        }
                    }
                });

        compositeSubscription.add(logSc);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }

    @OnClick(R.id.home_regist_btn)
    public void onClick() {
        startActivity(new Intent(LoginActivity.this,RegistActivity.class));
    }
}
