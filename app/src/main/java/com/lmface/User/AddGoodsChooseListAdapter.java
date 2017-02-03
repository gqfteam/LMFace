package com.lmface.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lmface.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by johe on 2016/9/21.
 */
public class AddGoodsChooseListAdapter extends BaseAdapter {
    private List<String> datas;//数据源
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ViewHolder mHolder;


    public void update(List<String> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public AddGoodsChooseListAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.datas = datas;
        this.layoutInflater = LayoutInflater.from(context);

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

            arg1 = layoutInflater.inflate(R.layout.item,
                    null);// inflate(context,
            // R.layout.list_item,
            // null);
            mHolder = new ViewHolder(arg1);
            arg1.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) arg1.getTag();
        }
        mHolder.txt.setText(datas.get(arg0));
        return arg1;
    }




    static class ViewHolder {
        @BindView(R.id.txt)
        TextView txt;
        @BindView(R.id.txt1)
        TextView txt1;
        @BindView(R.id.txt2)
        TextView txt2;
        @BindView(R.id.txt3)
        TextView txt3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
