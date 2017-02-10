package com.lmface.pojo;

import java.sql.Timestamp;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class user_msg extends RealmObject{
    private Integer userId;

    private String userName;

    private String userPassword;
    @PrimaryKey
    private Integer uiId;

    private String realname;

    private Integer age;

    private String nickname;

    private String sex;

    private String headimg;

    private Integer usertype;

    private String studentnum;

    private Date registertime;

    private String classid;

    private String personalnote;

    private String phone;

    private Integer islogin;

    private String academicinfoid;

    private String collegeid;

    private Double usermoney;

    private Integer defaultaddress;
    
    public Integer getDefaultaddress() {
        return defaultaddress;
    }

    public void setDefaultaddress(Integer defaultaddress) {
        this.defaultaddress = defaultaddress;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public Integer getUiId() {
        return uiId;
    }

    public void setUiId(Integer uiId) {
        this.uiId = uiId;
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

    public String getStudentnum() {
        return studentnum;
    }

    public void setStudentnum(String studentnum) {
        this.studentnum = studentnum == null ? null : studentnum.trim();
    }

    

    public Date getRegistertime() {
		return registertime;
	}

	public void setRegistertime(Date registertime) {
		this.registertime = registertime;
	}

	public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid == null ? null : classid.trim();
    }

    public String getPersonalnote() {
        return personalnote;
    }

    public void setPersonalnote(String personalnote) {
        this.personalnote = personalnote == null ? null : personalnote.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIslogin() {
        return islogin;
    }

    public void setIslogin(Integer islogin) {
        this.islogin = islogin;
    }

    public String getAcademicinfoid() {
        return academicinfoid;
    }

    public void setAcademicinfoid(String academicinfoid) {
        this.academicinfoid = academicinfoid == null ? null : academicinfoid.trim();
    }

    public String getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(String collegeid) {
        this.collegeid = collegeid == null ? null : collegeid.trim();
    }

	public Double getUsermoney() {
		return usermoney;
	}

	public void setUsermoney(Double usermoney) {
		this.usermoney = usermoney;
	}

    @Override
    public String toString() {
        return "user_msg{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", uiId=" + uiId +
                ", realname='" + realname + '\'' +
                ", age=" + age +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", headimg='" + headimg + '\'' +
                ", usertype=" + usertype +
                ", studentnum='" + studentnum + '\'' +
                ", registertime=" + registertime +
                ", classid='" + classid + '\'' +
                ", personalnote='" + personalnote + '\'' +
                ", phone='" + phone + '\'' +
                ", islogin=" + islogin +
                ", academicinfoid='" + academicinfoid + '\'' +
                ", collegeid='" + collegeid + '\'' +
                ", usermoney=" + usermoney +
                ", defaultaddress=" + defaultaddress +
                '}';
    }

    public user_msg(){}
	public user_msg(Integer userId, String userName, String userPassword, Integer uiId, String realname, Integer age,
			String nickname, String sex, String headimg, Integer usertype, String studentnum, Timestamp registertime,
			String classid, String personalnote, String phone, Integer islogin, String academicinfoid, String collegeid,
                    Double usermoney) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.uiId = uiId;
		this.realname = realname;
		this.age = age;
		this.nickname = nickname;
		this.sex = sex;
		this.headimg = headimg;
		this.usertype = usertype;
		this.studentnum = studentnum;
		this.registertime = registertime;
		this.classid = classid;
		this.personalnote = personalnote;
		this.phone = phone;
		this.islogin = islogin;
		this.academicinfoid = academicinfoid;
		this.collegeid = collegeid;
		this.usermoney = usermoney;
	}

    
}