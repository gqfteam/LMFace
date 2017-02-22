package com.lmface.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.lmface.R;
import com.lmface.dialog.SweetAlertDialog;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.user_msg;
import com.lmface.util.DialogUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.lmface.R.id.rl_user;

/**
 * Created by johe on 2017/2/21.
 */

public class UpdateUserPassWordActivity extends AppCompatActivity {


    @BindView(R.id.login_picture)
    CircleImageView loginPicture;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.ll_user_info)
    LinearLayout llUserInfo;
    @BindView(R.id.commit)
    Button commit;
    @BindView(rl_user)
    RelativeLayout rlUser;

    Realm realm;
    CompositeSubscription compositeSubscription;

    user_msg user_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_password);
        ButterKnife.bind(this);
        realm=Realm.getDefaultInstance();
        compositeSubscription=new CompositeSubscription();
        initView();
    }
    public void initView(){
        user_msg=realm.where(user_msg.class).findFirst();
        Animation anim= AnimationUtils.loadAnimation(this, R.anim.login_anim);
        anim.setFillAfter(true);
        rlUser.startAnimation(anim);
        if(!user_msg.getHeadimg().equals("")) {
            Picasso.with(this).load(user_msg.getHeadimg())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(loginPicture);
        }
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(account).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(password).skip(1);
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
                commit.setEnabled(aBoolean);
            }
        });
        compositeSubscription.add(etSc);


    }
    private SweetAlertDialog sweetAlertDialog;

    public void changePassWord(SweetAlertDialog sweetAlertDilog){
        this.sweetAlertDialog=sweetAlertDilog;
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Loading...")
                .showCancelButton(false)
                .show();
        Subscription subscription = NetWork.getUserService().updateUserPassword(user_msg.getUserId(),password.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        commit.setEnabled(true);
                        sweetAlertDialog.dismissWithAnimation();
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {

                        sweetAlertDialog.dismissWithAnimation();
                        commit.setEnabled(true);
                        if(resultCode.getCode()==10000){

                            realm.beginTransaction();
                            user_msg.setUserPassword(password.getText().toString());
                            realm.copyToRealmOrUpdate(user_msg);
                            realm.commitTransaction();
                            Log.i("gqf","onNext"+user_msg.toString());
                            onBackPressed();
                        }else{
                            Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }



    @OnClick(R.id.commit)
    public void onClick() {

        if(!account.getText().toString().equals(user_msg.getUserPassword())){
            account.setError("旧密码错误");
        }else if(account.getText().toString().equals(password.getText().toString())){
            password.setError("新旧密码相同");
        }else{
            DialogUtils.getInstance().ConfirmAndCancel(this, "确定修改吗？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    changePassWord(sweetAlertDialog);
                }
            });

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }
}
