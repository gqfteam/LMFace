package com.lmface.pojo;

public class goodsspecies {
    private Integer speciesid;

    private String speciesname;

    private Integer classificationid;

    public Integer getSpeciesid() {
        return speciesid;
    }

    public void setSpeciesid(Integer speciesid) {
        this.speciesid = speciesid;
    }

    public String getSpeciesname() {
        return speciesname;
    }

    public void setSpeciesname(String speciesname) {
        this.speciesname = speciesname == null ? null : speciesname.trim();
    }

    public Integer getClassificationid() {
        return classificationid;
    }

    public void setClassificationid(Integer classificationid) {
        this.classificationid = classificationid;
    }
}