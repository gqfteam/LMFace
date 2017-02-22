package com.lmface.huanxin;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.lmface.R;
import com.lmface.pojo.AddFriendMsg;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/19.
 */

public class AddFriendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<AddFriendMsg> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    Realm realm;
    private CompositeSubscription mcompositeSubscription;

    public AddFriendMsg getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public List<AddFriendMsg> getDatas() {
        return datas;
    }

    public void setDatas(List<AddFriendMsg> datas) {
        this.datas = datas;
    }

    public AddFriendsListAdapter(Context mContext, List<AddFriendMsg> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        mcompositeSubscription = new CompositeSubscription();
        realm=Realm.getDefaultInstance();
    }

    public void update(List<AddFriendMsg> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.add_friedns_list_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        //View v = mLayoutInflater.inflate(R.layout.my_order_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    public interface MyItemClickListener {
        public void onChangeStatu();
    }
    public void yesFriend(final String username){
       new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                    Message message = new Message();
                    message.what =1;
                    message.obj=username;
                    myHandler.sendMessage(message);
                } catch (Exception e) {

                }

            }
        }).start();
    }
    public void noFriend(final String username){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().declineInvitation(username);
                    Message message = new Message();
                    message.what = 2;
                    message.obj=username;
                    myHandler.sendMessage(message);
                } catch (Exception e) {

                }

            }
        }).start();
    }
    Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String name=(String)msg.obj;
            for(int i=0;i<datas.size();i++){
                if(datas.get(i).getName().equals(name)){
                    realm.beginTransaction();
                    datas.get(i).deleteFromRealm();
                    realm.commitTransaction();

                    datas.remove(i);

                    break;
                }
            }
            AddFriendsListAdapter.this.notifyDataSetChanged();
        }
    };
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;

        if(datas.get(p).getImg()!=null){
            if(!datas.get(p).getImg().equals("")){
                Picasso.with(mContext).load(datas.get(p).getImg())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(mHolder.addFriendsListItemImg);
            }
        }

        mHolder.addFriendsListItemTxt1.setText(datas.get(p).getName());
        mHolder.addFriendsListItemTxt2.setText(datas.get(p).getMsg());
        mHolder.addFriendsListItemYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //同意
                yesFriend(datas.get(p).getName());
            }
        });
        mHolder.addFriendsListItemNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拒绝
                noFriend(datas.get(p).getName());
            }
        });
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        mItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.add_friends_list_item_img)
        ImageView addFriendsListItemImg;
        @BindView(R.id.add_friends_list_item_txt1)
        TextView addFriendsListItemTxt1;
        @BindView(R.id.add_friends_list_item_txt2)
        TextView addFriendsListItemTxt2;
        @BindView(R.id.add_friends_list_item_yes)
        Button addFriendsListItemYes;
        @BindView(R.id.add_friends_list_item_no)
        Button addFriendsListItemNo;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

