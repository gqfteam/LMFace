package com.lmface.huanxin;

import com.lmface.pojo.UserFriend;
import com.lmface.util.CharacterParser;
import com.lmface.util.PinyinComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by johe on 2016/9/21.
 * gqf
 * 根据首字母排序
 */
public class DemoHelper {
    private static class holder {
        private static DemoHelper dh = new DemoHelper();
    }

    private DemoHelper(){}

    public static DemoHelper getInstance() {
        return holder.dh;
    }
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;


    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    public ArrayList<UserFriend> filledData(ArrayList<UserFriend> date){
        ArrayList<UserFriend> mSortList=new ArrayList<UserFriend>();
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();
        for(int i=0; i<date.size(); i++){
            UserFriend sortModel = date.get(i);
            sortModel.setUserName(date.get(i).getUserName());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel.getUserName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        // 根据a-z进行排序源数据
        Collections.sort(mSortList, pinyinComparator);//前为需要排列的数组，后为排列方式
        return mSortList;

    }
    public String getTimeLongToString(Long time){
        SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(time);
        String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00

        return sDateTime;
    }
}
