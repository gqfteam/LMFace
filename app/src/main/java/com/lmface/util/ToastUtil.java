package com.lmface.util;

import android.widget.Toast;

import com.lmface.application.LMFaceApplication;

public class ToastUtil {
	private static Toast toast;

	/**
	 * 强大的吐司，可以连续弹，不会等上一个消失
	 * 
	 * @param text
	 */
	public static void showToast(final String text) {
		if (UIUtils.isRunInMainThread()) {
			toast(text);
		} else {
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					// 保证在主线程弹吐司
					toast(text);
				}
			});
		}
	}

	public static void toast(String text) {
		if (toast == null) {
			toast = Toast.makeText(LMFaceApplication.getInstance(), text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}
}
