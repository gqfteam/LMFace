package com.lmface.signin.myInitiateSign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lmface.R;
import com.lmface.pojo.UserDailySignMsg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 * 我发起的日常签到详细信息页
 */

public class DaliySignEndMsgActivity extends AppCompatActivity {

    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.daily_sign_end_msg_toolbar)
    Toolbar dailySignEndMsgToolbar;
    @BindView(R.id.daily_sign_end_msg_list)
    RecyclerView dailySignEndMsgList;

    List<UserDailySignMsg> userDailySignMsgs;

    public void setToolbar(String statu) {

        dailySignEndMsgToolbar.setTitle(statu);
        setSupportActionBar(dailySignEndMsgToolbar);
        dailySignEndMsgToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        dailySignEndMsgToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sign_end_msg);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        String title=getIntent().getStringExtra("courseName");
        setToolbar(title);



    }

    //查询接收到这个签到事件下所有用户的签到情况
    public void initData(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
    }
}
