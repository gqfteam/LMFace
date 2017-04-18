package com.lmface.pojo;

public class collegeinfo {
    private Integer collegeid;

    private String collegename;

    private String cityid;

    public Integer getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(Integer collegeid) {
        this.collegeid = collegeid;
    }

    public String getCollegename() {
        return collegename;
    }

    public void setCollegename(String collegename) {
        this.collegename = collegename == null ? null : collegename.trim();
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid == null ? null : cityid.trim();
    }

    @Override
    public String toString() {
        return "collegeinfo{" +
                "collegeid=" + collegeid +
                ", collegename='" + collegename + '\'' +
                ", cityid='" + cityid + '\'' +
                '}';
    }
}