package com.lmface.signin.myInitiateSign;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.sign_user_msg;
import com.lmface.signin.NowSignEndMsgActivity;

import org.greenrobot.eventbus.EventBus;

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
 * Created by johe on 2017/2/27.
 */

public class DailySignListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<sign_user_msg> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    Realm realm;
    private CompositeSubscription mcompositeSubscription;


    public sign_user_msg getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public List<sign_user_msg> getDatas() {
        return datas;
    }


    public void setDatas(List<sign_user_msg> datas) {
        this.datas = datas;
    }

    public DailySignListAdapter(Context mContext, List<sign_user_msg> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        realm = Realm.getDefaultInstance();
        mLayoutInflater = LayoutInflater.from(mContext);
        mcompositeSubscription = new CompositeSubscription();
    }

    public void update(List<sign_user_msg> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.temporary_sign_list_item;
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
                        delect(positio);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.create().show();
    }

    public void delect(final int position) {

        Subscription subscription = NetWork.getSignService().delectSignInfo(datas.get(position).getSigninfoid())
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
                            datas.remove(position);
                            DailySignListAdapter.this.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mcompositeSubscription.add(subscription);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.temporarySignCommitBtn.setText("删除");
        mHolder.temporarySignCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("确定删除本次签到？", p, true);
            }
        });

        mHolder.signLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送广播通知，跳转签到详情页
                Intent intent=new Intent(mContext, NowSignEndMsgActivity.class);
                intent.putExtra("initiateSignId",datas.get(p).getSigninfoid());
                EventBus.getDefault().post(intent);
            }
        });

        mHolder.temporarySignListAddress.setText(datas.get(p).getSignaddress());
        mHolder.temporarySignListCourseName.setText(datas.get(p).getCoursename());
        mHolder.temporarySignListIntervalTime.setText("持续时间:" + datas.get(p).getSignintervaltime()+"分");
        mHolder.temporarySignListStartTime.setText("开始时间" + datas.get(p).getSignstarttime());

        String name = "";
        name = datas.get(p).getUserName();
        if (datas.get(p).getNickname() != null) {
            if (!datas.get(p).getNickname().equals("")) {
                name = datas.get(p).getNickname();
            }
        }
        if (datas.get(p).getRealname() != null) {
            if (!datas.get(p).getRealname().equals("")) {
                name = datas.get(p).getRealname();
            }
        }

        mHolder.temporarySignListUserName.setText("发起人：" + name);

        mHolder.temporarySignListPurpose.setText("签到目的："+datas.get(p).getSigngoal());



    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void startActivityToShowMsg(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
