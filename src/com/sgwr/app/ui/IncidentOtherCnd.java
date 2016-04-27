package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewConditionAdapter;
import com.sgwr.app.bean.ConditionTypeInfo;
import com.sgwr.app.bean.IncidentConditionInfo;
import com.sgwr.app.service.IncidentService;
import com.sgwr.app.widget.CustomDialog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class IncidentOtherCnd extends Activity {

	// header
	private ImageButton inner_header_back;
	private TextView inner_header_title;
	private ProgressBar inner_header_progress;

	// footer
	private RadioButton main_foot_radio_add;
	private RadioButton main_foot_radio_edit;
	private RadioButton main_foot_radio_delete;

	private ImageView main_foot_split1;
	private ImageView main_foot_split2;
	private ListView listview_othercnd;

	private AppContext context;

	private ArrayList<ConditionTypeInfo> cndList = null;
	private List<IncidentConditionInfo> incidentConditions = new ArrayList<IncidentConditionInfo>();
	private ListViewConditionAdapter cndAdpater;

	private long ActivityId = 0;
	private long IncidentId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_incident_condition);

		context = (AppContext) getApplication();

		Intent intent = getIntent();
		ActivityId = intent.getLongExtra("ActivityId", 0);
		IncidentId = intent.getLongExtra("IncidentId", 0);

		initView();
	}

	private void initView()
	{
		inner_header_back = (ImageButton) findViewById(R.id.inner_header_back);
		inner_header_title = (TextView) findViewById(R.id.inner_header_title);
		inner_header_progress = (ProgressBar) findViewById(R.id.inner_header_progress);

		main_foot_radio_add = (RadioButton) findViewById(R.id.main_foot_radio_add);
		main_foot_radio_edit = (RadioButton) findViewById(R.id.main_foot_radio_edit);
		main_foot_radio_delete = (RadioButton) findViewById(R.id.main_foot_radio_delete);

		main_foot_split1 = (ImageView) findViewById(R.id.main_foot_split1);
		main_foot_split2 = (ImageView) findViewById(R.id.main_foot_split2);

		listview_othercnd = (ListView) findViewById(R.id.listview_othercnd);

		main_foot_radio_edit.setVisibility(View.GONE);
		main_foot_split2.setVisibility(View.GONE);

		inner_header_back.setOnClickListener(new ClickEvent());
		main_foot_radio_add.setOnClickListener(new ClickEvent());
		main_foot_radio_delete.setOnClickListener(new ClickEvent());

		inner_header_title.setText("Address Book");
		cndAdpater = new ListViewConditionAdapter(this,
				R.layout.item_condition, incidentConditions);
		listview_othercnd.setAdapter(cndAdpater);

		loadConditons();
		if (incidentConditions.isEmpty())
		{
			loadIncidentConditions();
		}
	}

	private void loadConditons()
	{
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				try
				{
					cndList = IncidentService.getConditionTypes(context);
				}
				catch (AppException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void loadIncidentConditions()
	{
		inner_header_progress.setVisibility(View.VISIBLE);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					incidentConditions.clear();
					List<IncidentConditionInfo> list = (List<IncidentConditionInfo>) msg.obj;
					incidentConditions.addAll(list);
					cndAdpater.notifyDataSetChanged();
				}
				else
				{
					((AppException) msg.obj).makeToast(IncidentOtherCnd.this);
				}
				inner_header_progress.setVisibility(View.GONE);
			}
		};

		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				try
				{
					List<IncidentConditionInfo> list = IncidentService
							.getIncidentConditionList(context,
									String.valueOf(ActivityId));
					msg.what = 1;
					msg.obj = list;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		if (requestCode == 2)
		{
			if (resultCode == 2000)
			{
				loadIncidentConditions();
			}
		}
	}

	private void delete(final String strIds)
	{
		final CustomDialog dialog = new CustomDialog(this);
		dialog.setTitle("Message");
		dialog.setMessage("Are you sure that you want to delete record(s)?");
		dialog.setOnOkListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				dialog.dismiss();

				final Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg)
					{
						if (msg.what == 1)
						{
							loadIncidentConditions();
						}
						else
						{
							((AppException) msg.obj)
									.makeToast(IncidentOtherCnd.this);
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
							String strMsg = IncidentService
									.operateIncidentCondition(context,
											"delete", strIds);

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
		});

		dialog.setOnCloseListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.setOnCancelListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	class ClickEvent implements OnClickListener {
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			if (v instanceof ImageView)
			{
				if (v == inner_header_back)
				{
					finish();
				}
			}
			else if (v instanceof RadioButton)
			{
				main_foot_radio_add.setChecked(false);
				main_foot_radio_delete.setChecked(false);
				if (v == main_foot_radio_add)
				{
					main_foot_radio_add.setChecked(true);
					if (cndList == null)
					{
						Toast.makeText(IncidentOtherCnd.this,
								"Condition Type is null.", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					else
					{
						Intent intent = new Intent(IncidentOtherCnd.this,
								IncidentConditionDialog.class);
						intent.putExtra("type", "add");
						intent.putExtra("ActivityId", ActivityId);
						intent.putExtra("IncidentId", IncidentId);
						intent.putExtra("ConditonTypes", cndList);

						startActivityForResult(intent, 2);
					}
				}
				else if (v == main_foot_radio_delete)
				{
					boolean flag = false;
					StringBuilder sb = new StringBuilder();
					for (IncidentConditionInfo info : incidentConditions)
					{
						if (info.IsSelected)
						{
							sb.append(info.ID);
							sb.append(",");
							flag = true;
						}
					}

					if (sb.toString() != "")
					{
						sb.deleteCharAt(sb.length() - 1);
					}

					String strIds = sb.toString();
					if (!flag)
					{
						Toast.makeText(IncidentOtherCnd.this,
								"Please select a record to delete.",
								Toast.LENGTH_SHORT).show();
						return;
					}

					main_foot_radio_delete.setChecked(true);
					delete(strIds);
				}
			}
		}
	}

}
