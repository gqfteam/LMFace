package com.lmface.signin.mySignHistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.temporary_sign_msg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wjy on 2016/11/7.
 */

public class TemporarySignHistoryAdapter extends RecyclerView.Adapter<TemporarySignHistoryAdapter.ViewHolder> {

    List<temporary_sign_msg> datas;
    MyItemClickListener mItemClickListener;


    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public int getLayout() {
        return R.layout.temporary_sign_list_item;
    }

    public List<temporary_sign_msg> getDatas() {
        return datas;
    }

    public void setDatas(List<temporary_sign_msg> datas) {
        this.datas = datas;
    }

    public TemporarySignHistoryAdapter(List<temporary_sign_msg> datas, Context mContext) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final temporary_sign_msg data = datas.get(position);
        holder.temporarySignListUserName.setText(data.getUserName());
        holder.temporarySignListIntervalTime.setText(data.getSignintervaltime()+"分钟");
        holder.temporarySignListPurpose.setText(data.getSigngoal());
        holder.temporarySignListAddress.setText(data.getSignaddress());
        holder.temporarySignListStartTime.setText(data.getSignstarttime().toString());

    }

    @Override
    public int getItemCount() {
        //        Log.i("Daniel","---datas.size()---"+datas.size());
        return datas != null ? datas.size() : 0;
    }

    public void setdatas(List<temporary_sign_msg> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // View rootView;
        @BindView(R.id.temporary_sign_list_courseName)
        TextView temporarySignListCourseName;
        @BindView(R.id.temporary_sign_list_user_name)
        TextView temporarySignListUserName;
        @BindView(R.id.temporary_sign_list_interval_time)
        TextView temporarySignListIntervalTime;
        @BindView(R.id.temporary_sign_commit_btn)
        Button temporarySignCommitBtn;
        @BindView(R.id.temporary_sign_list_purpose)
        TextView temporarySignListPurpose;
        @BindView(R.id.temporary_sign_list_address)
        TextView temporarySignListAddress;
        @BindView(R.id.temporary_sign_list_start_time)
        TextView temporarySignListStartTime;
        @BindView(R.id.sign_lin)
        LinearLayout signLin;

        RelativeLayout RelativeLayoutItem;


        ViewHolder(View view) {
            super(view);
            //            rootView = view;
            ButterKnife.bind(this, view);
            //            this.mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //            if (mListener != null) {
            //                mListener.onItemClick(view, getPosition());
            //            }

        }
    }
}
