package com.lmface.pojo;

/**
 * Created by johe on 2016/9/26.
 * gqf
 * 商品选择器参数
 */
public class SearchConditions {

    String province="河南省";
    String city="洛阳市";
    String university="河南科技大学";
    String campus="西苑校区";
    String classification="电子产品";
    String species="全部";

    public SearchConditions(){

    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCampus() {

            return campus;

    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getSpecies() {
            return species;

    }

    public void setSpecies(String species) {
        this.species = species;
    }
}
