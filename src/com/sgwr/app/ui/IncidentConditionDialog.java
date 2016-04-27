package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.bean.ConditionTypeInfo;
import com.sgwr.app.bean.IncidentConditionInfo;
import com.sgwr.app.bean.SpinnerData;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.service.IncidentService;
import com.sgwr.app.utils.DateTimeUtils;
import com.sgwr.app.utils.JsonUtils;
import com.sgwr.app.utils.Utilities;
import com.sgwr.app.widget.LoadingDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class IncidentConditionDialog extends Activity {

	private TextView incidentcnd_title;
	private Spinner incidentcnd_spinner_cndtype;
	private EditText incidnetcnd_edit_desc;
	private Button incidentcnd_btn_cancel;
	private Button incident_btn_ok;
	private LoadingDialog loadDialog;

	private List<SpinnerData> spinner = new ArrayList<SpinnerData>();

	private AppContext context;
	private String ActionType = "";
	private long ActivityId = 0;
	private long IncidentId = 0;
	private UserInfo user;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_incident_condition);
		context = (AppContext) getApplication();
		user = context.CurrentUser;

		Intent intent = getIntent();
		ActionType = intent.getStringExtra("type");
		ActivityId = intent.getLongExtra("ActivityId", 0);
		IncidentId = intent.getLongExtra("IncidentId", 0);
		ArrayList<ConditionTypeInfo> list = (ArrayList<ConditionTypeInfo>) intent
				.getSerializableExtra("ConditonTypes");

		incidentcnd_title = (TextView) findViewById(R.id.incidentcnd_title);
		incidentcnd_spinner_cndtype = (Spinner) findViewById(R.id.incidentcnd_spinner_cndtype);
		incidnetcnd_edit_desc = (EditText) findViewById(R.id.incidnetcnd_edit_desc);
		incidentcnd_btn_cancel = (Button) findViewById(R.id.incidentcnd_btn_cancel);
		incident_btn_ok = (Button) findViewById(R.id.incident_btn_ok);

		spinner.clear();
		spinner.add(new SpinnerData(getString(R.string.please_select_one), ""));
		for (ConditionTypeInfo info : list)
		{
			spinner.add(new SpinnerData(info.CndType, info.Id));
		}

		ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinner);
		adapter.setDropDownViewResource(android.R.layout.select_dialog_item);

		incidentcnd_spinner_cndtype.setAdapter(adapter);

		incidentcnd_btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

		incident_btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Object obj = incidentcnd_spinner_cndtype.getSelectedItem();
				String strVal = Utilities
						.getSpinnerValue(incidentcnd_spinner_cndtype);
				String strText = Utilities
						.getSpinnerText(incidentcnd_spinner_cndtype);

				if (obj == null || strVal.equals(""))
				{
					Toast.makeText(IncidentConditionDialog.this,
							"Please select a condition type.",
							Toast.LENGTH_SHORT).show();
					return;
				}

				IncidentConditionInfo info = new IncidentConditionInfo();
				info.ActivityID = ActivityId;
				info.IncidentID = IncidentId;
				info.CndID = Utilities.Object2Int(strVal);
				info.CndType = strText;
				info.CreatedBy = user.UserName;
				info.CreatedTime = DateTimeUtils.getCurrentUtcDate();
				info.Description = Utilities.getEditText(incidnetcnd_edit_desc);

				String strJson = JsonUtils.SerializeObject(info);
				add(strJson);
			}
		});
	}

	public void add(final String strJson)
	{
		loadDialog = new LoadingDialog(this);
		loadDialog.show();

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				if (msg.what == 1)
				{
					loadDialog.dismiss();
					setResult(2000);

					finish();
				}
				else
				{
					((AppException) msg.obj)
							.makeToast(IncidentConditionDialog.this);
				}
			}
		};

		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				try
				{
					String strMsg = IncidentService.operateIncidentCondition(
							context, "add", strJson);
					msg.what = 1;
					msg.obj = strMsg;
				}
				catch (AppException e)
				{
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
}
