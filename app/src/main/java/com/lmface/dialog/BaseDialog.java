package com.lmface.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.lmface.R;
import com.lmface.util.BaseViewUtils;


/**
 * 底部弹出dialog
 * @author longx
 *
 */
public class BaseDialog extends Dialog {
	private Activity context;

	private static BaseDialog baseDialog;

	private static int windowWith;
	private static int windowHight;


	public BaseDialog(Activity context) {
		this(context, Gravity.BOTTOM);
	}


	public BaseDialog(Activity context,int gravity) {
		super(context, R.style.bottom_dialog);
		this.context = context;
		Window window = this.getWindow();
		window.setGravity(gravity);
		window.setWindowAnimations(R.style.PopMenuAnimation);
		this.setCanceledOnTouchOutside(true);
	}
	

	@Override
	public void setContentView(View view) {
		
		LayoutParams params = getWindow().getAttributes();
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = BaseViewUtils.getWindowsWidth(context);
		getWindow().setAttributes(params);

		super.setContentView(view, params);
	}
	
	

	
	
	private void setFullContentView(View view) {
		
		LayoutParams params = getWindow().getAttributes();
		params.height =BaseViewUtils.getWindowsWidth(context);
		params.width =BaseViewUtils.getWindowsHeight(context);
		getWindow().setAttributes(params);

		super.setContentView(view, params);
	}

	
	public void show(View contentView) {
		
		show();
		setContentView(contentView);
	}
	
	public void showFullScreen(View contentView) {
		
		show();
		setFullContentView(contentView);
	}
}
