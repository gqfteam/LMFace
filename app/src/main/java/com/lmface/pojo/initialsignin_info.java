package com.lmface.pojo;

import java.sql.Timestamp;

public class initialsignin_info {

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

   

	public Integer getSigninfoid() {
        return signinfoid;
    }

    public void setSigninfoid(Integer signinfoid) {
        this.signinfoid = signinfoid;
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

	public Integer getSignintervaltime() {
        return signintervaltime;
    }

    public void setSignintervaltime(Integer signintervaltime) {
        this.signintervaltime = signintervaltime;
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

    @Override
    public String toString() {
        return "initialsignin_info{" +
                "signinfoid=" + signinfoid +
                ", signstarttime=" + signstarttime +
                ", signintervaltime=" + signintervaltime +
                ", signendtime=" + signendtime +
                ", signaddress='" + signaddress + '\'' +
                ", signgoal='" + signgoal + '\'' +
                ", signscope=" + signscope +
                ", signcourseid=" + signcourseid +
                ", temporaryordaily=" + temporaryordaily +
                ", addresslatitude='" + addresslatitude + '\'' +
                ", addresslongitude='" + addresslongitude + '\'' +
                '}';
    }
}