package com.lmface.User;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Created by johe on 2017/1/27.
 */

public class MyGoodsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<goods_msg> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;
    private boolean isEdi = false;

    private List<Integer> selectDelectId;

    public List<Integer> getSelectDelectId() {
        return selectDelectId;
    }

    public void setSelectDelectId(List<Integer> selectDelectId) {
        this.selectDelectId = selectDelectId;
    }

    public void isEdi(boolean isEdi) {
        this.isEdi = isEdi;
        this.notifyDataSetChanged();
        selectDelectId = new ArrayList<>();
    }

    public goods_msg getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public MyGoodsListAdapter(Context mContext, List<goods_msg> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        selectDelectId = new ArrayList<>();
    }

    public void update(List<goods_msg> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.my_goods_list_item, parent, false);
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
        if (datas.get(position).getGoodsimgaddress1() != null) {
            if (!datas.get(position).getGoodsimgaddress1().equals("")) {
                Picasso.with(mContext).load(datas.get(position).getGoodsimgaddress1())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(mHolder.goodsItemImg);
            }
        }

        mHolder.itemsName.setText(datas.get(position).getGoodsname());
        mHolder.itemsClassification.setText(datas.get(position).getGoodsclassification() + "/" + datas.get(position).getSpeciesname());

        mHolder.itemsMaster.setText("联系人" + datas.get(position).getUserphonenum());
        mHolder.itemsPhonenumber.setVisibility(View.GONE);
        mHolder.itemsPhonenumber.setText("联系电话:" + datas.get(position).getUserphonenum());
        mHolder.itemsPrice.setText(""+datas.get(position).getGoodsprice());
        if (isEdi) {
            mHolder.isDelectCheck.setVisibility(View.VISIBLE);
        } else {
            mHolder.isDelectCheck.setVisibility(View.GONE);
        }
        if (selectDelectId.size() == 0) {
            mHolder.isDelectCheck.setChecked(false);
        } else {
            for (int i = 0; i < selectDelectId.size(); i++) {
                if (selectDelectId.get(i) == datas.get(position).getGoodsid()) {
                    mHolder.isDelectCheck.setChecked(true);
                } else {
                    mHolder.isDelectCheck.setChecked(false);
                }
            }
        }
        mHolder.isDelectCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("gqf", "isChecked" + isChecked + datas.get(position).getGoodsid());
                if (isChecked) {
                    selectDelectId.add(datas.get(position).getGoodsid());
                } else {
                    for (int i = 0; i < selectDelectId.size(); i++) {
                        if (selectDelectId.get(i) == datas.get(position).getGoodsid()) {
                            selectDelectId.remove(i);
                        }
                    }
                }
            }
        });
        mHolder.goodsItemLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("gqf", "myGoodsListRel");
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(null, position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    static class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.goods_item_lin)
        LinearLayout goodsItemLin;
        @BindView(R.id.my_goods_list_rel)
        RelativeLayout myGoodsListRel;
        @BindView(R.id.goods_item_img)
        ImageView goodsItemImg;
        @BindView(R.id.items_name)
        TextView itemsName;
        @BindView(R.id.items_classification)
        TextView itemsClassification;
        @BindView(R.id.items_master)
        TextView itemsMaster;
        @BindView(R.id.items_phonenumber)
        TextView itemsPhonenumber;
        @BindView(R.id.is_delect_check)
        CheckBox isDelectCheck;
        @BindView(R.id.items_price)
        TextView itemsPrice;
        public ViewHoder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }
}


