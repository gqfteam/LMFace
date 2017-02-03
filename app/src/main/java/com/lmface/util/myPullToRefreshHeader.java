package com.lmface.util;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lmface.R;
import com.lmface.util.in.srain.cube.views.ptr.PtrFrameLayout;
import com.lmface.util.in.srain.cube.views.ptr.PtrUIHandler;
import com.lmface.util.in.srain.cube.views.ptr.indicator.PtrIndicator;


public class myPullToRefreshHeader extends FrameLayout implements PtrUIHandler {
	Context mContext;

	public myPullToRefreshHeader(Context context) {
		super(context);
		mContext = context;
		initViews();
	}

	public myPullToRefreshHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initViews();
	}

	public myPullToRefreshHeader(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initViews();
	}

	ImageView animImg;
	Drawable d;
	View view;

	public void initViews() {
		view = LayoutInflater.from(getContext()).inflate(
				R.layout.pull_to_refresh_top, this);
		animImg = (ImageView) view.findViewById(R.id.pull_to_refresh_img);

		d = animImg.getDrawable();
		animImg.setImageDrawable(d);
		animImg.setBackground(null);
	}

	int i = 0;

	@Override
	public void onUIReset(PtrFrameLayout frame) {
		// TODO Auto-generated method stub
		Log.i("gqf", "myPullTpRefreshLayout:" + "onUIReset");
		// 结束刷新4
		i = 0;

		view.setScaleX(1);
		view.setScaleY(1);
		if (animationdrawableone != null) {
			animationdrawableone.stop();
			animationdrawableone = null;
		}
		if (animationdrawabletwo != null) {
			animationdrawabletwo.stop();
			animationdrawabletwo = null;
		}
		animImg.setImageDrawable(d);
		animImg.setBackground(null);

	}

	@Override
	public void onUIRefreshPrepare(PtrFrameLayout frame) {
		// TODO Auto-generated method stub
		Log.i("gqf", "myPullTpRefreshLayout:" + "onUIRefreshPrepare");
		// 准备刷新1
		// Log.i("gqf", "onUIRefreshPrepare"+frame.getMeasuredHeight());
		i++;
	}

	@Override
	public void onUIRefreshBegin(PtrFrameLayout frame) {
		// TODO Auto-generated method stub
		Log.i("gqf", "myPullTpRefreshLayout:" + "onUIRefreshBegin");
		// 开始刷新2
		i++;
		view.setScaleX(1);
		view.setScaleY(1);
		animImg.setBackgroundResource(R.drawable.pull_to_refresh_header_two_anim);
		animationdrawabletwo = (AnimationDrawable) animImg.getBackground();
		animationdrawabletwo.start();
	}

	boolean isRefreshEnd = false;

	@Override
	public void onUIRefreshComplete(PtrFrameLayout frame) {
		// TODO Auto-generated method stub
		Log.i("gqf", "myPullTpRefreshLayout:" + "onUIRefreshComplete");
		// 刷新中3
		i++;
	}

	AnimationDrawable animationdrawableone;
	AnimationDrawable animationdrawabletwo;

	boolean isRotate = false;



	@Override
	public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch,
			byte status, PtrIndicator ptrIndicator) {
		// TODO Auto-generated method stub

		// 整体刷新进度
		if (i == 1) {
			if (ptrIndicator.getCurrentPosY() < ptrIndicator.getHeaderHeight()) {
				// 小人头变大
				view.setScaleX((float) ptrIndicator.getCurrentPosY()
						/ ptrIndicator.getHeaderHeight()
						* ptrIndicator.getCurrentPosY()
						/ ptrIndicator.getHeaderHeight());
				view.setScaleY((float) ptrIndicator.getCurrentPosY()
						/ ptrIndicator.getHeaderHeight()
						* ptrIndicator.getCurrentPosY()
						/ ptrIndicator.getHeaderHeight());

				if (isRotate = true) {
					isRotate = false;
					if (animationdrawableone != null) {
						animationdrawableone.stop();
						animationdrawableone = null;
					}
					animImg.setImageDrawable(d);
					animImg.setBackground(null);
				}
			} else {
				// 小人翻转
				if (isRotate == false) {
					isRotate = true;
					view.setScaleX(1);
					view.setScaleY(1);
					animImg.setImageDrawable(null);
					animImg.setBackgroundResource(R.drawable.pull_to_refresh_header_one_anim);
					animationdrawableone = (AnimationDrawable) animImg
							.getBackground();
					animationdrawableone.start();
				}
			}
		}

	}
}
