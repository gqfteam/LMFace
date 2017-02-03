package com.lmface.util;

import com.lmface.pojo.SaleChooseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johe on 2016/9/19.
 * gqf
 * 用来解析Json为model
 */
public class ReadJson {
    private static class holder {
        private static ReadJson rj = new ReadJson();
    }

    private ReadJson() {
    }

    public static ReadJson getInstance() {
        return holder.rj;
    }

    /**
     * gqf
     * 解析义卖界面顶部条件JSon
     * @param json
     * @return
     */
    public ArrayList<SaleChooseModel> readSaleTopChooseJson(String json) {

        ArrayList<SaleChooseModel> scms = new ArrayList<SaleChooseModel>();
        SaleChooseModel scm = null;
        try {
            JSONArray datas = new JSONArray(json);

            int length = datas.length();
            for (int i = 0; i < length; i++) {
                JSONObject dataObj = datas.getJSONObject(i);
                scm=new SaleChooseModel();
                scm.setName(dataObj.getString("n"));

                JSONArray dataAry=dataObj.getJSONArray("c");
                int clenth=dataAry.length();
                ArrayList<String> have = new ArrayList<String>();
                for(int j=0;j<clenth;j++){
                    have.add((String)dataAry.get(j));
                }
                scm.setHave(have);
                scms.add(scm);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return scms;
    }

}
