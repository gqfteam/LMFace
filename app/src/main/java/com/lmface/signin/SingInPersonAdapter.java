package com.lmface.signin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lmface.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by wjy on 2016/11/7.
 */

public class SingInPersonAdapter extends RecyclerView.Adapter<SingInPersonAdapter.ViewHolder> {

    List<String> datas;
    MyItemClickListener mItemClickListener;

    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public int getLayout() {
        return R.layout.signperson_list_item;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public SingInPersonAdapter(List<String> datas, Context mContext) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String data = datas.get(position);
//        holder.name.setText(data);

//        holder.RelativeLayoutItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mItemClickListener != null) {
//                    mItemClickListener.onItemClick(null, position);
//                }
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        //        Log.i("Daniel","---datas.size()---"+datas.size());
        return datas != null ? datas.size() : 0;
    }

    public void setdatas(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // View rootView;
//        @BindView(R.id.name)
//        TextView name;
        RelativeLayout RelativeLayoutItem;


        ViewHolder(View view) {
            super(view);
            //            rootView = view;
            ButterKnife.bind(this, view);
            //            this.mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //            if (mListener != null) {
            //                mListener.onItemClick(view, getPosition());
            //            }

        }
    }
}
