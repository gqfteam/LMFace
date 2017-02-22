
package com.lmface.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 
 ******************************************
 * @author 廖乃波
 * @文件名称	:  ChatMsgEntity.java
 * @创建时间	: 2013-1-27 下午02:33:33
 * @文件描述	: 消息实体
 ******************************************
 */
public class ChatMsgEntity extends RealmObject{

    @PrimaryKey
    int chatId;

    private String fromName;

    private String name;//好友name

    private String date;

    private String text;

    private boolean isComMeg = true;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "ChatMsgEntity{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", text='" + text + '\'' +
                ", isComMeg=" + isComMeg +
                '}';
    }

    public boolean isComMeg() {
        return isComMeg;
    }

    public void setComMeg(boolean comMeg) {
        isComMeg = comMeg;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
    	isComMeg = isComMsg;
    }

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.text = text;
        this.isComMeg = isComMsg;
    }

}
