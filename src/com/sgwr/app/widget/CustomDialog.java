package com.sgwr.app.widget;

import com.sgwr.app.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomDialog extends Dialog {
	private TextView dialog_title_text;
	private TextView dialog_text_msg;
	private ImageButton dialog_title_btn_close;
	private Button dialog_btn_ok;
	private Button dialog_btn_cancel;
	private String _title = "";
	private String _message = "";
	private View.OnClickListener closeListener = null;
	private View.OnClickListener cancelListener = null;
	private View.OnClickListener okListener = null;

	public CustomDialog(Context context) {
		super(context, R.style.MaskDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);
		
		dialog_title_text = (TextView) findViewById(R.id.dialog_title_text);
		dialog_text_msg = (TextView) findViewById(R.id.dialog_text_msg);
		dialog_title_btn_close = (ImageButton) findViewById(R.id.dialog_title_btn_close);
		dialog_btn_ok = (Button) findViewById(R.id.dialog_btn_ok);
		dialog_btn_cancel = (Button) findViewById(R.id.dialog_btn_cancel);

		dialog_title_text.setText(_title);
		dialog_text_msg.setText(_message);

		if (closeListener != null)
			dialog_title_btn_close.setOnClickListener(closeListener);
		if (cancelListener != null)
			dialog_btn_cancel.setOnClickListener(cancelListener);
		if (okListener != null)
			dialog_btn_ok.setOnClickListener(okListener);
	}

	public void setTitle(String title)
	{
		this._title = title;
	}

	public void setMessage(String message)
	{
		this._message = message;
	}

	public void setOnCloseListener(View.OnClickListener CloseListener)
	{
		closeListener = CloseListener;
	}

	public void setOnCancelListener(View.OnClickListener CancelListener)
	{
		cancelListener = CancelListener;
	}

	public void setOnOkListener(View.OnClickListener OkListener)
	{
		okListener = OkListener;
	}
}
