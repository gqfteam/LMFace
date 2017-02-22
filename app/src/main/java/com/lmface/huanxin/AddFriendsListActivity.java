package com.lmface.huanxin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.AddFriendMsg;
import com.lmface.pojo.user_msg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/19.
 */

public class AddFriendsListActivity extends AppCompatActivity {


    Realm realm;
    CompositeSubscription mcompositeSubscription;
    @BindView(R.id.add_friends_list_toolbar)
    Toolbar addFriendsListToolbar;
    @BindView(R.id.add_friends_list)
    RecyclerView addFriendsList;

    List<AddFriendMsg> userNames;
    List<AddFriendMsg> addFriendMsgs;


    AddFriendsListAdapter addFriendsListAdapter;
    public void setToolbar(String str) {
        addFriendsListToolbar.setTitle(str);
        addFriendsListToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(addFriendsListToolbar);
        addFriendsListToolbar.setBackgroundResource(R.color.colorPrimary);
        addFriendsListToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        addFriendsListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_list);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        EventBus.getDefault().register(this);
        setToolbar("好友添加请求");
        addFriendMsgs=new ArrayList<>();
        userNames=realm.where(AddFriendMsg.class).findAll();
        if(userNames.size()>0) {
            Log.i("gqf","userNames"+userNames.toString());
            initDataImg(userNames.get(0));
        }
        initList(addFriendMsgs);
       // initLisener();
    }

    public void initList(List<AddFriendMsg> AddFriendMsg){

        if(addFriendsListAdapter==null){
            addFriendsListAdapter=new AddFriendsListAdapter(this,AddFriendMsg);
            addFriendsList.setLayoutManager(new LinearLayoutManager(this));
            addFriendsList.setAdapter(addFriendsListAdapter);

        }else{
            addFriendsListAdapter.update(AddFriendMsg);
        }
    }

    public void initDataImg(AddFriendMsg userFriend){

        Log.i("gqf","initDataImg"+userFriend.toString());
        Subscription subscription = NetWork.getUserService().selectUserByName(userFriend.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<user_msg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.getMessage());
                    }

                    @Override
                    public void onNext(user_msg user_msg) {
                        realm.beginTransaction();
                        userNames.get(addFriendMsgs.size()).setImg(user_msg.getHeadimg());
                        realm.copyToRealmOrUpdate(userNames.get(addFriendMsgs.size()));
                        realm.commitTransaction();

                        addFriendMsgs.add(userNames.get(addFriendMsgs.size()));
                        Log.i("gqf","onNext"+user_msg.toString());
                        if(addFriendMsgs.size()==userNames.size()){
                            initList(addFriendMsgs);
                        }else{
                            initDataImg(userNames.get(addFriendMsgs.size()));
                        }
                    }
                });
        mcompositeSubscription.add(subscription);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initImgUrl(final AddFriendMsg userFriend){
        Log.i("gqf","initImgUrl"+userFriend.toString());
        Subscription subscription = NetWork.getUserService().selectUserByName(userFriend.getName())
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
                        userFriend.setImg(user_msg.getHeadimg());
                        if(addFriendsListAdapter!=null){
                            addFriendMsgs=addFriendsListAdapter.getDatas();
                        }
                        addFriendMsgs.add(userFriend);
                        initList(userNames);
                    }
                });
        mcompositeSubscription.add(subscription);

    }


    public void initLisener(AddFriendMsg userFriend){
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                //好友请求被同意
                Log.i("gqf","onContactAdded"+username);
            }

            @Override
            public void onContactDeleted(String username) {
                Log.i("gqf","onContactDeleted"+username);
                //被删除时回调此方法
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                Log.i("gqf",reason+"AddFriendsListActivity"+username);
//                AddFriendMsg userFriend=new AddFriendMsg();
//                userFriend.setName(username);
//                userFriend.setMsg(reason);
//                realm.beginTransaction();
//                realm.insertOrUpdate(userFriend);
//                realm.commitTransaction();
//                initImgUrl(userFriend);
            }

            @Override
            public void onFriendRequestAccepted(String username) {
                //好友请求被接受
                Log.i("gqf","onFriendRequestAccepted"+username);
            }

            @Override
            public void onFriendRequestDeclined(String username) {
                //好友请求被拒绝
                Log.i("gqf","onFriendRequestDeclined"+username);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mcompositeSubscription.unsubscribe();
        EventBus.getDefault().unregister(this);
    }
}
