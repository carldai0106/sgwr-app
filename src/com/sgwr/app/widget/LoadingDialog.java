package com.sgwr.app.widget;

import com.sgwr.app.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoadingDialog extends Dialog {

	private Context mContext;
	private LayoutInflater inflater;
	private LayoutParams lp;
	private TextView loadtext;
	private View loadingProgress;
	private ImageButton btnClose;
	private AnimationDrawable loadingAnimation;
	private Handler handler = new Handler();
	private String strMsg = "";

	public LoadingDialog(Context context) {
		super(context, R.style.MaskDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_loading);
		loadingProgress = findViewById(R.id.loading_dialog_progress);
		btnClose = (ImageButton) findViewById(R.id.loading_dialog_close);
		loadtext = (TextView) findViewById(R.id.loading_dialog_desc);
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				LoadingDialog.this.dismiss();
			}
		});
		if (!strMsg.equals(""))
		{
			loadtext.setText(strMsg);
		}
		loadingAnimation = (AnimationDrawable) loadingProgress.getBackground();
	}

	@Override
	public void show()
	{
		super.show();
		handler.postDelayed(new Runnable() {
			@Override
			public void run()
			{
				loadingAnimation.start();
			}
		}, 50);
	}

	@Override
	public void dismiss()
	{
		super.dismiss();
		loadingAnimation.stop();
	}

	public void setLoadText(String content)
	{
		strMsg = content;
	}
}