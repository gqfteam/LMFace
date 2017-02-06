package com.lmface.address;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.user_address;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by johe on 2017/2/6.
 */

public class SelectAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<user_address> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;


    public user_address getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public SelectAddressAdapter(Context mContext, List<user_address> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public void update(List<user_address> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.select_address_list_item, parent, false);
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.addressItemName.setText(datas.get(p).getName());
        mHolder.addressItemPhone.setText(datas.get(p).getPhone());
        mHolder.addressItemMsg.setText(datas.get(p).getProvinces()+datas.get(p).getCity()+datas.get(p).getCounty()+datas.get(p).getStreet()+datas.get(p).getAddressTxt());
        mHolder.addressMsgLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    mItemClickListener.OnClickListener(p);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.address_item_name)
        TextView addressItemName;
        @BindView(R.id.address_item_phone)
        TextView addressItemPhone;
        @BindView(R.id.address_item_msg)
        TextView addressItemMsg;
        @BindView(R.id.address_msg_lin)
        LinearLayout addressMsgLin;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
