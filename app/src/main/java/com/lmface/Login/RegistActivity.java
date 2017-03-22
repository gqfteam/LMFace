package com.lmface.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.util.Phone;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RegistActivity extends AppCompatActivity {

    @BindView(R.id.regist_phone_et)
    EditText registPhoneEt;
    @BindView(R.id.regist_password_et)
    EditText registPasswordEt;
    @BindView(R.id.regist_password2_et)
    EditText registPassword2Et;
    @BindView(R.id.regist_bt)
    Button registBt;

    private CompositeSubscription compositeSubscription;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        compositeSubscription = new CompositeSubscription();
        mUnbinder=ButterKnife.bind(this);
    }

    @OnClick(R.id.regist_bt)
    public void onClick() {
        String _phone = registPhoneEt.getText().toString();
        String _password = registPasswordEt.getText().toString();
        String _password2 = registPassword2Et.getText().toString();
        if (Phone.IsMobileNO(_phone)){
            if (_password.equals(_password2)) {
                //开始注册
                registBt.setEnabled(false);
                register(_phone,_password);
            }else {
                Toast.makeText(this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                registPasswordEt.setText("");
                registPassword2Et.setText("");
            }
        }else {
            registPhoneEt.setText("");
            Toast.makeText(this, "手机格式不正确！", Toast.LENGTH_SHORT).show();
        }
    }


    public void registerHx(final String phone,final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("gqf","HyphenateException"+"registerHx1");
                    EMClient.getInstance().createAccount(phone, password);
                    Log.i("gqf","HyphenateException"+"registerHx2");
                    Message message = new Message();
                    message.what = 1;
                    Bundle b=new Bundle();
                    b.putString("phone",phone);
                    b.putString("password",password);
                    message.setData(b);
                    mLoghHndler.sendMessage(message);
                }catch (HyphenateException e){
                    Log.i("gqf","HyphenateException"+e.getMessage());
                    Message message = new Message();
                    message.what = 2;
                    mLoghHndler.sendMessage(message);
                }
            }
        }).start();

    }
    private Handler mLoghHndler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                //注册成功
                //去极光推送设置别名
                setAlias();
                registBt.setEnabled(true);
                registBt.post(new Runnable() {
                    @Override
                    public void run() {
                        RegistSuccessDialogFragment registSuccessDialogFragment = new RegistSuccessDialogFragment();
                        registSuccessDialogFragment.setDialogFragmentDismissLinsener(new DialogFragmentDismissLinsener() {
                            @Override
                            public void dialogDismiss() {
                                startActivity(new Intent(RegistActivity.this,LoginActivity.class));
                            }
                        });
                        registSuccessDialogFragment.show(getFragmentManager(),"registSuccessDialogFragment");
                    }
                });
            }else if(msg.what == 2){
                registBt.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT);
                    }
                });
                registBt.setEnabled(true);
            }

        }
    };
    private void register(final  String phone,final String password) {
        Subscription logSc = NetWork.getUserService().registeredUsers(phone,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.i("gqf","onError"+e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.i("gqf","onNext"+resultCode.toString());
                        if(resultCode.getCode()==10000){
                            registerHx(phone,password);
                        }else {
                            Toast.makeText(RegistActivity.this, resultCode.getMsg(), Toast.LENGTH_SHORT).show();
                            registBt.setEnabled(true);
                        }
                    }
                });

        compositeSubscription.add(logSc);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
        mUnbinder.unbind();
    }


    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    public  void setAlias() {
        EditText aliasEdit = registPhoneEt;
        String alias = aliasEdit.getText().toString().trim();
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(this,"别名为空", Toast.LENGTH_SHORT).show();
            return;
        }


        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("Jpush_TAG", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("Jpush_TAG", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("Jpush_TAG", logs);
            }
        //    ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("Jpush_TAG", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i("Jpush_TAG", "Unhandled msg - " + msg.what);
            }
        }
    };
}
