package com.lmface.pojo;

public class courseinfo {
    private Integer courseid;

    private String coursename;

    private Integer userid;

    private Integer coursestatu;

    @Override
    public String toString() {
        return "courseinfo{" +
                "courseid=" + courseid +
                ", coursename='" + coursename + '\'' +
                ", userid=" + userid +
                ", coursestatu=" + coursestatu +
                '}';
    }

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename == null ? null : coursename.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCoursestatu() {
        return coursestatu;
    }

    public void setCoursestatu(Integer coursestatu) {
        this.coursestatu = coursestatu;
    }

}