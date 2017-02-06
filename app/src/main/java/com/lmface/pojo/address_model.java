package com.lmface.pojo;

import java.util.List;

/**
 * Created by johe on 2017/2/3.
 */

public class address_model {
    String p;
    List<SaleChooseModel> city;

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public List<SaleChooseModel> getCity() {
        return city;
    }

    public void setCity(List<SaleChooseModel> city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "address_model{" +
                "p='" + p + '\'' +
                ", city=" + city +
                '}';
    }
}
