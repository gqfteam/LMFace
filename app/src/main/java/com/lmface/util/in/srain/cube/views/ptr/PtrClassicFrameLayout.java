package com.lmface.util.in.srain.cube.views.ptr;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class PtrClassicFrameLayout extends PtrFrameLayout {

	private PtrClassicDefaultHeader mPtrClassicHeader;
	private boolean disallowInterceptTouchEvent = false;

	public PtrClassicFrameLayout(Context context) {
		super(context);
		initViews();
	}

	public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}

	public PtrClassicFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initViews();
	}

	private void initViews() {
		mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
		setHeaderView(mPtrClassicHeader);
		addPtrUIHandler(mPtrClassicHeader);
	}

	public PtrClassicDefaultHeader getHeader() {
		return mPtrClassicHeader;
	}

	/**
	 * Specify the last update time by this key string
	 * 
	 * @param key
	 */
	public void setLastUpdateTimeKey(String key) {
		if (mPtrClassicHeader != null) {
			mPtrClassicHeader.setLastUpdateTimeKey(key);
		}
	}

	/**
	 * Using an object to specify the last update time.
	 * 
	 * @param object
	 */
	public void setLastUpdateTimeRelateObject(Object object) {
		if (mPtrClassicHeader != null) {
			mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
		}
	}

	@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		disallowInterceptTouchEvent = disallowIntercept;
		super.requestDisallowInterceptTouchEvent(disallowIntercept);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		if (disallowInterceptTouchEvent) {
			if (e.getAction() == MotionEvent.ACTION_UP) {
				disallowInterceptTouchEvent = false;
			}
			return dispatchTouchEventSupper(e);
		}
		return super.dispatchTouchEvent(e);
	}
	
}
