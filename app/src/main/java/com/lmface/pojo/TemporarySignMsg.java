package com.lmface.pojo;

import java.util.ArrayList;
import java.util.List;

public class TemporarySignMsg {

	int signId;
	List<Integer> needSignUserIds;
	List<Integer> signUserIds;

	@Override
	public String toString() {
		return "TemporarySignMsg{" +
				"signId=" + signId +
				", needSignUserIds=" + needSignUserIds +
				", signUserIds=" + signUserIds +
				'}';
	}

	public TemporarySignMsg(){
		needSignUserIds=new ArrayList<>();
		signUserIds=new ArrayList<>();
	}

	public int getSignId() {
		return signId;
	}

	public void setSignId(int signId) {
		this.signId = signId;
	}

	public List<Integer> getNeedSignUserIds() {
		return needSignUserIds;
	}

	public void setNeedSignUserIds(List<Integer> needSignUserIds) {
		this.needSignUserIds = needSignUserIds;
	}

	public List<Integer> getSignUserIds() {
		return signUserIds;
	}

	public void setSignUserIds(List<Integer> signUserIds) {
		this.signUserIds = signUserIds;
	}
	
	public void addNeedSignUserIds(int userId){
		needSignUserIds.add(userId);
	}
	public void addSignUserIds(int userId){
		signUserIds.add(userId);
	}
}
