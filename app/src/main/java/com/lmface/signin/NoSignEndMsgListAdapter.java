package com.lmface.signin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.UserFriend;
import com.lmface.pojo.user_sign;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 */

public class NoSignEndMsgListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<UserFriend> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;
    private List<Integer> signUserIds;
    private CompositeSubscription mcompositeSubscription;

    int signInfoId = 0;

    public int getSignInfoId() {
        return signInfoId;
    }

    public void setSignInfoId(int signInfoId) {
        this.signInfoId = signInfoId;
    }

    public List<Integer> getSignUserIds() {
        return signUserIds;
    }

    public void setSignUserIds(List<Integer> signUserIds) {
        this.signUserIds = signUserIds;
    }

    public UserFriend getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public List<UserFriend> getDatas() {
        return datas;
    }


    public void setDatas(List<UserFriend> datas) {
        this.datas = datas;
    }

    public NoSignEndMsgListAdapter(Context mContext, List<UserFriend> mDatas) {
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
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void sign(final int position, final int signStatu) {

        Gson g = new Gson();
        user_sign user_sign = new user_sign();
        user_sign.setUserId(datas.get(position).getUserId());
        user_sign.setSignStatu(signStatu);
        user_sign.setInitialsignininfoid(signInfoId);

        Subscription subscription = NetWork.getSignService().sign(g.toJson(user_sign))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode data) {
                        Log.i("gqf", "onNext" + datas.toString());
                        if (data.getCode() == 10000) {
                            if (signStatu == 0) {
                                //被取消成功
                                for (int i = 0; i < signUserIds.size(); i++) {
                                    Log.i("gqf", "signUserIds" + signUserIds.get(i));
                                    if (signUserIds.get(i) == datas.get(position).getUserId()) {
                                        signUserIds.remove(i);
                                        break;
                                    }
                                }
                            } else {
                                //帮助签到成功
                                signUserIds.add(datas.get(position).getUserId());
                            }
                            //刷新单条数据


                            NoSignEndMsgListAdapter.this.notifyItemChanged(position);

                        } else {
                            Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mcompositeSubscription.add(subscription);

    }

    public void showDialog(String str, final int positio, final boolean statu) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(str)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (statu) {
                            //取消签到
                            sign(positio, 0);
                        } else {
                            //帮助签到
                            sign(positio, 1);
                        }
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.create().show();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;

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

        datas.get(p).setSingIn(false);
        for (int userId : signUserIds) {
            if (userId == datas.get(p).getUserId()) {
                datas.get(p).setSingIn(true);
                Log.i("gqf", "signInUserName" + datas.get(p).getUserName());
                break;
            }
        }

        if (datas.get(p).isSingIn()) {
            //已签到
            mHolder.needSignIssign.setText("已签到");
        } else {
            //未签到
            mHolder.needSignIssign.setText("未签到");
        }
        mHolder.needSignIssignCheck.setEnabled(false);
        Log.i("gqf", datas.get(p).getUserName() + "needSignIssignCheck" + datas.get(p).isSingIn());


        mHolder.needSignIssignCheck.setChecked(datas.get(p).isSingIn());


        Log.i("gqf", "needSignIssignCheck" + mHolder.needSignIssignCheck.isChecked());

        mHolder.signMsgListItemLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datas.get(p).isSingIn()) {
                    //已签到
                    showDialog("确定取消" + datas.get(p).getIsUseName() + "签到？", p, datas.get(p).isSingIn());
                } else {
                    //未签到
                    showDialog("确定帮助" + datas.get(p).getIsUseName() + "签到？", p, datas.get(p).isSingIn());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void onChangeStatu();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.need_sign_user)
        TextView needSignUser;
        @BindView(R.id.need_sign_issign)
        TextView needSignIssign;
        @BindView(R.id.friends_list_item_chart)
        TextView friendsListItemChart;

        @BindView(R.id.need_sign_issign_check)
        CheckBox needSignIssignCheck;
        @BindView(R.id.sign_msg_list_item_lin)
        LinearLayout signMsgListItemLin;

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
