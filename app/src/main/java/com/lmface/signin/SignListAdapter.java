package com.lmface.signin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.network.api.LocationService;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.sign_user_msg;
import com.lmface.pojo.user_msg;
import com.lmface.pojo.user_sign;
import com.lmface.signin.myinterface.LocationInterface;

import java.sql.Timestamp;
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
 * 签到列表
 * Created by johe on 2017/2/27.
 */

public class SignListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<sign_user_msg> datas;
    private final LayoutInflater mLayoutInflater;
    private LocationInterface locationInterface;
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

    public SignListAdapter(Context mContext, List<sign_user_msg> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        realm=Realm.getDefaultInstance();
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
    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOncompareLocationDistanceListener(LocationInterface listener) {
        locationInterface = listener;
    }
    public void showDialog(String str, final int positio, final boolean statu) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(str)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sign(positio);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.create().show();
    }

    public void sign(final int position){

        Gson g=new Gson();
        user_sign user_sign=new user_sign();
        user_sign.setUserId(realm.where(user_msg.class).findFirst().getUserId());
        user_sign.setSignStatu(1);
        user_sign.setSignTime(new Timestamp(System.currentTimeMillis()));
        user_sign.setInitialsignininfoid(datas.get(position).getSigninfoid());
        user_sign.setSignlatitude("34.432423");
        user_sign.setSignlongitude("35.5345345");

        Subscription subscription = NetWork.getSignService().sign(g.toJson(user_sign))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode data) {
                        Log.i("gqf","onNext"+datas.toString());
                        if(data.getCode()==10000){
                            datas.remove(position);
                            SignListAdapter.this.notifyDataSetChanged();
                        }else{
                            Toast.makeText(mContext,data.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mcompositeSubscription.add(subscription);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.temporarySignCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取经纬度信息
                locationInterface.compareLocationDistance(Double.valueOf(datas.get(p).getAddresslatitude())
                        ,Double.valueOf(datas.get(p).getAddresslongitude()));

                showDialog("确定签到？", p, true);
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

        //判断开始时间是否小于或等于当前时间
        if(datas.get(p).getSignstarttime().getTime()<=System.currentTimeMillis()){
            mHolder.temporarySignCommitBtn.setEnabled(false);
        }else{
            mHolder.temporarySignCommitBtn.setEnabled(true);
        }

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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }



    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);

                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度

                logMsg(location.getLongitude()+"",location.getLatitude()+"");
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };

    /**
     * 显示请求字符串
     *
     * @param lng
     * @param lat
     */
    public void logMsg(final  String lng,final  String lat) {

        try {
          /*  if (signInGpsBtn != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        signInGpsBtn.post(new Runnable() {
                            @Override
                            public void run() {
                                longitudeTxt.setText(lng);
                                latitudeTxt.setText(lat);
                            }
                        });

                    }
                }).start();}*/

            //LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
