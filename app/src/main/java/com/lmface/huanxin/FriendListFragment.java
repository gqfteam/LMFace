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
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.UserFriend;
import com.lmface.pojo.user_msg;
import com.lmface.view.SideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * gqf
 * 通讯录好友列表
 */
public class FriendListFragment extends Fragment {


    @BindView(R.id.friend_list_allfriends)
    ListView friendListAllfriends;
    @BindView(R.id.friend_list_sidebar)
    SideBar friendListSidebar;
    @BindView(R.id.friend_list_dialog)
    TextView friendListDialog;
    private List<String> usernames;
    private FriendsListAdapter mFriendsListAdapter;
    private Thread mThread;
    private DemoHelper demoHelper;
    private Context mContext;
    CompositeSubscription mcompositeSubscription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        mcompositeSubscription=new CompositeSubscription();
        initFriendsData();

        return view;
    }

    public void initFriendsData(){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Message message = new Message();
                    message.what = 1;
                    myHandler.sendMessage(message);
                } catch (Exception e) {

                }

            }
        });
        mThread.start();
    };

    public void delectFriend(final String username){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    Message message = new Message();
                    message.what = 2;
                    myHandler.sendMessage(message);
                } catch (Exception e) {

                }

            }
        });
        mThread.start();
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initList(usernames);
                    break;
                case 2://删除好友后
                    initFriendsData();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //    public void intentChatFragment(){
    //        friendListAllfriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    //            @Override
    //            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    //                Log.i("wjd","friendName:"+usernames.get(i));
    //
    //                        Intent _intent = new Intent(getActivity(), ChatActivity.class);
    //                        _intent.putExtra("friendName",usernames.get(i));
    //                        startActivity(_intent);
    //            }
    //        });
    //    }

    public void initSideBar() {
        friendListSidebar.setTextView(friendListDialog);
        // 设置右侧触摸监听
        friendListSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mFriendsListAdapter.getPositionForSection(s
                        .charAt(0));
                if (position != -1) {
                    friendListAllfriends.setSelection(position);
                }

            }
        });
    }
    ArrayList<UserFriend> users;
    public void initList(final List<String> usernames) {
        users = new ArrayList<>();
        for (int i = 0; i < usernames.size(); i++) {
            Log.i("wjd", "friendName:" + usernames.get(i));
            UserFriend user = new UserFriend(usernames.get(i));
            user.setUserName(usernames.get(i));
            users.add(user);
        }
        demoHelper = DemoHelper.getInstance();
        users = demoHelper.filledData(users);
        user_msgs=new ArrayList<>();
        //根据用户名查找用户信息
        if(users.size()>0) {
            initUserMsgList(users.get(0).getUserName());
        }
        initList();

    }

    public void initList(){
        if(mFriendsListAdapter==null) {
            mFriendsListAdapter = new FriendsListAdapter(getContext(), users);
            friendListAllfriends.setAdapter(mFriendsListAdapter);
            friendListAllfriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent _intent = new Intent(getActivity(), ChatActivity.class);
                    _intent.putExtra("friendName", usernames.get(i));
                    _intent.putExtra("FriendList_to_ChatFragment", true);
                    startActivity(_intent);
                }
            });
            friendListAllfriends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                    //长按删除好友
                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setTitle("是否删除联系人"+users.get(position).getUserName()+"")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    delectFriend(users.get(position).getUserName());
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
        }else{
            mFriendsListAdapter.update(users);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
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
                        Log.i("gqf","user_msg"+user_msg.toString());
                        user_msgs.add(user_msg);
                        users.get(user_msgs.size()-1).setMsg(user_msg.getNickname(),user_msg.getHeadimg(),user_msg.getSex(),user_msg.getPhone());
                        if(users.size()>user_msgs.size()){
                            initUserMsgList(users.get(user_msgs.size()).getUserName());
                        }else{
                            initList();
                        }

                    }
                });
        mcompositeSubscription.add(subscription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mcompositeSubscription.unsubscribe();
    }
    //    id,username,password(服务器)
    // id,username,userimg+。。。（服务器）
    //id,username,聊天记录,好友username(本地，表名username+。。。)

}
