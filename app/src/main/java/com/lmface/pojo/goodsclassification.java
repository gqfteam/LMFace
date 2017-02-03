package com.lmface.pojo;

public class goodsclassification {
    private Integer classificationid;

    private String goodsclassification;

    private String classificationname;

    @Override
    public String toString() {
        return "goodsclassification{" +
                "classificationid=" + classificationid +
                ", goodsclassification='" + goodsclassification + '\'' +
                ", classificationname='" + classificationname + '\'' +
                '}';
    }

    public Integer getClassificationid() {
        return classificationid;
    }

    public void setClassificationid(Integer classificationid) {
        this.classificationid = classificationid;
    }

    public String getGoodsclassification() {
        return goodsclassification;
    }

    public void setGoodsclassification(String goodsclassification) {
        this.goodsclassification = goodsclassification == null ? null : goodsclassification.trim();
    }

    public String getClassificationname() {
        return classificationname;
    }

    public void setClassificationname(String classificationname) {
        this.classificationname = classificationname == null ? null : classificationname.trim();
    }
}