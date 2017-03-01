package com.lmface.pojo;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by johe on 2016/9/22.
 * gqf
 * 用户好友
 */
public class UserFriend {
    private List<EMMessage> messages;//聊天记录
    private String sortLetters;  //显示数据拼音的首字母
    private String userName;


    private int userId;
    private String realeName;

    private String nickname;

    private String sex;

    private String headimg;

    String phone;

    String isUseName="";

    boolean isSingIn=false;

    int needSignNum;//用户被发起签到的次数
    int signNum;//用户签到的次数

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

    public boolean isSingIn() {
        return isSingIn;
    }

    public void setSingIn(boolean singIn) {
        isSingIn = singIn;
    }

    public String getIsUseName() {
        return isUseName;
    }

    public void setIsUseName(String isUseName) {
        this.isUseName = isUseName;
    }

    private int mesCount;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealeName() {
        return realeName;
    }

    public void setRealeName(String realeName) {
        this.realeName = realeName;
    }

    public UserFriend(String userName) {
        this.userName=userName;
    }

    public void setMsg(int userId,String nikename,String headimg,String sex,String phone,String realeName){
        this.setNickname(nikename);
        this.setHeadimg(headimg);
        this.setSex(sex);
        this.setPhone(phone);
        this.setRealeName(realeName);
        this.setUserId(userId);
    }

    public int getMesCount() {
        return mesCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMesCount(int mesCount) {
        this.mesCount = mesCount;
    }


    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public List<EMMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<EMMessage> messages) {
        this.messages = messages;
    }
}
