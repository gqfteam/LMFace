package com.lmface.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.lmface.application.LMFaceApplication;


public class CommonUtil {
	/**
	 * 在主线程执行任务
	 * @param r
	 */
	public static void runOnUIThread(Runnable r){
		LMFaceApplication.getMainThreadHandler().post(r);
	}
	
	/**
	 * 将子view从父view中移除
	 * @param childView
	 */
	public static void removeSelfFromParent(View childView){
		if(childView!=null){
			ViewParent parent = childView.getParent();
			if(parent instanceof ViewGroup){
				ViewGroup group = (ViewGroup) parent;
				group.removeView(childView);//移除子view
			}
		}
	}
	
	public static String getString(int resId){
		return LMFaceApplication.getContext().getResources().getString(resId);
	}
	/**
	 * 获取颜色资源
	 * @param resId
	 * @return
	 */
	public static int getColor(int resId){
		return LMFaceApplication.getContext().getResources().getColor(resId);
	}
	
	/**
	 * 获取dimens文件的值
	 * @param resId
	 * @return
	 */
	public static float getDimens(int resId){
		return LMFaceApplication.getContext().getResources().getDimension(resId);
	}
	
	/**
	 * 获取字符串数组资源
	 * @param resId
	 * @return
	 */
	public static String[] getStringArray(int resId){
		return LMFaceApplication.getContext().getResources().getStringArray(resId);
	}
	
	public static Drawable getDrawable(int resId){
		return LMFaceApplication.getContext().getResources().getDrawable(resId);
	}

}
