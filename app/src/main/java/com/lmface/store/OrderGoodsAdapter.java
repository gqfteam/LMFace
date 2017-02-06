package com.lmface.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.goods_msg;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by johe on 2017/2/4.
 */

public class OrderGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<goods_msg> datas;
    private ArrayList<Integer> goodsNums;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;


    public goods_msg getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public OrderGoodsAdapter(Context mContext, List<goods_msg> mDatas, ArrayList<Integer> goodsNum) {
        this.mContext = mContext;
        this.datas = mDatas;
        this.goodsNums = goodsNum;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<goods_msg> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.order_goods_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHoder(v);

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHoder mHolder = (ViewHoder) holder;

        mHolder.orderGoodsItemNum.setText("共" + goodsNums.get(position)+"件商品");
        mHolder.orderGoodsItemPrice.setText("" + (goodsNums.get(position) * datas.get(position).getGoodsprice() + datas.get(position).getCourierMoney()));
        mHolder.orderGoodsItemCourier.setText(datas.get(position).getCourierName()+datas.get(position).getCourierMoney()+"元");
        mHolder.shopCarGoodsNum.setText("x"+goodsNums.get(position));
        mHolder.shopCarGoodsName.setText(datas.get(position).getGoodsname());
        mHolder.shopCarGoodsClassification.setText(datas.get(position).getGoodsclassification()+"/"+datas.get(position).getSpeciesname());
        mHolder.shopCarGoodsPrice.setText(datas.get(position).getGoodsprice()+"");
        mHolder.shopCarStoreUserName.setText("物主："+datas.get(position).getUserName());

        if(datas.get(position).getGoodsimgaddress1()!=null){
            if(!datas.get(position).getGoodsimgaddress1().equals("")){
                Picasso.with(mContext).load(datas.get(position).getGoodsimgaddress1())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(mHolder.shopCarGoodsImg);
            }
        }
        mHolder.shopCarCheck.setVisibility(View.GONE);
        mHolder.shopCarAtUser.setVisibility(View.GONE);
        mHolder.shopCarEdiRel.setVisibility(View.GONE);
        mHolder.shopCarEdi.setVisibility(View.GONE);




    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    static class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.shop_car_store_user_name)
        TextView shopCarStoreUserName;
        @BindView(R.id.shop_car_at_user)
        TextView shopCarAtUser;
        @BindView(R.id.shop_car_edi)
        TextView shopCarEdi;
        @BindView(R.id.top_lin)
        LinearLayout topLin;
        @BindView(R.id.shop_car_check)
        CheckBox shopCarCheck;
        @BindView(R.id.shop_car_goods_img)
        ImageView shopCarGoodsImg;
        @BindView(R.id.shop_car_goods_name)
        TextView shopCarGoodsName;
        @BindView(R.id.shop_car_goods_classification)
        TextView shopCarGoodsClassification;
        @BindView(R.id.shop_car_goods_price)
        TextView shopCarGoodsPrice;
        @BindView(R.id.shop_car_goods_num)
        TextView shopCarGoodsNum;
        @BindView(R.id.shop_car_msg_rel)
        RelativeLayout shopCarMsgRel;
        @BindView(R.id.shop_car_jian)
        Button shopCarJian;
        @BindView(R.id.shop_car_buy_num)
        TextView shopCarBuyNum;
        @BindView(R.id.shop_car_add)
        Button shopCarAdd;
        @BindView(R.id.shop_car_delect)
        TextView shopCarDelect;
        @BindView(R.id.shop_car_edi_rel)
        LinearLayout shopCarEdiRel;
        @BindView(R.id.select_courier_lin)
        LinearLayout selectCourierLin;
        @BindView(R.id.order_goods_item_num)
        TextView orderGoodsItemNum;
        @BindView(R.id.order_goods_item_price)
        TextView orderGoodsItemPrice;
        @BindView(R.id.order_goods_item_courier)
        TextView orderGoodsItemCourier;
        ViewHoder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}