package com.lmface.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lmface.Login.LoginActivity;
import com.lmface.R;
import com.lmface.dialog.BaseDialog;
import com.lmface.dialog.SweetAlertDialog;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.user_msg;
import com.lmface.util.DialogUtils;
import com.lmface.util.PhotoGet;
import com.lmface.util.SettingsUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.userInfo_contain)
    LinearLayout userInfoContain;
    @BindView(R.id.user_head_img)
    CircleImageView userHeadImg;
    @BindView(R.id.user_money_txt)
    TextView userMoneyTxt;
    private Realm realm;

    CompositeSubscription compositeSubscription;
    private static final int TAKEPHOTO = 1; // 拍照
    private static final int GALLERY = 2; // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3; // 结果
    private PhotoGet photoGet;
    BaseDialog baseDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setToolBar();
        baseDialog=new BaseDialog(this);
        realm = Realm.getDefaultInstance();
        compositeSubscription=new CompositeSubscription();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user_msg userInfo = realm.where(user_msg.class).findFirst();
        userName.setText(userInfo.getNickname());
        userMoneyTxt.setText("¥"+userInfo.getUsermoney());
        if (userInfo.getHeadimg() != null) {
            if (!userInfo.getHeadimg().equals("")) {
                Picasso.with(this).load(userInfo.getHeadimg())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(userHeadImg);
            }
        }
    }

    private void setToolBar() {
        toolbar.setTitle(getResources().getString(R.string.my));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKEPHOTO:
                String headIconPath=photoGet.getHeadIconPath();
                if (headIconPath!=null)
                    photoGet.startPhotoZoom(Uri.fromFile(new File(headIconPath)), 150);
                break;
            case GALLERY:
                if (data != null) {
                    photoGet.startPhotoZoom(data.getData(), 150);
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    photoGet.saveImage(data,userHeadImg);
                    changeUserHeadImg(photoGet.getHeadFile());
                    this.sweetAlertDialog=DialogUtils.getInstance().DialogLoading(this);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.setTitleText("Loading...")
                            .showCancelButton(false)
                            .show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void changeUserHeadImg(File mPhotoFile ){
        Log.i("gqf","changeUserHeadImg"+mPhotoFile.getName()+mPhotoFile.getName());

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), mPhotoFile);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("zichifile", mPhotoFile.getName(), photoRequestBody);
        builder.setType(MultipartBody.FORM);
        MultipartBody mb=builder.build();

        int userId=realm.where(user_msg.class).findFirst().getUiId();


        Subscription subscription = NetWork.getUserService().changeUserHeadImg(mb.part(0),userId)
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
                    public void onNext(ResultCode s) {
                        sweetAlertDialog.dismissWithAnimation();

                        if(s.getCode()==10000){
                            user_msg um=realm.where(user_msg.class).findFirst();
                            realm.beginTransaction();
                            um.setHeadimg(s.getMsg());
                            realm.copyToRealmOrUpdate(um);
                            realm.commitTransaction();
                            Picasso.with(getApplicationContext()).load(s.getMsg())
                                    .placeholder(R.drawable.ic_launcher)
                                    .error(R.drawable.ic_launcher)
                                    .into(userHeadImg);
                        }else{
                            Toast.makeText(getApplicationContext(),s.getMsg(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        compositeSubscription.add(subscription);


    }
    @OnClick({R.id.update_user_password_lin,R.id.user_name_lin, R.id.user_img_lin, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_img_lin:
                //跳转修改用户头像
                //startActivity(new Intent(UserInfoActivity.this, ChangeUserHeadImgActivity.class));
                photoGet=PhotoGet.getInstance();
                photoGet.showAvatarDialog(UserInfoActivity.this, baseDialog);

                break;
            case R.id.user_name_lin:
                //跳转修改用户信息页面
                startActivity(new Intent(UserInfoActivity.this, UpdateUserInfoActivity.class));
                break;
            case R.id.logout:
                DialogUtils.getInstance().ConfirmAndCancel(this, "确定退出账户？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        logout(sweetAlertDialog);
                    }
                });
                break;
            case R.id.update_user_password_lin:
                //跳转修改用户密码页

                startActivity(new Intent(UserInfoActivity.this, UpdateUserPassWordActivity.class));

                break;
        }
    }
    SweetAlertDialog sweetAlertDialog;
    public void logout(SweetAlertDialog sweetAlertDilog){
        this.sweetAlertDialog=sweetAlertDilog;
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Loading...")
                .showCancelButton(false)
                .show();

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Message message=new Message();
                message.what=1;
                mHandler.sendMessage(message);
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                Message message2=new Message();
                message2.what=2;
                mHandler.sendMessage(message2);
            }
        });


    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                //退出账户是删除此用户
                realm.beginTransaction();
                user_msg userInfo = realm.where(user_msg.class).findFirst();
                if (userInfo != null) {
                    userInfo.deleteFromRealm();
                }
                realm.commitTransaction();
                //取消“记住密码，自动登录”
                SettingsUtils.setPrefAutoLogin(getApplicationContext(), false);
                SettingsUtils.setPrefRememberPassword(getApplicationContext(), false);
                sweetAlertDialog.dismissWithAnimation();
                startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));//跳转到登录页面
            }else  if(msg.what==2){
                sweetAlertDialog.dismissWithAnimation();
                Toast.makeText(getApplicationContext(), "退出登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }
}