//        extends BaseAdapter {
//    private List<goods_msg> datas;
//    private Context mContext;
//    private LayoutInflater layoutInflater;
//    private ViewHolder mHolder;
//
//    private boolean isEdi = false;
//
//    private List<Integer> selectDelectId;
//
//    public List<Integer> getSelectDelectId() {
//        return selectDelectId;
//    }
//
//    public void setSelectDelectId(List<Integer> selectDelectId) {
//        this.selectDelectId = selectDelectId;
//    }
//
//    public void isEdi(boolean isEdi) {
//        this.isEdi = isEdi;
//        this.notifyDataSetChanged();
//        selectDelectId=new ArrayList<>();
//    }
//
//    public MyGoodsListAdapter(Context context, List<goods_msg> datas) {
//        this.mContext = context;
//        this.datas = datas;
//        this.layoutInflater = LayoutInflater.from(context);
//        selectDelectId=new ArrayList<>();
//    }
//
//    public void update(List<goods_msg> datas) {
//        this.datas = datas;
//        this.notifyDataSetChanged();
//    }
//
//    public int getCount() {
//        if (datas == null) {
//            return 0;
//        }
//        return datas.size();
//    }
//
//    public goods_msg getItem(int position) {
//        return datas.get(position);
//    }
//
//    public long getItemId(int position) {
//        return position;
//    }
//
//
//    public View getView(final int position, View arg1, ViewGroup arg2) {
//
//        if (arg1 == null) {
//
//            arg1 = layoutInflater.inflate(R.layout.my_goods_list_item,
//                    null);// inflate(context,
//            // R.layout.list_item,
//            // null);
//            mHolder = new ViewHolder(arg1);
//            arg1.setTag(mHolder);
//
//        } else {
//            mHolder = (ViewHolder) arg1.getTag();
//        }
//
//
//        Picasso.with(mContext).load(datas.get(position).getGoodsimgaddress1())
//                .placeholder(R.drawable.ic_launcher)
//                .error(R.drawable.ic_launcher)
//                .into(mHolder.goodsItemImg);
//
//
//        mHolder.itemsName.setText(datas.get(position).getGoodsname());
//        mHolder.itemsClassification.setText(datas.get(position).getGoodsclassification() + "/" + datas.get(position).getSpeciesname());
//
//        mHolder.itemsMaster.setText("联系人" + datas.get(position).getUserphonenum());
//        mHolder.itemsPhonenumber.setVisibility(View.GONE);
//        mHolder.itemsPhonenumber.setText("联系电话:" + datas.get(position).getUserphonenum());
//
//        if(isEdi){
//            mHolder.isDelectCheck.setVisibility(View.VISIBLE);
//            mHolder.isDelectCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Log.i("gqf","isChecked"+isChecked+datas.get(position).getGoodsid());
//                    if(isChecked){
//                        selectDelectId.add(datas.get(position).getGoodsid());
//                    }else{
//                        for(int i=0;i<selectDelectId.size();i++){
//                            if(selectDelectId.get(i)==datas.get(position).getGoodsid()){
//                                selectDelectId.remove(i);
//                            }
//                        }
//                    }
//                }
//            });
//        }else{
//            mHolder.isDelectCheck.setVisibility(View.GONE);
//        }
//        if(selectDelectId.size()==0){
//            mHolder.isDelectCheck.setChecked(false);
//        }else {
//            for (int i = 0; i < selectDelectId.size(); i++) {
//                if (selectDelectId.get(i) == datas.get(position).getGoodsid()) {
//                    mHolder.isDelectCheck.setChecked(true);
//                } else {
//                    mHolder.isDelectCheck.setChecked(false);
//                }
//            }
//        }
//
//
//
//        mHolder.button.setVisibility(View.GONE);
//        mHolder.button.setFocusable(false);
//        mHolder.button.setClickable(false);
//        return arg1;
//    }
//
//
//    static class ViewHolder {
//        @BindView(R.id.is_delect_check)
//        CheckBox isDelectCheck;
//        @BindView(R.id.goods_item_img)
//        ImageView goodsItemImg;
//        @BindView(R.id.items_name)
//        TextView itemsName;
//        @BindView(R.id.items_classification)
//        TextView itemsClassification;
//        @BindView(R.id.items_master)
//        TextView itemsMaster;
//        @BindView(R.id.items_phonenumber)
//        TextView itemsPhonenumber;
//        @BindView(R.id.button)
//        CheckBox button;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}
