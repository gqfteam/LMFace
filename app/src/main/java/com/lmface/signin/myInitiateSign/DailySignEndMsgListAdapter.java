package com.lmface.signin.myInitiateSign;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.UserFriend;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/3/1.
 */

public class DailySignEndMsgListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<UserFriend> datas;
    private final LayoutInflater mLayoutInflater;
    private  MyItemClickListener mItemClickListener;

    private CompositeSubscription mcompositeSubscription;


    public UserFriend getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public List<UserFriend> getDatas() {
        return datas;
    }


    public void setDatas(List<UserFriend> datas) {
        this.datas = datas;
    }

    public DailySignEndMsgListAdapter(Context mContext, List<UserFriend> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        mcompositeSubscription = new CompositeSubscription();
    }

    public void update(List<UserFriend> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.sign_msg_list_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        //View v = mLayoutInflater.inflate(R.layout.my_order_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new  ViewHolder(v);

        return viewHolder;
    }


    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener( MyItemClickListener listener) {
        mItemClickListener = listener;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final  ViewHolder mHolder = ( ViewHolder) holder;

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(p);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (p == getPositionForSection(section)) {
            mHolder.friendsListItemChart.setVisibility(View.VISIBLE);
            mHolder.friendsListItemChart.setText(datas.get(p).getSortLetters());
        } else {
            mHolder.friendsListItemChart.setVisibility(View.GONE);
        }


        String name = "";
        name = datas.get(p).getUserName();
        if (datas.get(p).getNickname() != null) {
            if (!datas.get(p).getNickname().equals("")) {
                name = datas.get(p).getNickname();
            }
        }
        if (datas.get(p).getRealeName() != null) {
            if (!datas.get(p).getRealeName().equals("")) {
                name = datas.get(p).getRealeName();
            }
        }

        datas.get(p).setIsUseName(name);
        mHolder.needSignUser.setText(name);


        mHolder.needSignIssignCheck.setVisibility(View.GONE);
        mHolder.needSignIssign.setText(datas.get(p).getSignNum()+"/"+datas.get(p).getNeedSignNum());

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void onChangeStatu();

        public void showDialog(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.need_sign_user)
        TextView needSignUser;
        @BindView(R.id.need_sign_issign)
        TextView needSignIssign;
        @BindView(R.id.need_sign_issign_check)
        CheckBox needSignIssignCheck;
        @BindView(R.id.friends_list_item_chart)
        TextView friendsListItemChart;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return datas.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = datas.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }
    public int getCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }
    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    public Object[] getSections() {
        return null;
    }
}

