package com.lmface.huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lmface.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by johe on 2016/9/21.
 * gqf
 * 通讯录界面
 */
public class ContactActivity extends FragmentActivity {


    @BindView(R.id.contact_back_txt)
    TextView contactBackTxt;
    @BindView(R.id.contact_top_msg_rad)
    RadioButton contactTopMsgRad;
    @BindView(R.id.contact_top_friend_rad)
    RadioButton contactTopFriendRad;
    @BindView(R.id.contact_fragment)
    FrameLayout contactFragment;
    @BindView(R.id.contact_top_add_friend)
    ImageView contactTopAddFriend;
    private FragmentTransaction ft;
    private MsgListFragment msgListFragment;
    private FriendListFragment friendListFragment;


    private boolean isContactRad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        initTopRadAndFragment();

    }

    private void initTopRadAndFragment() {
        if (isContactRad) {
            //消息
            contactTopMsgRad.setChecked(true);
            contactTopFriendRad.setChecked(false);

            msgListFragment = new MsgListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.contact_fragment, msgListFragment).commit();

        } else {
            //联系人
            contactTopMsgRad.setChecked(false);
            contactTopFriendRad.setChecked(true);
            friendListFragment = new FriendListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.contact_fragment, friendListFragment).commit();
        }
    }



    private void showFragment(Fragment index) {
        ft = getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            Fragment f = getSupportFragmentManager().getFragments().get(i);
            if (f == index) {
                ft.show(f);
            } else {
                ft.hide(f);
            }

        }
        ft.commit();
    }

    @OnClick({R.id.contact_top_msg_rad, R.id.contact_top_friend_rad, R.id.contact_back_txt,R.id.contact_top_add_friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contact_top_msg_rad:
                isContactRad = true;
                if (msgListFragment != null) {
                    showFragment(msgListFragment);
                } else {
                    msgListFragment = new MsgListFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.contact_fragment, msgListFragment).commit();
                }
                break;
            case R.id.contact_top_friend_rad:
                isContactRad = false;
                if (friendListFragment != null) {
                    showFragment(friendListFragment);
                } else {
                    friendListFragment = new FriendListFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.contact_fragment, friendListFragment).commit();
                }
                break;
            case R.id.contact_back_txt:
                //Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.contact_top_add_friend:
                //Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ContactActivity.this,AddFriendActivity.class));
                break;
        }
    }
}
