package com.lmface.address;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.network.NetWork;
import com.lmface.pojo.ResultCode;
import com.lmface.pojo.user_address;
import com.lmface.pojo.user_msg;

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
 * Created by johe on 2017/2/3.
 */

public class AddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Realm realm;
    CompositeSubscription mcompositeSubscription;

    private Context mContext;
    private List<user_address> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    private int userDefultAddressId = 0;


    public int getUserDefultAddressId() {
        return userDefultAddressId;
    }

    public List<user_address> getDatas() {
        return datas;
    }

    public void setDatas(List<user_address> datas) {
        this.datas = datas;
    }

    public void setUserDefultAddressId(final  int AddressId) {
        Log.i("gqf","userDefultAddressId"+AddressId);
        Subscription subscription = NetWork.getUserService().updateUserDefaultAddress(realm.where(user_msg.class).findFirst().getUserId(),AddressId)
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
                        Log.i("gqf","userDefultAddressId"+resultCode.getCode());
                        if (resultCode.getCode() == 10000) {
                            userDefultAddressId = AddressId;
                            user_msg um=realm.where(user_msg.class).findFirst();
                            realm.beginTransaction();
                            um.setDefaultaddress(AddressId);
                            realm.copyToRealmOrUpdate(um);
                            realm.commitTransaction();
                            Log.i("gqf","userDefultAddressId"+userDefultAddressId);
                            Log.i("gqf","userDefultAddressId"+realm.where(user_msg.class).findFirst().getDefaultaddress());
                        }
                        AddressListAdapter.this.notifyDataSetChanged();
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    public user_address getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public AddressListAdapter(Context mContext, List<user_address> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        realm = Realm.getDefaultInstance();
        mcompositeSubscription = new CompositeSubscription();
        userDefultAddressId=realm.where(user_msg.class).findFirst().getDefaultaddress();
    }

    public void update(List<user_address> mDatas) {
        this.datas = mDatas;
        userDefultAddressId=realm.where(user_msg.class).findFirst().getDefaultaddress();
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.address_list_item, parent, false);
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
    user_address ua;
    public void delectAddress(int addressId, final int position) {
        if(datas.get(position).getAddressId()==userDefultAddressId){
            setUserDefultAddressId(0);
        }
        ua=datas.get(position);
        datas.remove(position);
        this.notifyItemRemoved(position);

        Subscription subscription = NetWork.getUserAddressService().delectUserAddress(addressId)
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

                        }else{
                            datas.add(position,ua);
                        }
                        ua=null;
                        update(datas);
                    }
                });
        mcompositeSubscription.add(subscription);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final int position=holder.getLayoutPosition();
        final user_address ua=datas.get(position);

        if (userDefultAddressId != 0) {
            if (ua.getAddressId() == userDefultAddressId) {
                mHolder.addressItemCheck.setChecked(true);
            }else{
                mHolder.addressItemCheck.setChecked(false);
            }
        }
        mHolder.addressItemName.setText(ua.getName());
        mHolder.addressItemPhone.setText(ua.getPhone());
        mHolder.addressItemMsg.setText(ua.getCountries() + ua.getProvinces() + ua.getCity() +
                ua.getCounty() + ua.getStreet() + ua.getAddressTxt());

        mHolder.addressItemEdiLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.changeActivity(ua.getAddressId());
                }
            }
        });
        mHolder.addressItemDelectLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("此收货地址是否删除")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                delectAddress(ua.getAddressId(),position);
                            }
                        })
                        .setNegativeButton("否",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alert.create().show();
            }
        });
        mHolder.addressMsgLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.changeActivity(ua.getAddressId());
                }
            }
        });

        mHolder.addressItemCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.addressItemCheck.isChecked()){
                    if(userDefultAddressId!=ua.getAddressId()){
                        //提示用户是否设置为默认地址
                        if(mItemClickListener!=null){
                            mItemClickListener.ediDefaultAddress(ua.getAddressId());
                        }
                    }
                }else{
                    if(userDefultAddressId==ua.getAddressId()){
                        if(mItemClickListener!=null){
                            mItemClickListener.ediDefaultAddress(0);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void changeActivity(int addressId);
        public void ediDefaultAddress(int addressId);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.address_msg_lin)
        LinearLayout addressMsgLin;
        @BindView(R.id.address_item_name)
        TextView addressItemName;
        @BindView(R.id.address_item_phone)
        TextView addressItemPhone;
        @BindView(R.id.address_item_msg)
        TextView addressItemMsg;
        @BindView(R.id.address_item_check)
        CheckBox addressItemCheck;
        @BindView(R.id.address_item_edi_lin)
        LinearLayout addressItemEdiLin;
        @BindView(R.id.address_item_delect_lin)
        LinearLayout addressItemDelectLin;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}