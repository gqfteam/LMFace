package com.lmface.huanxin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ChatMsgEntity;
import com.lmface.pojo.UserFriend;
import com.lmface.pojo.user_msg;
import com.lmface.util.TimeUtils;
import com.lmface.util.ToastUtil;
import com.lmface.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ChatActivity extends Activity implements OnClickListener {

    private Button mBtnSend;
    public static boolean isActive = false;
    private EditText mEditTextContent;
    private TitleBarView mTitleBarView;
    private ListView mListView;
    private TextView tv_new_news;
    private ChatMsgAdapter mAdapter;
    private String username;
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    Realm realm;
    //private MyReceiver myBroadcastReceiver;
    private int type;
    user_msg friendMsg=null;
    CompositeSubscription compositeSubscription;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);
        realm = Realm.getDefaultInstance();
        compositeSubscription=new CompositeSubscription();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        initTitleView();
        initData();

        //iniMsg();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    public void  initFriendMsg(){
        Subscription logSc = NetWork.getUserService().selectUserByName(username)
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
                        friendMsg=user_msg;
                        if(mAdapter!=null){
                            mAdapter.setHeadImg(realm.where(user_msg.class).findFirst().getHeadimg(),friendMsg.getHeadimg());
                        }
                    }
                });
        compositeSubscription.add(logSc);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ChatMsgEntity entity = (ChatMsgEntity) msg.obj;
//                int id=1;
//                for(ChatMsgEntity c:realm.where(ChatMsgEntity.class).equalTo("name",entity.getName()).findAll()){
//                    if(id<c.getChatId()){
//                        id=c.getChatId();
//                    }
//                }
//                entity.setChatId(id+1);
//                realm.beginTransaction();
//                realm.insertOrUpdate(entity);
//                realm.commitTransaction();
                mAdapter.addChat(entity);
                mListView.smoothScrollToPosition(mListView.getChildCount()-1);
            }
        }
    };
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
                msg.what = 1;
                msg.obj = entity;
                handler.sendMessage(msg);

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

    private void initTitleView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        mTitleBarView.setTitleText(username);
        mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
        mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        username = getIntent().getStringExtra("friendName");
        tv_new_news = (TextView) findViewById(R.id.tv_new_news);
        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        mListView = (ListView) findViewById(R.id.listview);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }

    //从本地数据库这种获取聊天记录
    public void initData() {
        for (ChatMsgEntity chatMsgEntity : realm.where(ChatMsgEntity.class).equalTo("name", username).findAll()) {
            mDataArrays.add(chatMsgEntity);
        }
        mAdapter = new ChatMsgAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
        //getOldMessage(username);
        if(friendMsg==null) {
            //更新头像
            initFriendMsg();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                send();
                break;
        }
    }


    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (!TextUtils.isEmpty(contString)) {
            if (type == 0) {
                sendMessage(contString, username);
            } else if (type == 1) {
                sendMessage(contString, getIntent().getStringExtra("groupId"));
            }
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(getDate());
            entity.setMsgType(false);
            entity.setText(contString);

            entity.setFromName(realm.where(user_msg.class).findFirst().getUserName());
            entity.setName(username);

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

            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);



        } else
            ToastUtil.showToast("消息不能为空！");
    }

    private void send(String message) {
        if (!TextUtils.isEmpty(message)) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(getDate());
            entity.setMsgType(true);
            entity.setText(message);

            entity.setFromName(realm.where(user_msg.class).findFirst().getUserName());
            entity.setName(username);

            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);
        } else
            ToastUtil.showToast("消息不能为空！");
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));

        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
                + mins);

        return sbBuffer.toString();
    }

    /**
     * 发送文本消息及表情
     *
     * @param content
     */
    private void sendMessage(String content, String id) {

        Intent intentbro = new Intent();
        intentbro.setAction(Constant.CHATNLIST);
        sendBroadcast(intentbro);

        Log.e("send", "id:" + id + ",type:" + type);
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(id);

        //创建一条文本消息

        //如果是群聊，设置chattype,默认是单聊

        EMMessage message = EMMessage.createTxtSendMessage(content, id);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("gqf", "onSuccess");
            }

            @Override
            public void onError(int code, String error) {
                Log.i("gqf", code + "onError" + error);
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.i("gqf", progress + "onProgress" + status);
            }
        });

        if (type == 1) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        EMClient.getInstance().chatManager().sendMessage(message);
        //conversation.appendMessage(message);

        //发送广播给msglist页面
        UserFriend userFriend=new UserFriend(username);
        List<EMMessage> myEmMessages=new ArrayList<>();
        myEmMessages.add(message);
        userFriend.setMessages(myEmMessages);
        EventBus.getDefault().post(userFriend);
    }

    /**
     * 获取聊天记录
     */
    private void getOldMessage(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        //获取此会话的所有消息
        if (conversation != null) {
            List<EMMessage> messages = conversation.getAllMessages();

            for (int i = 0; i < messages.size(); i++) {
                ChatMsgEntity entity = new ChatMsgEntity();
                entity.setDate(TimeUtils.getFormaDatass(messages.get(i).getMsgTime()));
                if (!realm.where(user_msg.class).findFirst().getUserName().equals(messages.get(i).getFrom())) {
                    entity.setMsgType(true);
                    entity.setName(messages.get(i).getFrom());
                } else {
                    entity.setMsgType(false);
                    entity.setName(messages.get(i).getTo());
                }
                int startIndex = messages.get(i).getBody().toString().toString().lastIndexOf("txt:\"");
                String message_news = messages.get(i).getBody().toString().toString().substring(startIndex + 5,
                        messages.get(i).getBody().toString().toString().length() - 1);
                entity.setText(message_news);
                mDataArrays.add(entity);
                mAdapter.notifyDataSetChanged();






            }

        }

    }
  

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }





}