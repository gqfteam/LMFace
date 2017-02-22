package com.lmface.huanxin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lmface.R;
import com.lmface.dialog.SweetAlertDialog;
import com.lmface.view.TitleBarView;

public class AddFriendActivity extends Activity {
	private Context mContext;
	private Button btn_complete;
	private TitleBarView mTitleBarView;
	private EditText et_add_id;
	private EditText et_add_reason;
	private SweetAlertDialog sweetAlertDialog;
	private String add_id;
	private String add_reason;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_userinfo);
		mContext=this;
		findView();
		initTitleView();
		init();
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		btn_complete=(Button) findViewById(R.id.register_complete);
		et_add_id= (EditText) findViewById(R.id.et_add_id);
		et_add_reason= (EditText) findViewById(R.id.et_add_reason);
	}
	
	private void init(){
		btn_complete.setOnClickListener(completeOnClickListener);

		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.arg1==1) {//申请成功动画，
//					sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//					sweetAlertDialog.showCancelButton(false)
//							.showConfirmButton(false)
//							.setTitleText("提交成功！")
//							.show();
					Toast.makeText(getApplicationContext(), "申请成功", Toast.LENGTH_SHORT).show();
				}//else sweetAlertDialog.dismissWithAnimation();
				else Toast.makeText(getApplicationContext(), "申请失败", Toast.LENGTH_SHORT).show();
			}
		};
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.add_friend);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private OnClickListener completeOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {//点击添加好友之后
			add_id = et_add_id.getText().toString().trim();
			add_reason = et_add_reason.getText().toString().trim();
			if (TextUtils.isEmpty(add_id)){
				et_add_id.setError("ID不能为空！");
				return;
			}

			if (TextUtils.isEmpty(add_reason)){
				et_add_reason.setError("理由不能为空！");
				return;
			}
			//sweetAlertDialog= DialogUtils.getInstance().DialogLoading(mContext);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						//参数为要添加的好友的username和添加理由
						EMClient.getInstance().contactManager().addContact(add_id, add_reason);//需异步处理

						Message message= new Message();
						message.arg1=1;//注册成功动画，
						handler.sendMessage(message);
						Thread.sleep(2000);

						finish();

					} catch (HyphenateException e) {
						e.printStackTrace();
						Message message= new Message();
						message.arg1=2;//注册成功动画，
						handler.sendMessage(message);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	};
}
