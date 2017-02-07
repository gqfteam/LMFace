package com.lmface.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.util.Phone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;
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
                selectByUserName(_phone,_password2);
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

    private void selectByUserName(final String phone, final String password) {
        NetWork.getUserService().selectByUserName(phone)
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
                            register(phone,password);
                        }
                        Toast.makeText(RegistActivity.this, resultCode.getMsg(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void register(String phone, String password) {
        NetWork.getUserService().registeredUsers(phone,password)
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
                            RegistSuccessDialogFragment registSuccessDialogFragment = new RegistSuccessDialogFragment();
                            registSuccessDialogFragment.setDialogFragmentDismissLinsener(new DialogFragmentDismissLinsener() {
                                @Override
                                public void dialogDismiss() {
                                    startActivity(new Intent(RegistActivity.this,LoginActivity.class));
                                }
                            });
                            registSuccessDialogFragment.show(getFragmentManager(),"registSuccessDialogFragment");
                        }else {
                            Toast.makeText(RegistActivity.this, resultCode.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
        mUnbinder.unbind();
    }
}
