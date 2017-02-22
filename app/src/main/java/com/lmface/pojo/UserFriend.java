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


    private String nickname;

    private String sex;

    private String headimg;

    String phone;
    private int mesCount;

    public UserFriend(String userName) {
        this.userName=userName;
    }

    public void setMsg(String nikename,String headimg,String sex,String phone){
        this.setNickname(nikename);
        this.setHeadimg(headimg);
        this.setSex(sex);
        this.setPhone(phone);
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
