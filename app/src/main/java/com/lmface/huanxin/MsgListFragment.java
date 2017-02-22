package com.lmface.huanxin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.UserFriend;
import com.lmface.pojo.user_msg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * gqf
 * 通讯录消息列表
 */
public class MsgListFragment extends Fragment {


    @BindView(R.id.contact_msg_list)
    ListView contactMsgList;
    private ArrayList<UserFriend> mUserFriends;
    private UserFriend mUserFriend;
    private MsgListAdapter msgListAdapter;
    private Map<String, EMConversation> conversations;
    private Thread mThread;
    private Context mContext;

    CompositeSubscription mcompositeSubscription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_msg_list, container, false);
        ButterKnife.bind(this, view);

        mcompositeSubscription=new CompositeSubscription();
        initAllConversations();


        return view;
    }
    public void initAllConversations(){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conversations = EMClient.getInstance().chatManager().getAllConversations();
                    //                    Log.i("wjd","conversations.size():"+conversations.size());

                } catch (Exception e) {
                }
                if (conversations.size() != 0) {
                    Message message = new Message();
                    message.what = 1;
                    myHandler.sendMessage(message);
                }
            }
        });
        mThread.start();
    }


    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initList();
                    break;
                case 2:
                    initAllConversations();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void initList() {
        mContext = getActivity();
        mUserFriends = new ArrayList<>();
        Set<String> friends = conversations.keySet();
        ArrayList<String> friendsname = new ArrayList<String>();
        for (String user : friends) {
            friendsname.add(user);
        }
        for (int i = 0; i < conversations.size(); i++) {
            mUserFriend = new UserFriend(friendsname.get(i));
            //设置昵称
            //mUserFriend.setNick("111");
            mUserFriend.setUserName(friendsname.get(i));
            mUserFriend.setMessages(conversations.get(friendsname.get(i)).getAllMessages());
            mUserFriends.add(mUserFriend);
        }
        //根据用户名查找用户信息
        if(mUserFriends.size()>0) {
            initUserMsgList(mUserFriends.get(0).getUserName());
        }

        msgListAdapter = new MsgListAdapter(getContext(), mUserFriends);
        contactMsgList.setAdapter(msgListAdapter);
    }
    public void delectConversations(final String name){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //删除和某个user会话，如果需要保留聊天记录，传false
                    EMClient.getInstance().chatManager().deleteConversation(name, true);
                } catch (Exception e) {
                }
                if (conversations.size() != 0) {
                    Message message = new Message();
                    message.what = 2;
                    myHandler.sendMessage(message);
                }
            }
        });
        mThread.start();
    }
    public void initList(ArrayList<UserFriend> users){
        if(msgListAdapter==null){
            msgListAdapter = new MsgListAdapter(getContext(), users);
            contactMsgList.setAdapter(msgListAdapter);
        }else{
            msgListAdapter.update(users);
        }
        contactMsgList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //长按删除会话
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("是否删除与"+mUserFriends.get(position).getUserName()+"的会话")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                delectConversations(mUserFriends.get(position).getUserName());
                            }
                        })
                        .setNegativeButton("否",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alert.create().show();
                return false;
            }
        });
    }

    List<user_msg> user_msgs;
    public void initUserMsgList(String userName){

        //多次查询后刷新list
        Subscription subscription = NetWork.getUserService().selectUserByName(userName)
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
                        user_msgs.add(user_msg);
                        mUserFriends.get(user_msgs.size()-1).setMsg(user_msg.getNickname(),user_msg.getHeadimg(),user_msg.getSex(),user_msg.getPhone());
                        if(mUserFriends.size()>user_msgs.size()){
                            initUserMsgList(mUserFriends.get(user_msgs.size()).getUserName());
                        }else{
                            initList(mUserFriends);
                        }

                    }
                });
        mcompositeSubscription.add(subscription);
    }
    @OnItemClick(R.id.contact_msg_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(mContext, ChatActivity.class);
        Log.i("wjd", "mUserFriends.size():" + mUserFriends.size());
        Log.i("wjd", "mUserFriends.get(i).getMessages():" + mUserFriends.get(i).getMessages().size());
        intent.putExtra("friendName", mUserFriends.get(i).getUserName());
        intent.putExtra("FriendList_to_ChatFragment", true);
        intent.putExtra("index", i);
        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        initList(mUserFriends);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mcompositeSubscription.unsubscribe();
    }

}
