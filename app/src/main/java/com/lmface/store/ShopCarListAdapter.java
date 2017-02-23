package com.lmface.store;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.goods_msg_car;
import com.lmface.pojo.shop_car;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/1/30.
 */

public class ShopCarListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<goods_msg_car> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    private boolean isEdi = false;//批量编辑状态


    private boolean isSelectAll=false;//全选

    private goods_msg_car ediGoodsMsgCar;
    CompositeSubscription mcompositeSubscription;


    List<Integer> batchEdi;//选中编辑状态的position
    List<goods_msg_car> ediDatas;//更新中的数据


    List<Integer> batchDelects;//批量删除的物品position
    List<Integer> batchUpdate;//批量更新的物品position

    public void batchDelect() {
        if(batchDelects.size()!=0){
            List<Integer> carIds=new ArrayList<>();
            for(Integer i:batchDelects){
                carIds.add(datas.get(i).getCarId());
            }

            Subscription subscription = NetWork.getShopCarService().delectShopCarByListCarId(carIds)
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
                        public void onNext(ResultCode resultCode) {
                            if (resultCode.getCode() == 10000) {
                                //删除成功后删除数据
                                List<Integer> carIds=new ArrayList<>();
                                for(Integer i:batchDelects){
                                    carIds.add(datas.get(i).getCarId());
                                }
                                for(int i=0;i<datas.size();i++){
                                    for(int n=0;n<carIds.size();n++){
                                        if(datas.get(i).getCarId()==carIds.get(n)){
                                            datas.remove(i);
                                            i--;
                                        }
                                    }
                                }
                                Log.i("gqf","onNext");
                            }else{
                                Log.i("gqf",resultCode.getMsg());
                            }
                            //初始化编辑数据
                            initData();
                            //更新列表
                            update(datas);
                        }
                    });
            mcompositeSubscription.add(subscription);
        }
    }

    public void batchUpdate() {
        if(ediDatas.size()==datas.size()){
            List<shop_car> ShopCarMsgs=new ArrayList<>();
            int index=-1;
            for(goods_msg_car gmc:ediDatas){
                index++;
                if(gmc.getCarGoodsNum()!=datas.get(index).getCarGoodsNum()) {
                    shop_car sc=new shop_car();
                    sc.setCarId(gmc.getCarId());
                    sc.setCarGoodsNum(gmc.getCarGoodsNum());
                    ShopCarMsgs.add(sc);
                }
            }
            Gson g=new Gson();
            Subscription subscription = NetWork.getShopCarService().updateListShopCarNum(g.toJson(ShopCarMsgs))
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
                        public void onNext(ResultCode resultCode) {
                            if (resultCode.getCode() == 10000) {
                                for(int i=0;i<datas.size();i++){
                                    if(ediDatas.get(i).getCarGoodsNum()!= datas.get(i).getCarGoodsNum()){
                                        datas.get(i).setCarGoodsNum(ediDatas.get(i).getCarGoodsNum());
                                        ShopCarListAdapter.this.notifyItemChanged(i);
                                    }
                                }
                                Log.i("gqf","onNext");
                            }else{
                                Log.i("gqf",resultCode.getMsg());
                            }
                            //批量更新之后初始化
                            initData();
                        }
                    });
            mcompositeSubscription.add(subscription);
        }
    }
    public void initData() {
        batchDelects = new ArrayList<>();
        batchUpdate = new ArrayList<>();
        ediDatas = new ArrayList<>();
        batchEdi = new ArrayList<>();
        this.notifyDataSetChanged();
    }
    public void delect(final int position) {
        //单个删除,这个position是datas的
        //联网删除，之后再datas.remove
        Subscription subscription = NetWork.getShopCarService().delectShopCar(datas.get(position).getCarId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        if (resultCode.getCode() == 10000) {
                            datas.remove(position);
                            ShopCarListAdapter.this.notifyItemRemoved(position);
                        } else {

                        }
                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void updataLess(final goods_msg_car gmc){
        Subscription subscription = NetWork.getShopCarService().updateShopCarNum(gmc.getCarId(), gmc.getCarGoodsNum())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(ResultCode resultCode) {

                    }
                });
        mcompositeSubscription.add(subscription);
    }
    public void updata(final goods_msg_car gmc,final int position) {
        //单个更新，这个position是ediDatas的
        if (gmc.getCarGoodsNum() == datas.get(batchEdi.get(position)).getCarGoodsNum()) {
            Log.i("gqf","updata1");
            //数据没有变化
            Subscription subscription = NetWork.getShopCarService().updateShopCarNum(ediDatas.get(position).getCarId(), ediDatas.get(position).getCarGoodsNum())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                        @Override
                        public void onNext(ResultCode resultCode) {
                            if (resultCode.getCode() == 10000) {
                                Log.i("gqf","updata");
                            } else {
                                batchEdi.add(position);
                                ediDatas.add(gmc);
                                ShopCarListAdapter.this.notifyItemChanged(position);
                            }
                        }
                    });
            mcompositeSubscription.add(subscription);
        } else {
            //数据有变化
            Log.i("gqf","updata");
            //网络更新
            Subscription subscription = NetWork.getShopCarService().updateShopCarNum(ediDatas.get(position).getCarId(), ediDatas.get(position).getCarGoodsNum())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                        @Override
                        public void onNext(ResultCode resultCode) {
                            if (resultCode.getCode() == 10000) {
                                Log.i("gqf","updata");
                            } else {
                                batchEdi.add(position);
                                ediDatas.add(gmc);
                                ShopCarListAdapter.this.notifyItemChanged(position);
                            }
                        }
                    });
            mcompositeSubscription.add(subscription);
        }
    }
    public void updataStatu(boolean edi) {
        isEdi = edi;
        if(isEdi){
            int index=-1;
            batchEdi=new ArrayList<>();
            for(goods_msg_car gmc:datas){
                index++;
                batchEdi.add(index);
                Boolean isAdd=false;
                if(ediDatas.size()>index){
                    if(ediDatas.get(index).getCarId()!=gmc.getCarId()){
                        isAdd=true;
                    }
                }else{
                    isAdd=true;
                }
                if(isAdd) {
                    goods_msg_car g = new goods_msg_car();
                    g.setCarId(datas.get(index).getCarId());
                    g.setCarGoodsNum(datas.get(index).getCarGoodsNum());
                    g.setGoodsnum(datas.get(index).getGoodsnum());
                    ediDatas.add(index, g);
                }
            }
            for(goods_msg_car gmc:ediDatas){
                Log.i("gqf","updataStatu"+gmc.getGoodsnum());
            }
        }else{
            batchUpdate();
        }
        this.notifyDataSetChanged();
    }
    public goods_msg_car getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }
    public void Destory() {
        mcompositeSubscription.unsubscribe();
    }
    public ShopCarListAdapter(Context mContext, List<goods_msg_car> mDatas) {
        if(this.datas==null){
            batchUpdate = new ArrayList<>();
            batchDelects = new ArrayList<>();
            batchEdi = new ArrayList<>();
            ediDatas = new ArrayList<>();
        }
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);

        mcompositeSubscription = new CompositeSubscription();
    }

    public void update(List<goods_msg_car> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.shop_car_item, parent, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        //检测批量编辑状态
        if (isEdi == false) {
            mHolder.shopCarEdi.setText("编辑");
            mHolder.shopCarMsgRel.setVisibility(View.VISIBLE);
            mHolder.shopCarDelect.setVisibility(View.VISIBLE);
            mHolder.shopCarEdiRel.setVisibility(View.GONE);
            mHolder.topLin.setVisibility(View.VISIBLE);
            for (Integer i : batchEdi) {
                mHolder.shopCarMsgRel.setVisibility(View.VISIBLE);
                mHolder.shopCarDelect.setVisibility(View.GONE);
                mHolder.shopCarEdiRel.setVisibility(View.GONE);
                mHolder.shopCarEdi.setText("编辑");
                if (i == position) {
                    mHolder.shopCarEdi.setText("完成");
                    mHolder.shopCarMsgRel.setVisibility(View.GONE);
                    mHolder.shopCarEdiRel.setVisibility(View.VISIBLE);
                    mHolder.shopCarDelect.setVisibility(View.VISIBLE);
                    break;
                }
            }
        } else {
            mHolder.shopCarMsgRel.setVisibility(View.GONE);
            mHolder.shopCarEdiRel.setVisibility(View.VISIBLE);
            mHolder.shopCarDelect.setVisibility(View.GONE);
            mHolder.topLin.setVisibility(View.GONE);
        }
        mHolder.shopCarAtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    mItemClickListener.onItemClick(null,position);
                }
            }
        });

        //编辑按钮监听
        mHolder.shopCarEdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isedi = false;
                int ediPosition = -1;
                for (Integer i : batchEdi) {
                    //选中编辑
                    ediPosition++;
                    isedi = false;
                    if (i == position) {
                        //当前正在编辑，取消状态
                        isedi = true;
                        break;
                    }
                }
                if (isedi) {
                    //取消编辑
                    ((TextView)v).setText("编辑");
                    //从预编辑数据组中删除
                    updata(ediDatas.get(ediPosition ),ediPosition );
                    datas.get(batchEdi.get(ediPosition)).setCarGoodsNum(ediDatas.get(ediPosition ).getCarGoodsNum());
                    batchEdi.remove(ediPosition  );
                    ediDatas.remove(ediPosition  );
                } else {
                    //选中编辑
                    ((TextView)v).setText("完成");
                    batchEdi.add(position);
                    //加入据编辑数据组
                    goods_msg_car gmc=new goods_msg_car();
                    gmc.setCarId(datas.get(position).getCarId());
                    gmc.setCarGoodsNum(datas.get(position).getCarGoodsNum());
                    gmc.setGoodsnum(datas.get(position).getGoodsnum());
                    ediDatas.add(gmc);
                }
                ShopCarListAdapter.this.notifyItemChanged(position);
            }
        });

        //删除按钮监听
        mHolder.shopCarDelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delect(position);
            }
        });

        //增加按钮
        mHolder.shopCarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = -1;
                for (Integer i : batchEdi) {
                    p++;
                    if (i == position) {
                        break;
                    }
                }
                int s = addOrJian(1, p);
                if (s == 1) {
                    mHolder.shopCarAdd.setEnabled(true);
                    mHolder.shopCarJian.setEnabled(true);
                } else if (s == 2) {
                    mHolder.shopCarJian.setEnabled(false);
                    mHolder.shopCarAdd.setEnabled(true);
                } else {
                    mHolder.shopCarAdd.setEnabled(false);
                    mHolder.shopCarJian.setEnabled(true);
                }
                mHolder.shopCarBuyNum.setText(""+ediDatas.get(p).getCarGoodsNum());
                if(mItemClickListener!=null){
                    mItemClickListener.changePrice();
                }
            }
        });

        mHolder.shopCarGoodsNum.setText("X" + datas.get(position).getCarGoodsNum());
        mHolder.shopCarGoodsName.setText(datas.get(position).getGoodsname());
        mHolder.shopCarGoodsClassification.setText("分类：" + datas.get(position).getGoodsclassification() + "/" + datas.get(position).getSpeciesname());
        mHolder.shopCarGoodsPrice.setText("¥" + datas.get(position).getGoodsprice());

        //商品数量判断
        goods_msg_car gmc=null;
        int index=-1;
        if(gmc==null) {
            gmc = datas.get(position);
        }
        for(Integer i:batchEdi){
            index++;
            if(i==position){
                gmc=ediDatas.get(index);
                break;
            }
        }
        if(gmc.getGoodsnum()<=0){
            mHolder.shopCarGoodsClassification.setText("失效");
            mHolder.shopCarEdi.setEnabled(false);
            //加减按钮失效
            mHolder.shopCarAdd.setEnabled(false);
            mHolder.shopCarJian.setEnabled(false);

            mHolder.shopCarBuyNum.setText(0 + "");
        }else {
            mHolder.shopCarEdi.setEnabled(true);
            //加减按钮可用
            mHolder.shopCarAdd.setEnabled(true);
            mHolder.shopCarJian.setEnabled(true);
            if (gmc.getCarGoodsNum() > gmc.getGoodsnum()) {
                gmc.setCarGoodsNum(gmc.getGoodsnum());
                updataLess(gmc);

            } else {//加减按钮判断

                if (gmc.getCarGoodsNum() == gmc.getGoodsnum()) {
                    mHolder.shopCarAdd.setEnabled(false);
                    mHolder.shopCarJian.setEnabled(true);
                } else if (gmc.getCarGoodsNum() > 1 && gmc.getCarGoodsNum() < gmc.getGoodsnum() && gmc.getGoodsnum() > 1) {
                    mHolder.shopCarAdd.setEnabled(true);
                    mHolder.shopCarJian.setEnabled(true);
                } else if (gmc.getCarGoodsNum() == 1 && gmc.getGoodsnum() != 1) {
                    mHolder.shopCarAdd.setEnabled(true);
                    mHolder.shopCarJian.setEnabled(false);
                }
                if (gmc.getGoodsnum() == 1) {
                    mHolder.shopCarAdd.setEnabled(false);
                    mHolder.shopCarJian.setEnabled(false);
                }

            }
            mHolder.shopCarBuyNum.setText(gmc.getCarGoodsNum() + "");
        }




        //减少按钮
        mHolder.shopCarJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = -1;
                for (Integer i : batchEdi) {
                    p++;
                    if (i == position) {

                        break;
                    }
                }
                int s = addOrJian(2, p);
                if (s == 1) {
                    mHolder.shopCarAdd.setEnabled(true);
                    mHolder.shopCarJian.setEnabled(true);
                } else if (s == 2) {
                    mHolder.shopCarAdd.setEnabled(true);
                    mHolder.shopCarJian.setEnabled(false);
                } else {
                    mHolder.shopCarJian.setEnabled(true);
                    mHolder.shopCarAdd.setEnabled(false);
                }
                mHolder.shopCarBuyNum.setText(""+ediDatas.get(p).getCarGoodsNum());
                if(mItemClickListener!=null){
                    mItemClickListener.changePrice();
                }
            }
        });

        //数据绑定
        if (datas.get(position).getNickname() != null && !datas.get(position).getNickname().equals("")) {
            mHolder.shopCarStoreUserName.setText("物主：" + datas.get(position).getNickname());
        } else {
            mHolder.shopCarStoreUserName.setText("物主：" + datas.get(position).getUserName());
        }

        //选中按钮刷新
        boolean isCheck=false;
        for (Integer i : batchDelects) {
            if (i == position) {
                isCheck=true;
                break;
            }else{
                isCheck=false;
            }
        }
        mHolder.shopCarCheck.setChecked(isCheck);


        //图片加载
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
                    .into(mHolder.shopCarGoodsImg);
        }

        //选中checkbox监听
        mHolder.shopCarCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int index = -1;
                for (int i = 0; i < batchDelects.size(); i++) {
                    if (batchDelects.get(i) == position) {
                        index = i;
                        break;
                    }
                }
                if(isSelectAll==false) {
                    if (isChecked) {
                        if (batchDelects.size() < datas.size()) {
                            batchDelects.add(position);
                            if(batchDelects.size()==datas.size()){
                                if (mItemClickListener != null) {
                                    mItemClickListener.isSelectAll(true);
                                }
                            }else{
                                if (mItemClickListener != null) {
                                    mItemClickListener.isSelectAll(false);
                                }
                            }
                        }
                    }
                }
                if(!isChecked){
                    if (batchDelects.size() > 0) {
                        batchDelects.remove(index);
                        if (mItemClickListener != null) {
                            mItemClickListener.isSelectAll(false);

                        }
                    }
                }

                //通知activity更新底部价格数据
                if(mItemClickListener!=null){
                    mItemClickListener.changePrice();
                    mItemClickListener.selectNum(batchDelects.size());
                }

            }
        });

    }

    public Bundle getIntent(){
        Bundle bundle=new Bundle();
        ArrayList<Integer> goodsIds=new ArrayList<>();
        ArrayList<Integer> goodsNums=new ArrayList<>();
        for(int i=0;i<batchDelects.size();i++){

            goodsIds.add(datas.get(batchDelects.get(i)).getGoodsid());
            goodsNums.add(datas.get(batchDelects.get(i)).getCarGoodsNum());
        }


        bundle.putIntegerArrayList("goodsIds",goodsIds);
        bundle.putIntegerArrayList("goodsNums",goodsNums);

        return bundle;
    }
    public int addOrJian(int s, int position) {
        //edidata的position
        int buyNum = ediDatas.get(position).getCarGoodsNum();
        if (s == 1) {
            buyNum++;
        } else {
            buyNum--;
        }
        ediDatas.get(position).setCarGoodsNum(buyNum);

        if (buyNum > 1 && buyNum < ediDatas.get(position).getGoodsnum()) {
            return 1;
        }

        if (buyNum == 1) {
            return 2;
        }
        if (buyNum == ediDatas.get(position).getGoodsnum()) {
            return 3;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public double getSelectGoodsPrice() {
        double price = 0;
        for (Integer g : batchDelects) {
            price = price + datas.get(g).getGoodsprice()*datas.get(g).getCarGoodsNum();
        }
        return price;
    }

    public double getSelectGoodsCourier() {
        double price = 0;
        for (Integer g : batchDelects) {
            price = price + datas.get(g).getCourierMoney();
        }
        return price;
    }

    public void selectAll(boolean i) {
        if (i) {
            batchDelects = new ArrayList<>();
            int n=-1;
            for (goods_msg_car g : datas) {
                n++;
                batchDelects.add(n);
            }

        } else {
            if(batchDelects.size()==datas.size()) {
                batchDelects = new ArrayList<>();
            }
        }
        isSelectAll=i;
        this.notifyDataSetChanged();
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
        public void changePrice();
        public void isSelectAll(Boolean is);
        public void selectNum(int num);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shop_car_store_user_name)
        TextView shopCarStoreUserName;
        @BindView(R.id.shop_car_at_user)
        TextView shopCarAtUser;
        @BindView(R.id.shop_car_edi)
        TextView shopCarEdi;
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
        @BindView(R.id.shop_car_check)
        CheckBox shopCarCheck;
        @BindView(R.id.top_lin)
        LinearLayout topLin;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}