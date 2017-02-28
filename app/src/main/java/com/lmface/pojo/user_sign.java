package com.lmface.pojo;

import java.sql.Timestamp;

public class user_sign {
    private Integer usignid;

    private Integer userId;

    private Integer signStatu;

    private Integer initialsignininfoid;

    private String signlatitude;

    private String signlongitude;

    private Timestamp signTime;
    
    
    
    
    public Timestamp getSignTime() {
		return signTime;
	}

	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}

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

   
    public Integer getSignStatu() {
		return signStatu;
	}

	public void setSignStatu(Integer signStatu) {
		this.signStatu = signStatu;
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
}