package com.sgwr.app.ui;

import com.sgwr.app.AppContext;
import com.sgwr.app.R;
import com.sgwr.app.utils.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ConfigDialog extends Activity {

	private EditText config_edittext;
	private Button btnOk;
	private ImageButton btnClose;

	private AppContext context;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_config);
		context = (AppContext) getApplication();

		config_edittext = (EditText) findViewById(R.id.config_edittext);
		config_edittext.setText(context.getValue("HostName"));
		btnClose = (ImageButton) findViewById(R.id.loading_dialog_close);
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

		btnOk = (Button) findViewById(R.id.dialog_btn_ok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				String strURL = config_edittext.getText().toString();
				if (StringUtils.isEmpty(strURL.trim()))
				{
					config_edittext.setError("Please enter your website url");
					return;
				}
				else
				{
					context.setValue("HostName", strURL);
					setResult(2000);
					finish();
				}
			}
		});

	}
}
