package com.sgwr.app.ui;

import java.io.Serializable;
import java.util.ArrayList;

import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewChkDtAdapter;
import com.sgwr.app.adapter.ListViewMultiChkAdapter;
import com.sgwr.app.bean.CheckDateItemInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CheckDateListDialog extends Activity {

	public static final int CHECKBOX_DATETIME_ITEM = 0x01;
	public static final int CHECKBOX_ITEM = 0x02;

	private ListView listview;
	private Button btnCancel;
	private Button btnOk;
	private TextView txtTitle;
	private ArrayList<CheckDateItemInfo> list;
	private int ResultCode = 0;

	public interface IActivityResult {
		void setIntentResult(Intent data);
	}

	private IActivityResult activityResult;

	public IActivityResult getActivityResult()
	{
		return activityResult;
	}

	public void setActivityResult(IActivityResult activityResult)
	{
		this.activityResult = activityResult;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_list_chk_date);

		listview = (ListView) findViewById(R.id.listview_chk_date);
		btnCancel = (Button) findViewById(R.id.list_chk_date_btn_cancel);
		btnOk = (Button) findViewById(R.id.list_chk_date_btn_ok);
		txtTitle = (TextView) findViewById(R.id.list_chk_date_text_title);

		Intent intent = getIntent();
		
		int type = intent.getIntExtra("chk_date_list_type", CHECKBOX_DATETIME_ITEM);
		ResultCode = intent.getIntExtra("chk_date_list_result_code", 1001);
		String title = intent.getStringExtra("chk_date_list_title");
		Serializable obj = intent.getSerializableExtra("chk_date_list_data");

		if (title != null)
		{
			txtTitle.setText(title);
		}

		list = (ArrayList<CheckDateItemInfo>) obj;
		if (list != null)
		{
			int resource = 0;
			if (type == CHECKBOX_DATETIME_ITEM)
			{
				ListViewChkDtAdapter adapter = new ListViewChkDtAdapter(this,
						list, R.layout.item_checkbox_datetime);
				listview.setAdapter(adapter);
			}
			else if (type == CHECKBOX_ITEM)
			{
				ListViewMultiChkAdapter adapter = new ListViewMultiChkAdapter(
						this, list, R.layout.item_checkbox);
				listview.setAdapter(adapter);
			}
		}

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				ArrayList<CheckDateItemInfo> results = new ArrayList<CheckDateItemInfo>();
				if (list != null)
				{
					for (CheckDateItemInfo info : list)
					{
						if (info.IsChecked)
						{
							results.add(info);
						}
					}
				}

				Intent intent = new Intent();
				intent.putExtra("chk_date_list_result", results);

				setResult(ResultCode, intent);

				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1)
		{
			if (resultCode == 2)
			{
				if (activityResult != null)
				{
					activityResult.setIntentResult(data);
				}
			}
		}
	}
}
