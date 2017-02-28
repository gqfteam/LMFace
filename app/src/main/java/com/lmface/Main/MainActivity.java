package com.lmface.Main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.lmface.R;
import com.lmface.User.EdiDialogFragment;
import com.lmface.application.LMFaceApplication;
import com.lmface.huanxin.AddFriendsListActivity;
import com.lmface.huanxin.ContactActivity;
import com.lmface.pojo.AddFriendMsg;
import com.lmface.pojo.ChangeActivity;
import com.lmface.pojo.ChatMsgEntity;
import com.lmface.pojo.UserFriend;
import com.lmface.util.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

import static com.lmface.R.id.toolbar;

public class MainActivity extends AppCompatActivity implements StoreFragment.mListener, UserFragment.mListener,SignFragment.mListener {

    private static final String HOME_TAG = "home_flag";
    private static final String STORE_TAG = "store_flag";
    private static final String MY_TAG = "my_flag";
    private static final String SIGN_TAG = "sign_flag";
    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(toolbar)
    Toolbar mToolbar;
    private static final int CONTENT_ORDERS = 2;
    private static final int CONTENT_MY = 3;
    private static final int CONTENT_HOME = 0;
    private static final int CONTENT_SIGN = 1;
    @BindView(R.id.bottomBar)
    BottomNavigationBar bottomBar;
    @BindView(R.id.toolbar_text)
    TextView toolbarText;
    EdiDialogFragment editNameDialog;
    private void setToolbar(String toolstr, int position) {


        switch (position) {
            case CONTENT_HOME:
                mToolbar.setTitle(toolstr);
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mToolbar.setTitleTextColor(Color.WHITE);
                toolbarText.setVisibility(View.GONE);
                this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
                setSupportActionBar(mToolbar);
                break;
            case CONTENT_ORDERS:
                mToolbar.setBackgroundColor(getResources().getColor(R.color.whitesmoke));
                toolbarText.setVisibility(View.VISIBLE);
                toolbarText.setText(toolstr);
                toolbarText.setTextColor(getResources().getColor(R.color.black));
                mToolbar.setTitle("");
                setSupportActionBar(mToolbar);
                toolbarText.setGravity(Gravity.CENTER_VERTICAL);
                mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.action_search) {
                            if (storeFragment != null) {
                                //弹出dialog输入模糊查询名称
                                editNameDialog = new EdiDialogFragment();
                                editNameDialog.setTitle("查询商品");
                                editNameDialog.setInputType( InputType.TYPE_CLASS_TEXT);
                                editNameDialog.show(getSupportFragmentManager(), "EditNameDialog");
                                editNameDialog.setDimssLinsener(new EdiDialogFragment.DimssLinsener() {
                                    @Override
                                    public void fragmentDimss() {
                                        editNameDialog=null;
                                    }

                                    @Override
                                    public void onOk(String ediTxt) {
                                        if(storeFragment!=null) {
                                            editNameDialog.setEnd(true);
                                            storeFragment.getByName(ediTxt);
                                        }
                                    }
                                });
                                //调用fragment的查询

                            }
                        } else {
                            //跳转到消息中心
                            startActivity(new Intent(MainActivity.this,ContactActivity.class));
                        }
                        return false;
                    }
                });
                break;
            case CONTENT_MY:
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbarText.setVisibility(View.VISIBLE);
                toolbarText.setText(toolstr);
                toolbarText.setTextColor(getResources().getColor(R.color.white));
                mToolbar.setTitle("");
                toolbarText.setGravity(Gravity.CENTER);
                setSupportActionBar(mToolbar);
                break;
            case CONTENT_SIGN:
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbarText.setVisibility(View.VISIBLE);
                toolbarText.setText(toolstr);
                toolbarText.setTextColor(getResources().getColor(R.color.white));
                mToolbar.setTitle("");
                toolbarText.setGravity(Gravity.CENTER);
                setSupportActionBar(mToolbar);
                break;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible((bottomBar.getCurrentSelectedPosition() == CONTENT_ORDERS));
        menu.findItem(R.id.action_notification).setVisible((bottomBar.getCurrentSelectedPosition() == CONTENT_ORDERS));
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //加入activity列表
        ((LMFaceApplication) getApplication()).addActivity(this);
        EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        setToolbar("校园首页", 0);
        initBotomBar();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        initAddFriend();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startActivity(ChangeActivity activity) {
        Intent intent = new Intent(MainActivity.this, activity.getActivity());
        if (activity.getGoodsId() != -1) {
            intent.putExtra("goodsId", activity.getGoodsId());
        }
        startActivity(intent);
    }

    public void initBotomBar() {
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar
                .setActiveColor(R.color.colorPrimaryDark)
                .setInActiveColor(R.color.frame_gary)
                .setBarBackgroundColor(R.color.whitesmoke);

        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_menu_deal_off, R.string.firstPage))
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_tool_off, R.string.sign))
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_poi_off, R.string.store))
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_user_off, R.string.my))
                .initialise();
        setContent(CONTENT_HOME);
        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        setContent(CONTENT_HOME);

                        break;
                    case 1:
                        setContent(CONTENT_SIGN);
                        break;
                    case 2:
                        setContent(CONTENT_ORDERS);
                        break;
                    case 3:
                        setContent(CONTENT_MY);
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            Log.i("gqf", "onMessageReceived");
            for (int i = 0; i < messages.size(); i++) {
                Log.i("gqf", "messages" + messages.get(i).toString());
                ChatMsgEntity entity = new ChatMsgEntity();
                entity.setDate(TimeUtils.getFormaDatass(messages.get(i).getMsgTime()));
                //				if (!realm.where(user_msg.class).findFirst().getUserName().equals(messages.get(i).getFrom())) {
                //					entity.setMsgType(true);
                //					entity.setName(messages.get(i).getFrom());
                //				} else {
                entity.setMsgType(true);

                entity.setName(messages.get(i).getFrom());
                entity.setFromName(messages.get(i).getTo());

                int startIndex = messages.get(i).getBody().toString().toString().lastIndexOf("txt:\"");
                String message_news = messages.get(i).getBody().toString().toString().substring(startIndex + 5,
                        messages.get(i).getBody().toString().toString().length() - 1);
                entity.setText(message_news);

                Log.i("gqf", "messages" + entity.toString());
                Message msg = new Message();
                msg.what = 4;
                msg.obj = entity;
                mHandler.sendMessage(msg);

                //发送广播给msglist页面
                UserFriend userFriend=new UserFriend(messages.get(i).getFrom());
                List<EMMessage> myEmMessages=new ArrayList<>();
                myEmMessages.add(messages.get(i));
                userFriend.setMessages(myEmMessages);
                EventBus.getDefault().post(userFriend);

            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };
    /**
     * 页面切换
     *
     * @param contentHome
     */
    HomeFragment homeFragment;
    StoreFragment storeFragment;
    UserFragment userFragment;

    SignFragment signFragment;
    private void setContent(int contentHome) {
        switch (contentHome) {
            case CONTENT_HOME:
                String home_str = getResources().getString(R.string.firstPage);
                setToolbar("校园首页", contentHome);
                Log.i("gqf","校园首页");
                homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance(home_str);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, homeFragment,HOME_TAG).commit();
                }else {
                    setFragment(homeFragment, HOME_TAG);
                }
                break;
            case CONTENT_ORDERS:
                String orders_str = getResources().getString(R.string.store);
                setToolbar("淘个不停", contentHome);
                storeFragment = (StoreFragment) getSupportFragmentManager().findFragmentByTag(STORE_TAG);
                if (storeFragment == null) {
                    storeFragment = StoreFragment.newInstance(orders_str);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, storeFragment,STORE_TAG).commit();
                }else {
                    setFragment(storeFragment, STORE_TAG);
                }
                break;
            case CONTENT_MY:
                String my_str = getResources().getString(R.string.my);
                setToolbar("个人中心", contentHome);
                userFragment = (UserFragment) getSupportFragmentManager().findFragmentByTag(MY_TAG);
                if (userFragment == null) {
                    userFragment = UserFragment.newInstance(my_str);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, userFragment,MY_TAG).commit();
                }else {
                    setFragment(userFragment, MY_TAG);
                }
                break;
            case CONTENT_SIGN:
                String tool_str = getResources().getString(R.string.sign);
                setToolbar("校园工具", contentHome);
                signFragment = (SignFragment) getSupportFragmentManager().findFragmentByTag(SIGN_TAG);
                if (signFragment == null) {
                    signFragment = SignFragment.newInstance(tool_str);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, signFragment,SIGN_TAG).commit();
                }else {
                    setFragment(signFragment, SIGN_TAG);
                }
                break;
        }

    }

    /**
     * 设置fragment
     *
     * @param fragment
     */
    @DebugLog
    private void setFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.container, fragment, tag);

                for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {

                    Fragment f = getSupportFragmentManager().getFragments().get(i);

                    if (f == fragment) {
                        fragmentTransaction.show(f);

                    } else {
                        if(!f.getTag().contains(tag)){
                            fragmentTransaction.hide(f);

                        }

                    }

                }
        fragmentTransaction.commit();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        realm.close();
        KeyboardUtils.hideSoftInput(this);
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        if (mcompositeSubscription != null && !mcompositeSubscription.isUnsubscribed()) {
            mcompositeSubscription.unsubscribe();
        }
    }

    public void changeActivity(
            Class activityClass) {
        startActivity(new Intent(MainActivity.this, activityClass));
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                String username=((AddFriendMsg)msg.obj).getName();
                String reason=((AddFriendMsg)msg.obj).getMsg();

                if (realm.where(AddFriendMsg.class).equalTo("name", username).findFirst() == null) {
                    Log.i("gqf", "onContactInvited        realm");
                    AddFriendMsg userFriend = new AddFriendMsg();
                    userFriend.setName(username);
                    userFriend.setMsg(reason);
                    if (realm.where(AddFriendMsg.class).findFirst() == null) {
                        userFriend.setMsgId(1);
                    } else {
                        int id = 0;
                        for (AddFriendMsg addFriendMsg : realm.where(AddFriendMsg.class).findAll()) {
                            if (addFriendMsg.getMsgId() > id) {
                                id = addFriendMsg.getMsgId();
                            }
                        }
                        userFriend.setMsgId(id+1);
                    }
                    realm.beginTransaction();
                    realm.insertOrUpdate(userFriend);
                    realm.commitTransaction();
                }else{
                    realm.beginTransaction();
                    AddFriendMsg addFriendMsg= realm.where(AddFriendMsg.class).equalTo("name", username).findFirst();
                    addFriendMsg.setMsg(reason);
                    realm.insertOrUpdate(addFriendMsg);
                    realm.commitTransaction();
                }
                SendNotification(username + "请求加你为好友", "", AddFriendsListActivity.class, false);
                EventBus.getDefault().post(realm.where(AddFriendMsg.class).equalTo("name", username).findFirst());
                Log.i("gqf", "onContactInvited" + realm.where(AddFriendMsg.class).findFirst().toString());
            }else if(msg.what==2){
                SendNotification(msg.obj.toString() + "接受了你的好友请求", "", ContactActivity.class, false);
            }
            else if(msg.what==3){
                SendNotification(msg.obj.toString() + "拒绝了你的好友请求", "", ContactActivity.class, false);
            }
            else if(msg.what==4){
               // EMMessage emMessage=(EMMessage)msg.obj;
                //存入本地数据库
                ChatMsgEntity entity = (ChatMsgEntity) msg.obj;
                int id=1;
                for(ChatMsgEntity c:realm.where(ChatMsgEntity.class).equalTo("name",entity.getName()).findAll()){
                    if(id<c.getChatId()){
                        id=c.getChatId();
                    }
                }
                entity.setChatId(id+1);
                realm.beginTransaction();
                realm.insertOrUpdate(entity);
                realm.commitTransaction();
                SendNotification(entity.getName() + "发来了一条消息", "内容：" + entity.getText(), ContactActivity.class, false);

                //发送广播给msgList页面


            }
        }
    };


    public void initAddFriend() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                //好友请求被同意
                Log.i("gqf", "onContactAdded" + username);

            }

            @Override
            public void onContactDeleted(String username) {
                Log.i("gqf", "onContactDeleted" + username);
                //被删除时回调此方法
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                Log.i("gqf", reason + "onContactInvited" + username);
                Message msg=new Message();
                msg.what=1;
                AddFriendMsg userFriend = new AddFriendMsg();
                userFriend.setName(username);
                userFriend.setMsg(reason);
                msg.obj=userFriend;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFriendRequestAccepted(String username) {
                //好友请求被接受
                Log.i("gqf", "onFriendRequestAccepted" + username);
                Message msg=new Message();
                msg.what=2;
                msg.obj=username;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFriendRequestDeclined(String username) {
                //好友请求被拒绝
                Log.i("gqf", "onFriendRequestDeclined" + username);
                Message msg=new Message();
                msg.what=3;
                msg.obj=username;
                mHandler.sendMessage(msg);

            }
        });
    }

    private void SendNotification(String username, String message, Class<?> cls, boolean isGroup) {

        //点击通知栏跳转界面
        Intent updateIntent = new Intent(getApplicationContext(), cls);
        PendingIntent updatePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, updateIntent, 0);

        //下载通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.edi_img)
                .setContentTitle(username)
                .setContentText(message)
                .setContentIntent(updatePendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

    }
}













