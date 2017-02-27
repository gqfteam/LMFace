package com.lmface.pojo;

public class UserDailySignMsg {

	int initiatorId;//发起人id
	int courseId;//发起人的课程事件id
	String initiatorName;//没有真实姓名用昵称，没有昵称用账号
	String courseName;//发起人的课程事件名
	int needSignNum;//用户被发起签到的次数
	int signNum;//用户签到的次数
	public int getInitiatorId() {
		return initiatorId;
	}
	public void setInitiatorId(int initiatorId) {
		this.initiatorId = initiatorId;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getInitiatorName() {
		return initiatorName;
	}
	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getNeedSignNum() {
		return needSignNum;
	}
	public void setNeedSignNum(int needSignNum) {
		this.needSignNum = needSignNum;
	}
	public int getSignNum() {
		return signNum;
	}
	public void setSignNum(int signNum) {
		this.signNum = signNum;
	}
	
	
	
}
