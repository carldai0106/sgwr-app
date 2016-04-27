package com.sgwr.app;

import com.sgwr.app.ui.ConfigDialog;
import com.sgwr.app.ui.Login;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AppSplash extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		final View view = View.inflate(this, R.layout.splash, null);
		setContentView(view);

		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		// set duration
		animation.setDuration(2000);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				// TODO Auto-generated method stub
				redirectTo();
			}
		});
	}

	private void redirectTo()
	{
		final AppContext context = (AppContext) this.getApplication();

		if (context.getValue("HostName") != null)
		{
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
			finish();
		}
		else
		{
			Intent intent = new Intent(AppSplash.this, ConfigDialog.class);
			startActivityForResult(intent, 1000);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		if (requestCode == 1000)
		{
			if (resultCode == 2000)
			{
				Intent intent = new Intent(this, Login.class);
				startActivity(intent);
				finish();
			}
		}
	}
}
