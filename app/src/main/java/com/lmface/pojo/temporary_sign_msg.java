package com.lmface.pojo;

import java.sql.Timestamp;

public class temporary_sign_msg {
    private Integer usignid;

    private Integer userId;

    private Integer signstatu;

    private Integer initialsignininfoid;

    private String signlatitude;

    private String signlongitude;

    private Timestamp signtime;

    private Timestamp signstarttime;

    private Integer signintervaltime;

    private Timestamp signendtime;

    private String signaddress;

    private String signgoal;

    private Integer signscope;

    private Integer temporaryordaily;

    private String addresslatitude;

    private String addresslongitude;

    private Integer signcourseid;


    public Integer getUsignid() {
        return usignid;
    }

    public void setUsignid(Integer usignid) {
        this.usignid = usignid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSignstatu() {
        return signstatu;
    }

    public void setSignstatu(Integer signstatu) {
        this.signstatu = signstatu;
    }

    public Integer getInitialsignininfoid() {
        return initialsignininfoid;
    }

    public void setInitialsignininfoid(Integer initialsignininfoid) {
        this.initialsignininfoid = initialsignininfoid;
    }

    public String getSignlatitude() {
        return signlatitude;
    }

    public void setSignlatitude(String signlatitude) {
        this.signlatitude = signlatitude == null ? null : signlatitude.trim();
    }

    public String getSignlongitude() {
        return signlongitude;
    }

    public void setSignlongitude(String signlongitude) {
        this.signlongitude = signlongitude == null ? null : signlongitude.trim();
    }

   


    public Integer getSignintervaltime() {
        return signintervaltime;
    }

    public void setSignintervaltime(Integer signintervaltime) {
        this.signintervaltime = signintervaltime;
    }

 

    public Timestamp getSigntime() {
		return signtime;
	}

	public void setSigntime(Timestamp signtime) {
		this.signtime = signtime;
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

    public Integer getSigncourseid() {
        return signcourseid;
    }

    public void setSigncourseid(Integer signcourseid) {
        this.signcourseid = signcourseid;
    }

   
}