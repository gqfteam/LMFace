package com.lmface.pojo;

import java.util.ArrayList;

/**
 * Created by johe on 2016/9/19.
 * gqf
 * 义卖顶部条件选择器条件模型
 */
public class SaleChooseModel {
    String name;
    ArrayList<String> have;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<String> getHave() {
        return have;
    }
    public void setHave(ArrayList<String> have) {
        this.have = have;
    }

    @Override
    public String toString() {
        return "SaleChooseModel{" +
                "name='" + name + '\'' +
                ", have=" + have +
                '}';
    }
}
