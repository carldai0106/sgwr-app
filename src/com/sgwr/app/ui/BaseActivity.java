package com.sgwr.app.ui;

import com.sgwr.app.AppManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class BaseActivity extends Activity {
	private boolean allowFullScreen = true;
	private boolean allowDestroy = true;
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		allowFullScreen = true;
		
		AppManager.getAppManager().addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		AppManager.getAppManager().finishActivity(this);
	}
	
	public boolean isAllowFullScreen(){
		return allowFullScreen;
	}
	
	public void setAllowFullScreen(boolean allowFullScreen) {
		this.allowFullScreen = allowFullScreen;
	}

	public void setAllowDestroy(boolean allowDestroy) {
		this.allowDestroy = allowDestroy;
	}

	public void setAllowDestroy(boolean allowDestroy, View view) {
		this.allowDestroy = allowDestroy;
		this.view = view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
			view.onKeyDown(keyCode, event);
			if (!allowDestroy) {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
