package com.lmface.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.Login.LoginActivity;
import com.lmface.R;
import com.lmface.pojo.user_msg;
import com.lmface.util.SettingsUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.userInfo_contain)
    LinearLayout userInfoContain;
    @BindView(R.id.user_head_img)
    CircleImageView userHeadImg;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setToolBar();

        realm = Realm.getDefaultInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        user_msg userInfo = realm.where(user_msg.class).findFirst();
        userName.setText(userInfo.getNickname());
        if(userInfo.getHeadimg()!=null){
            if(!userInfo.getHeadimg().equals("")){
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

    @OnClick({R.id.user_name_lin,R.id.user_img_lin, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_img_lin:
                //跳转修改用户头像
                startActivity(new Intent(UserInfoActivity.this, ChangeUserHeadImgActivity.class));

                break;
            case R.id.user_name_lin:
                //跳转修改用户信息页面
                startActivity(new Intent(UserInfoActivity.this, UpdateUserInfoActivity.class));

                break;
            case R.id.logout:
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

                startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));//跳转到登录页面
                break;
        }
    }
}
