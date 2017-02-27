package com.lmface.pojo;

import java.sql.Timestamp;

public class sign_user_msg {
    private Integer signinfoid;

    private Timestamp signstarttime;

    private Integer signintervaltime;

    private Timestamp signendtime;

    private String signaddress;

    private String signgoal;

    private Integer signscope;

    private Integer signcourseid;

    private Integer temporaryordaily;

    private String addresslatitude;

    private String addresslongitude;

    private String coursename;

    private Integer userid;

    private String realname;

    private Integer age;

    private String nickname;

    private String sex;

    private String headimg;

    private Integer usertype;

    private String phone;

    private String collegeid;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSigninfoid() {
        return signinfoid;
    }

    public void setSigninfoid(Integer signinfoid) {
        this.signinfoid = signinfoid;
    }

    

    public Integer getSignintervaltime() {
        return signintervaltime;
    }

    public void setSignintervaltime(Integer signintervaltime) {
        this.signintervaltime = signintervaltime;
    }

   

    public Timestamp getSignstarttime() {
		return signstarttime;
	}

	public void setSignstarttime(Timestamp signstarttime) {
		this.signstarttime = signstarttime;
	}

	public Timestamp getSignendtime() {
		return signendtime;
	}

	public void setSignendtime(Timestamp signendtime) {
		this.signendtime = signendtime;
	}

	public String getSignaddress() {
        return signaddress;
    }

    public void setSignaddress(String signaddress) {
        this.signaddress = signaddress == null ? null : signaddress.trim();
    }

    public String getSigngoal() {
        return signgoal;
    }

    public void setSigngoal(String signgoal) {
        this.signgoal = signgoal == null ? null : signgoal.trim();
    }

    public Integer getSignscope() {
        return signscope;
    }

    public void setSignscope(Integer signscope) {
        this.signscope = signscope;
    }

    public Integer getSigncourseid() {
        return signcourseid;
    }

    public void setSigncourseid(Integer signcourseid) {
        this.signcourseid = signcourseid;
    }

    public Integer getTemporaryordaily() {
        return temporaryordaily;
    }

    public void setTemporaryordaily(Integer temporaryordaily) {
        this.temporaryordaily = temporaryordaily;
    }

    public String getAddresslatitude() {
        return addresslatitude;
    }

    public void setAddresslatitude(String addresslatitude) {
        this.addresslatitude = addresslatitude == null ? null : addresslatitude.trim();
    }

    public String getAddresslongitude() {
        return addresslongitude;
    }

    public void setAddresslongitude(String addresslongitude) {
        this.addresslongitude = addresslongitude == null ? null : addresslongitude.trim();
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(String collegeid) {
        this.collegeid = collegeid == null ? null : collegeid.trim();
    }
}