package com.sgwr.app.ui;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import com.sgwr.app.R;
import com.sgwr.app.utils.UIHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

public class IncidentReviewDialog extends Activity {

	private EditText edit_review_print1;
	private EditText edit_review_print2;

	private EditText edit_review_signature1;
	private EditText edit_review_signature2;

	private EditText edit_review_title1;
	private EditText edit_review_title2;

	private EditText edt_review_date1;
	private EditText edt_review_date2;

	private ImageButton imgbtn_review_delete1;
	private ImageButton imgbtn_review_delete2;

	private ImageButton dialog_title_btn_close;
	private Button dialog_btn_cancel;
	private Button dialog_btn_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_review);

		edit_review_print1 = (EditText) findViewById(R.id.edit_review_print1);
		edit_review_print2 = (EditText) findViewById(R.id.edit_review_print2);
		edit_review_signature1 = (EditText) findViewById(R.id.edit_review_signature1);
		edit_review_signature2 = (EditText) findViewById(R.id.edit_review_signature2);
		edit_review_title1 = (EditText) findViewById(R.id.edit_review_title1);
		edit_review_title2 = (EditText) findViewById(R.id.edit_review_title2);
		edt_review_date1 = (EditText) findViewById(R.id.edt_review_date1);
		edt_review_date2 = (EditText) findViewById(R.id.edt_review_date2);
		imgbtn_review_delete1 = (ImageButton) findViewById(R.id.imgbtn_review_delete1);
		imgbtn_review_delete2 = (ImageButton) findViewById(R.id.imgbtn_review_delete2);
		dialog_title_btn_close = (ImageButton) findViewById(R.id.dialog_title_btn_close);
		dialog_btn_cancel = (Button) findViewById(R.id.dialog_btn_cancel);
		dialog_btn_ok = (Button) findViewById(R.id.dialog_btn_ok);

		edt_review_date1.setOnClickListener(new ClickEvent());
		edt_review_date2.setOnClickListener(new ClickEvent());
		imgbtn_review_delete1.setOnClickListener(new ClickEvent());
		imgbtn_review_delete2.setOnClickListener(new ClickEvent());
		dialog_title_btn_close.setOnClickListener(new ClickEvent());
		dialog_btn_cancel.setOnClickListener(new ClickEvent());
		dialog_btn_ok.setOnClickListener(new ClickEvent());

		Intent intent = getIntent();
		Serializable obj = intent.getSerializableExtra("review");
		if (obj != null)
		{
			HashMap<String, String> maps = (HashMap<String, String>) obj;
			edit_review_print1.setText(maps.get("print1"));
			edit_review_print2.setText(maps.get("print2"));

			edit_review_signature1.setText(maps.get("signature1"));
			edit_review_signature2.setText(maps.get("signature2"));

			edit_review_title1.setText(maps.get("title1"));
			edit_review_title2.setText(maps.get("title2"));

			edt_review_date1.setText(maps.get("date1"));
			edt_review_date2.setText(maps.get("date2"));
		}
	}

	class ClickEvent implements OnClickListener {
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			if (v == dialog_btn_cancel || v == dialog_title_btn_close)
			{
				finish();
			}
			else if (v == edt_review_date1 || v == edt_review_date2)
			{
				final EditText edtView = (EditText) v;
				Calendar cal = Calendar.getInstance();
				new DatePickerDialog(
						IncidentReviewDialog.this,
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth)
							{
								String strDate = year
										+ "-"
										+ ((monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1))
												: (monthOfYear + 1))
										+ "-"
										+ (dayOfMonth < 10 ? ("0" + dayOfMonth)
												: dayOfMonth);
								edtView.setText(strDate);
							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH)).show();
			}
			else if (v == dialog_btn_ok)
			{
				Intent intent = new Intent();

				HashMap<String, String> maps = new HashMap<String, String>();
				maps.put("print1", UIHelper.getEditText(edit_review_print1));
				maps.put("print2", UIHelper.getEditText(edit_review_print2));
				maps.put("signature1",
						UIHelper.getEditText(edit_review_signature1));
				maps.put("signature2",
						UIHelper.getEditText(edit_review_signature2));
				maps.put("title1", UIHelper.getEditText(edit_review_title1));
				maps.put("title2", UIHelper.getEditText(edit_review_title2));
				maps.put("date1", UIHelper.getEditText(edt_review_date1));
				maps.put("date2", UIHelper.getEditText(edt_review_date2));

				intent.putExtra("review", maps);
				setResult(1005, intent);

				finish();
			}
			else if (v == imgbtn_review_delete1)
			{
				edt_review_date1.setText("");
			}
			else if (v == imgbtn_review_delete2)
			{
				edt_review_date2.setText("");
			}
		}
	}
}
