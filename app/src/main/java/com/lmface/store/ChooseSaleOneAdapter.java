package com.lmface.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.SaleChooseModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by johe on 2016/9/19.
 * gqf
 * Salefragment顶部条件选择器左边列表
 */
public class ChooseSaleOneAdapter extends BaseAdapter {

    private ArrayList<SaleChooseModel> datas;//数据源
    private Context mContext;
    private LayoutInflater layoutInflater;

    private ViewHolder mHolder;

    String province="河南省";
    String university="河南科技大学";
    String classification="电子产品";
    int chooseid;

    public int getChooseid() {
        return chooseid;
    }

    public void setChooseid(int chooseid) {
        this.chooseid = chooseid;
    }
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void update(ArrayList<SaleChooseModel> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public ChooseSaleOneAdapter(Context context, ArrayList<SaleChooseModel> datas) {
        this.mContext = context;
        this.datas = datas;
        this.layoutInflater = LayoutInflater.from(context);
        province= ChooseStoreSwoListFragment.mSearchConditions.getProvince();
        university=ChooseStoreSwoListFragment.mSearchConditions.getUniversity();
        classification=ChooseStoreSwoListFragment.mSearchConditions.getClassification();
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

            arg1 = layoutInflater.inflate(R.layout.choose_sale_one_list_item,
                    null);// inflate(context,
            // R.layout.list_item,
            // null);
            mHolder = new ChooseSaleOneAdapter.ViewHolder(arg1);
            arg1.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) arg1.getTag();
        }
        if((datas.get(arg0).getName().equals( province)&&chooseid==0)
                ||(datas.get(arg0).getName().equals( university)&&chooseid==1)
                ||(datas.get(arg0).getName().equals( classification)&&chooseid==2)){
            mHolder.saleChoseItemOneLin.setBackgroundResource(R.color.gray_light);
        }else{
            mHolder.saleChoseItemOneLin.setBackgroundResource(R.color.white);

        }
        mHolder.choseItemMsg.setText(datas.get(arg0).getName());



        return arg1;
    }

    static class ViewHolder {
        @BindView(R.id.chose_item_img)
        ImageView choseItemImg;
        @BindView(R.id.chose_item_msg)
        TextView choseItemMsg;
        @BindView(R.id.chose_item_ishave_img)
        ImageView choseItemIshaveImg;
        @BindView(R.id.sale_chose_item_one_lin)
        LinearLayout saleChoseItemOneLin;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
