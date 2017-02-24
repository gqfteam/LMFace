package com.lmface.huanxin;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lmface.R;
import com.lmface.pojo.ChatMsgEntity;
import com.lmface.util.FaceConversionUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 
 ******************************************
 * @author 廖乃波
 * @文件名称	:  ChatMsgAdapter.java
 * @创建时间	: 2013-1-27 下午02:33:16
 * @文件描述	: 消息数据填充起
 ******************************************
 */
public class ChatMsgAdapter extends BaseAdapter {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private List<ChatMsgEntity> coll;
	private LayoutInflater mInflater;
	private Context context;

	public String userImg="";
	public String friendImg="";

	public void setHeadImg(String user,String friend){
		userImg=user;
		friendImg=friend;
	}

	public ChatMsgAdapter(Context context, List<ChatMsgEntity> coll) {
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		ChatMsgEntity entity = coll.get(position);

		if (entity.getMsgType()) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ChatMsgEntity entity = coll.get(position);
		boolean isComMsg = entity.getMsgType();

		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isComMsg) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.iv_userhead = (ImageView) convertView
					.findViewById(R.id.iv_userhead);

			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSendTime.setText(entity.getDate());
		SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, entity.getText());
		viewHolder.tvContent.setText(spannableString);

		if(isComMsg){
			if(!friendImg.equals("")){
				Picasso.with(context).load(friendImg)
						.placeholder(R.drawable.ic_launcher)
						.error(R.drawable.ic_launcher)
						.into(viewHolder.iv_userhead);
			}
		}else{
			if(!userImg.equals("")){
				Picasso.with(context).load(userImg)
						.placeholder(R.drawable.ic_launcher)
						.error(R.drawable.ic_launcher)
						.into(viewHolder.iv_userhead);
			}
		}


		return convertView;
	}

	public void addChat(ChatMsgEntity chatMsgEntity){
		coll.add(chatMsgEntity);
		this.notifyDataSetChanged();
	}

	class ViewHolder {
		public TextView tvSendTime;
		public TextView tvContent;
		public ImageView iv_userhead;
		public boolean isComMsg = true;
	}

}
