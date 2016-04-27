package com.sgwr.app.ui;

import com.sgwr.app.R;
import com.sgwr.app.widget.LoadingDialog;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity {
	
	private LoadingDialog loadDlg;
	private View loading_dialog_progress;
	private AnimationDrawable anim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.test);
		
		loadDlg = new LoadingDialog(this);
		
		loading_dialog_progress = findViewById(R.id.loading_dialog_progress);
		anim = (AnimationDrawable)loading_dialog_progress.getBackground();
		
		loading_dialog_progress.post(new Runnable() {
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				anim.start();
			}
		});
		
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				loadDlg.setLoadText("Loading");
				loadDlg.show();
				
				//switcher.showNext();
				/*CustomDialog dialog = new CustomDialog(TestActivity.this);
				dialog.setTitle("Message");
				dialog.setMessage("Are you sure that you want to delete record(s) ?");
				dialog.setOnCancelListener(new OnClickListener() {
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						Toast.makeText(TestActivity.this, "Cancel",
								Toast.LENGTH_SHORT).show();
					}

				});

				dialog.setOnCloseListener(new OnClickListener() {

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						Toast.makeText(TestActivity.this, "Close",
								Toast.LENGTH_SHORT).show();
					}
				});

				dialog.setOnOkListener(new OnClickListener() {

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						Toast.makeText(TestActivity.this, "Ok",
								Toast.LENGTH_SHORT).show();
					}
				});

				dialog.show();*/

			}
		});
	}
}
