package com.lmface.signin.myInitiateSign;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.courseinfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/2/28.
 */

public class CourseInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<courseinfo> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    Realm realm;
    private CompositeSubscription mcompositeSubscription;


    public courseinfo getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public List<courseinfo> getDatas() {
        return datas;
    }


    public void setDatas(List<courseinfo> datas) {
        this.datas = datas;
    }

    public CourseInfoListAdapter(Context mContext, List<courseinfo> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        realm = Realm.getDefaultInstance();
        mLayoutInflater = LayoutInflater.from(mContext);
        mcompositeSubscription = new CompositeSubscription();
    }

    public void update(List<courseinfo> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.my_initiate_sign_course_list_item;
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

                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.create().show();
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.courseName.setText(datas.get(p).getCoursename());
        mHolder.delectCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("是否删除此签到事件",p,true);
            }
        });
        mHolder.selectSignAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    mItemClickListener.statistical(p);
                }
            }
        });
        mHolder.selectSignEveryone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    mItemClickListener.showDrawerLayout(p);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void statistical(int position);

        public void showDrawerLayout(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.course_name)
        TextView courseName;
        @BindView(R.id.delect_course)
        TextView delectCourse;
        @BindView(R.id.select_sign_all)
        LinearLayout selectSignAll;
        @BindView(R.id.select_sign_everyone)
        LinearLayout selectSignEveryone;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
