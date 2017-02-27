package com.lmface.pojo;

/**
 * Created by johe on 2017/2/27.
 */

public class SignUserMsg {

    user_msg user_msg;
    boolean isSign=false;

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.lmface.pojo.user_msg getUser_msg() {
        return user_msg;
    }

    public void setUser_msg(com.lmface.pojo.user_msg user_msg) {
        this.user_msg = user_msg;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }
}
