package com.lmface.huanxin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessageBody;
import com.lmface.R;
import com.lmface.pojo.UserFriend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by johe on 2016/9/22.
 */
public class MsgListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ViewHolder mHolder;
    private ArrayList<UserFriend> datas;
    private DemoHelper mDemoHelper;

    public void update(ArrayList<UserFriend> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public MsgListAdapter(Context context, ArrayList<UserFriend> datas) {
        this.mContext = context;
        this.datas = datas;
        this.layoutInflater = LayoutInflater.from(context);
        mDemoHelper = DemoHelper.getInstance();
    }


    public int getCount() {
        if (datas == null) {
            return 0;
        }
        Log.d("wjd", "datas.size():" + datas.size());
        return datas.size();
    }

    public Object getItem(int arg0) {
        return datas.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }


    public View getView(int arg0, View arg1, ViewGroup arg2) {

        if (arg1 == null) {
            arg1 = layoutInflater.inflate(R.layout.msg_list_item, null);// inflate(context,
            // R.layout.list_item,
            // null);
            mHolder = new ViewHolder(arg1);
            arg1.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) arg1.getTag();
        }

        boolean isNikName=false;
        if(datas.get(arg0).getNickname()!=null){
            if(!datas.get(arg0).getNickname().equals("")){
                isNikName=true;
            }
        }
        if(isNikName){
            mHolder.msgListItemName.setText(datas.get(arg0).getNickname());
        }else{
            mHolder.msgListItemName.setText(datas.get(arg0).getUserName());
        }

        if (datas.size() != 0) {
            EMMessageBody eb = datas.get(arg0).getMessages().get(datas.get(arg0).getMessages().size() - 1).getBody();
            mHolder.msgListItemMsg.setText(eb.toString().substring(5, eb.toString().length() - 1));
            String time = mDemoHelper.getTimeLongToString(datas.get(arg0).getMessages().get(datas.get(arg0).getMessages().size() - 1).getMsgTime());
            mHolder.msgListItemTime.setText(time);
        }
        if(datas.get(arg0).getHeadimg()!=null){
            if(!datas.get(arg0).getHeadimg().equals("")){

                Picasso.with(mContext).load(datas.get(arg0).getHeadimg())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(mHolder.msgListItemImg);
            }
        }
        return arg1;
    }


    static class ViewHolder {
        @BindView(R.id.msg_list_item_img)
        ImageView msgListItemImg;
        @BindView(R.id.msg_list_item_name)
        TextView msgListItemName;
        @BindView(R.id.msg_list_item_time)
        TextView msgListItemTime;
        @BindView(R.id.msg_list_item_msg)
        TextView msgListItemMsg;
        @BindView(R.id.msg_lItem_lLaout)
        LinearLayout msgLItemLLaout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
