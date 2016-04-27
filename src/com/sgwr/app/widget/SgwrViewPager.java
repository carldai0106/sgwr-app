package com.sgwr.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SgwrViewPager extends ViewPager {

	public SgwrViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SgwrViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	 @Override
	 public boolean onInterceptTouchEvent(MotionEvent arg0)
	 {
	 // TODO Auto-generated method stub
	 return false;
	 }
	
	 @Override
	 public boolean onTouchEvent(MotionEvent arg0)
	 {
	 // TODO Auto-generated method stub
	 return false;
	 }

}
