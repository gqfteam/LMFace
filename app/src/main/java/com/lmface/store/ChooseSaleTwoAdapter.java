package com.lmface.store;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.SaleChooseModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by johe on 2016/9/19.
 * gqf
 * Salefragment顶部选择器右边list
 */
public class ChooseSaleTwoAdapter extends BaseAdapter {


    private SaleChooseModel saleChooseModel;
    private ArrayList<String> datas;//数据源
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ViewHolder mHolder;
    private String mSaleChooseModelName;

    public String getmSaleChooseModelName() {
        return mSaleChooseModelName;
    }

    public void setmSaleChooseModelName(String mSaleChooseModelName) {

        this.mSaleChooseModelName = mSaleChooseModelName;
    }

    int chooseid;

    public int getChooseid() {
        return chooseid;
    }

    public void setChooseid(int chooseid) {
        this.chooseid = chooseid;
    }

    String city="洛阳市";

    String campus="西苑校区";

    String species="全部";


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void update(SaleChooseModel datas) {

        if(datas!=null) {
            saleChooseModel=datas;
            this.datas = datas.getHave();
        }else{
            this.datas=null;
        }

        this.notifyDataSetChanged();

    }

    public ChooseSaleTwoAdapter(Context context, SaleChooseModel datas) {
        this.mContext = context;
        saleChooseModel=datas;
        this.datas = saleChooseModel.getHave();
        this.layoutInflater = LayoutInflater.from(context);
        city= ChooseStoreSwoListFragment.mSearchConditions.getCity();
        campus=ChooseStoreSwoListFragment.mSearchConditions.getCampus();
        species=ChooseStoreSwoListFragment.mSearchConditions.getSpecies();
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
            arg1 = layoutInflater.inflate(R.layout.choose_sale_two_list_item,
                    null);// inflate(context,
            // R.layout.list_item,
            // null);
            mHolder = new ChooseSaleTwoAdapter.ViewHolder(arg1);
            arg1.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) arg1.getTag();
        }

        if(saleChooseModel.getName().equals(mSaleChooseModelName)) {
            if ((datas.get(arg0).equals(city) && chooseid == 0)
                    || (datas.get(arg0).equals(campus) && chooseid == 1)
                    || (datas.get(arg0).equals(species) && chooseid == 2)) {
                mHolder.choseTwoItemImg.setVisibility(View.VISIBLE);
                Log.i("gqf","gqf123"+saleChooseModel.getHave().get(arg0)+mSaleChooseModelName+datas.get(arg0));
            } else {
                mHolder.choseTwoItemImg.setVisibility(View.INVISIBLE);
            }
        }else{
            mHolder.choseTwoItemImg.setVisibility(View.INVISIBLE);
        }
        mHolder.choseTwoItemTxt.setText(datas.get(arg0)+"");

        return arg1;
    }

    static class ViewHolder {
        @BindView(R.id.chose_two_item_img)
        ImageView choseTwoItemImg;
        @BindView(R.id.chose_two_item_txt)
        TextView choseTwoItemTxt;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
