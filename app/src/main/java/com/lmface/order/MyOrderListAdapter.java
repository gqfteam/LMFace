package com.lmface.order;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.order_goods_usermsg;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/7.
 */

public class MyOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<order_goods_usermsg> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    private CompositeSubscription mcompositeSubscription;
    public order_goods_usermsg getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public MyOrderListAdapter(Context mContext, List<order_goods_usermsg> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        mcompositeSubscription=new CompositeSubscription();
    }

    public void update(List<order_goods_usermsg> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.my_order_list_item;
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
    public void changeOrderStatu(int position,int statu){
        Subscription subscription = NetWork.getGoodsOrderService().updateOrderStatus(datas.get(position).getOrderId(),statu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext.getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(ResultCode resultCode) {
                        if(resultCode.getCode()==10000){
                            //通知fragment刷新
                            if(mItemClickListener!=null){
                                mItemClickListener.onChangeStatu();
                            }
                        }else{
                            Toast.makeText(mContext.getApplicationContext(),resultCode.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void showDialog(String str,final int positio,final int statu){
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(str)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        changeOrderStatu(positio,statu);
                    }
                })
                .setNegativeButton("否",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.create().show();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.myOrderGoodsName.setText(datas.get(p).getGoodsname());
        mHolder.myOrderCourierPrice.setText("(含运费¥" + datas.get(p).getCourierMoney() + ")");
        mHolder.myOrderGoodsClassification.setText(datas.get(p).getGoodsclassification() + "/" + datas.get(p).getSpeciesname());
        mHolder.myOrderGoodsNum.setText("X" + datas.get(p).getOrderGoodsNum());
        mHolder.ymOrderGoodsNum.setText("共" + datas.get(p).getOrderGoodsNum() + "件商品");
        mHolder.myOrderStoreUserName.setText(datas.get(p).getStoreusernickname());
        mHolder.myOrderGoodsPrice.setText("" + datas.get(p).getGoodsprice());
        mHolder.myOrderPrice.setText("" + datas.get(p).getOrderPrice());

        String imgPath="";
        if(!datas.get(p).getGoodsimgaddress1().equals("")){
            imgPath=datas.get(p).getGoodsimgaddress1();
        }else if(!datas.get(p).getGoodsimgaddress2().equals("")){
            imgPath=datas.get(p).getGoodsimgaddress2();
        }else if(!datas.get(p).getGoodsimgaddress3().equals("")){
            imgPath=datas.get(p).getGoodsimgaddress3();
        }
        if(!imgPath.equals("")) {
            Picasso.with(mContext).load(imgPath)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(mHolder.myOrderGoodsImg);
        }
        switch (datas.get(p).getOrderStatus()) {
            case -1:
                mHolder.myOrderStatu.setText("订单已取消");
                mHolder.commitGetGoodsLin.setVisibility(View.GONE);
                break;
            case 0:
                mHolder.myOrderStatu.setText("等待卖家确认");
                mHolder.commitGetGoodsLin.setVisibility(View.VISIBLE);
                mHolder.commitCancelGoodsBtn.setVisibility(View.VISIBLE);
                mHolder.commitGetGoodsBtn.setVisibility(View.GONE);
                mHolder.commitCancelGoodsBtn.setText("点击取消");
                mHolder.commitCancelGoodsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //取消订单
                        showDialog("是否确取消订单",p,-1);
                    }
                });
                break;
            case 1:
                mHolder.myOrderStatu.setText("等待卖家发货");
                mHolder.commitGetGoodsLin.setVisibility(View.GONE);
                break;
            case 2:
                mHolder.myOrderStatu.setText("商品已发货");
                mHolder.commitGetGoodsBtn.setText("确认接收");
                mHolder.commitCancelGoodsBtn.setVisibility(View.GONE);
                mHolder.commitGetGoodsLin.setVisibility(View.VISIBLE);
                mHolder.commitGetGoodsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击确认收货，修改订单状态为3
                        showDialog("是否确定收货",p,3);
                    }
                });
                break;
            case 3:
                mHolder.myOrderStatu.setText("订单已完成");
                mHolder.commitGetGoodsLin.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void onChangeStatu();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.my_order_store_user_name)
        TextView myOrderStoreUserName;
        @BindView(R.id.my_order_statu)
        TextView myOrderStatu;
        @BindView(R.id.top_lin)
        LinearLayout topLin;
        @BindView(R.id.my_order_goods_img)
        ImageView myOrderGoodsImg;
        @BindView(R.id.my_order_goods_name)
        TextView myOrderGoodsName;
        @BindView(R.id.my_order_goods_classification)
        TextView myOrderGoodsClassification;
        @BindView(R.id.my_order_goods_price)
        TextView myOrderGoodsPrice;
        @BindView(R.id.my_order_goods_num)
        TextView myOrderGoodsNum;
        @BindView(R.id.my_order_msg_rel)
        RelativeLayout myOrderMsgRel;
        @BindView(R.id.ym_order_goods_num)
        TextView ymOrderGoodsNum;
        @BindView(R.id.my_order_price)
        TextView myOrderPrice;
        @BindView(R.id.my_order_courier_price)
        TextView myOrderCourierPrice;
        @BindView(R.id.commit_get_goods_btn)
        Button commitGetGoodsBtn;
        @BindView(R.id.commit_get_goods_lin)
        LinearLayout commitGetGoodsLin;
        @BindView(R.id.commit_cancel_goods_btn)
        Button commitCancelGoodsBtn;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
