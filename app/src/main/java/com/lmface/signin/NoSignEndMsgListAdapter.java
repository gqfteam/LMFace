package com.lmface.signin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.SignUserMsg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/27.
 */

public class NoSignEndMsgListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<SignUserMsg> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    private CompositeSubscription mcompositeSubscription;


    public SignUserMsg getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public List<SignUserMsg> getDatas() {
        return datas;
    }


    public void setDatas(List<SignUserMsg> datas) {
        this.datas = datas;
    }

    public NoSignEndMsgListAdapter(Context mContext, List<SignUserMsg> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        mcompositeSubscription = new CompositeSubscription();
    }

    public void update(List<SignUserMsg> mDatas) {
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


    public void showDialog(String str, final int positio, final boolean statu) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(str)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(statu){
                            //取消签到
                        }else{
                            //帮助签到
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

        String name="";
        name=datas.get(p).getUser_msg().getUserName();
        if(datas.get(p).getUser_msg().getNickname()!=null) {
            if (!datas.get(p).getUser_msg().getNickname().equals("")) {
                name=datas.get(p).getUser_msg().getNickname();
            }
        }
        if(datas.get(p).getUser_msg().getRealname()!=null) {
            if (!datas.get(p).getUser_msg().getRealname().equals("")) {
                name=datas.get(p).getUser_msg().getRealname();
            }
        }

        mHolder.needSignUser.setText(name);
        datas.get(p).setName(name);

        if(datas.get(p).isSign()){
            //已签到
            mHolder.needSignIssign.setText("已签到");
        }else{
            //未签到
            mHolder.needSignIssign.setText("未签到");
        }
        mHolder.needSignIssignCheck.setChecked(datas.get(p).isSign());
        mHolder.needSignIssignCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datas.get(p).isSign()){
                    //已签到
                    showDialog("确定取消"+datas.get(p).getName()+"签到？",p,datas.get(p).isSign());
                }else{
                    //未签到
                    showDialog("确定帮助"+datas.get(p).getName()+"签到？",p,datas.get(p).isSign());
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

        public void showDialog(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.need_sign_user)
        TextView needSignUser;
        @BindView(R.id.need_sign_issign)
        TextView needSignIssign;
        @BindView(R.id.need_sign_issign_check)
        CheckBox needSignIssignCheck;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
