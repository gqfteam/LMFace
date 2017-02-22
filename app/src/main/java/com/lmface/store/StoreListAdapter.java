package com.lmface.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.User.MyGoodsListAdapter;
import com.lmface.pojo.ChangeActivity;
import com.lmface.pojo.goods_msg;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by johe on 2016/9/19.
 * gqf
 * 义卖界面商品显示listadapter
 */
public class StoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<goods_msg> datas;
    private final LayoutInflater mLayoutInflater;
    private MyGoodsListAdapter.MyItemClickListener mItemClickListener;


    public goods_msg getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public StoreListAdapter(Context mContext, List<goods_msg> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<goods_msg> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.sale_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHoder(v);

        return viewHolder;
    }


    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyGoodsListAdapter.MyItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHoder mHolder = (ViewHoder) holder;

        String imgPath="";
        if(!datas.get(position).getGoodsimgaddress1().equals("")){
            imgPath=datas.get(position).getGoodsimgaddress1();
        }else if(!datas.get(position).getGoodsimgaddress2().equals("")){
            imgPath=datas.get(position).getGoodsimgaddress2();
        }else if(!datas.get(position).getGoodsimgaddress3().equals("")){
            imgPath=datas.get(position).getGoodsimgaddress3();
        }
        if(!imgPath.equals("")) {
            Picasso.with(mContext).load(imgPath)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(mHolder.goodsItemImg);
        }
        mHolder.itemsPrice.setText(""+datas.get(position).getGoodsprice());
        mHolder.itemsName.setText(datas.get(position).getGoodsname());
        mHolder.itemsClassification.setText(datas.get(position).getGoodsclassification() + "/" + datas.get(position).getSpeciesname());

        String goodsUserName="" ;
        if(datas.get(position).getNickname().equals("")){
            goodsUserName=datas.get(position).getNickname();
        }else{
            goodsUserName=datas.get(position).getUserName();
        }
        mHolder.itemsMaster.setText("联系人:" + goodsUserName);
        mHolder.itemsPhonenumber.setText("联系电话:" + datas.get(position).getUserphonenum());
        mHolder.GoodsListItemLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity changeActivity=new ChangeActivity();
                                    changeActivity.setActivity(GoodsDetailsActivity.class);
                                    changeActivity.setGoodsId(datas.get(position).getGoodsid());
                                    EventBus.getDefault().post(changeActivity);
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
        @BindView(R.id.goods_item_lin)
        LinearLayout GoodsListItemLin;
        @BindView(R.id.items_price)
        TextView itemsPrice;
        public ViewHoder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }
}
/*extends BaseAdapter {
    private List<goods_msg> datas;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ViewHolder mHolder;

    public StoreListAdapter(Context context, List<goods_msg> datas) {
        this.mContext = context;
        this.datas = datas;
        this.layoutInflater = LayoutInflater.from(context);

    }

    public void update(List<goods_msg> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public int getCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    public Object getItem(int arg0) {
        return datas.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }


    public View getView(int arg0, View arg1, ViewGroup arg2) {

        if (arg1 == null) {

            arg1 = layoutInflater.inflate(R.layout.sale_list_item,
                    null);// inflate(context,
            // R.layout.list_item,
            // null);
            mHolder = new ViewHolder(arg1);
            arg1.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) arg1.getTag();
        }


        Picasso.with(mContext).load(datas.get(arg0).getGoodsimgaddress1())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(mHolder.goodsItemImg);


        mHolder.itemsName.setText(datas.get(arg0).getGoodsname());
        mHolder.itemsClassification.setText(datas.get(arg0).getGoodsclassification() + "/" + datas.get(arg0).getSpeciesname());

        mHolder.itemsMaster.setText("联系人" + datas.get(arg0).getUserphonenum());
        mHolder.itemsPhonenumber.setText("联系电话:" + datas.get(arg0).getUserphonenum());


        return arg1;
    }

    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
*/