package com.lmface.huanxin;

import android.os.Environment;

import java.io.File;

public class Constant {

    // 用户信息 存储的sp name
    public static final String PREFENCES_NAME = "shandazushou";
    // 安装
    public static final String ISFIRST = "isfirst";
    //用户uid
    public static final String UID = "user_id";
    //用户pwd
    public static final String PASSWORD = "user_pwd";
    //用户token
    public static final String TOKEN = "token";
    //用户昵称
    public static final String NICKNAME = "nickname";
    //用户头像
    public static final String HEAD_ICON = "head_icon";


    // 存储用户基本信息的 sp name
    public static final String BASE_INFO_NAME = "baseinfo";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String LINK_ADDRESS = "link_address";
    public static final String SEX = "sex";

    public static final String SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "ImageCache"
            + File.separator
            + "qingchundao"
            + File.separator ;
    /**
     * 接收到消息的广播
     */
    public static String CHATNEWS="chat_news";

    public static String CHATNLIST="chat_list";

    public static final String NEWFRIENDID="NEWFRIENDID";
    public static final String NEWFRIENDREASON="NEWFRIENDREASON";

}