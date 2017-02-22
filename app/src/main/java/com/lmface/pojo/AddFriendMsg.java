package com.lmface.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by johe on 2017/2/19.
 */

public class AddFriendMsg extends RealmObject{
    @PrimaryKey
    int msgId;
    String name;
    String msg;

    String img;

    @Override
    public String toString() {
        return "AddFriendMsg{" +
                "msgId=" + msgId +
                ", name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
