package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewContactAdapter;
import com.sgwr.app.bean.IncidentContactInfo;
import com.sgwr.app.service.IncidentService;
import com.sgwr.app.utils.JsonUtils;
import com.sgwr.app.utils.Utilities;
import com.sgwr.app.utils.Utilities.Predicate;
import com.sgwr.app.widget.CustomDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class IncidentContactList extends Activity {

	// header
	private ImageButton inner_header_back;
	private TextView inner_header_title;
	private ProgressBar inner_header_progress;

	private RadioButton main_foot_radio_add;
	private RadioButton main_foot_radio_edit;
	private RadioButton main_foot_radio_delete;
	private ListView listview_contact;

	private ListViewContactAdapter adapter;
	private List<IncidentContactInfo> list;

	private long ActivityId;
	private long IncidentId;
	private String ContactTypeId;

	private final static int REQUEST_CODE = 2000;
	private int IncidentContactResultCode = 1000;
	private AppContext context;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_incident_contact);
		context = (AppContext) getApplication();
		// header
		inner_header_back = (ImageButton) findViewById(R.id.inner_header_back);
		inner_header_title = (TextView) findViewById(R.id.inner_header_title);
		inner_header_progress = (ProgressBar) findViewById(R.id.inner_header_progress);
		// footer
		main_foot_radio_add = (RadioButton) findViewById(R.id.main_foot_radio_add);
		main_foot_radio_edit = (RadioButton) findViewById(R.id.main_foot_radio_edit);
		main_foot_radio_delete = (RadioButton) findViewById(R.id.main_foot_radio_delete);

		listview_contact = (ListView) findViewById(R.id.listview_contact);

		inner_header_title.setText("Contact List");

		inner_header_back.setOnClickListener(new ClickEvent());
		main_foot_radio_add.setOnClickListener(new ClickEvent());
		main_foot_radio_edit.setOnClickListener(new ClickEvent());
		main_foot_radio_delete.setOnClickListener(new ClickEvent());

		Intent intent = getIntent();

		ActivityId = intent.getLongExtra("activityid", 0);
		IncidentId = intent.getLongExtra("incidentid", 0);

		ContactTypeId = intent.getStringExtra("id");
		String json = intent.getStringExtra("json");

		List<IncidentContactInfo> contacts = new ArrayList<IncidentContactInfo>();
		try
		{
			contacts = JsonUtils.DeserializeObject(json,
					new TypeReference<List<IncidentContactInfo>>() {
					});
		}
		catch (AppException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		list = Utilities.mapList(contacts,
				new Predicate<IncidentContactInfo>() {
					@Override
					public boolean Predicate(IncidentContactInfo obj)
					{
						if (String.valueOf(obj.ContactID).equals(ContactTypeId))
							return true;
						else
							return false;
					}
				});

		adapter = new ListViewContactAdapter(this, list,
				R.layout.item_incident_contact);
		listview_contact.setAdapter(adapter);
	}

	private void loadList()
	{
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					List<IncidentContactInfo> contacts = (List<IncidentContactInfo>) msg.obj;
					list.clear();
					list.addAll(contacts);
					adapter.notifyDataSetChanged();
				}
				else
				{
					((AppException) msg.obj)
							.makeToast(IncidentContactList.this);
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
					List<IncidentContactInfo> contacts = IncidentService
							.getIncidentContactList(context,
									String.valueOf(ActivityId));
					msg.what = 1;
					msg.obj = contacts;
				}
				catch (AppException e)
				{
					// TODO Auto-generated catch block
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
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE)
		{
			if (resultCode == IncidentContactResultCode)
			{
				loadList();
			}
		}
	}

	private void delete(final String json)
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
							loadList();
						}
						else
						{
							((AppException) msg.obj)
									.makeToast(IncidentContactList.this);
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
									.operateIncidentContact(context, "delete",
											json);
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
			main_foot_radio_add.setChecked(false);
			main_foot_radio_edit.setChecked(false);
			main_foot_radio_delete.setChecked(false);

			// TODO Auto-generated method stub
			if (v == main_foot_radio_add)
			{
				main_foot_radio_add.setChecked(true);

				Intent intent = new Intent(IncidentContactList.this,
						IncidentContact.class);

				intent.putExtra("type", com.sgwr.app.ActionType.Add);
				intent.putExtra("ResultCode", IncidentContactResultCode);
				intent.putExtra("ContactTypeId", ContactTypeId);
				intent.putExtra("ActivityId", ActivityId);
				intent.putExtra("IncidentId", IncidentId);

				startActivityForResult(intent, REQUEST_CODE);

			}
			else if (v == main_foot_radio_edit)
			{
				IncidentContactInfo info = null;
				for (IncidentContactInfo item : list)
				{
					if (item.IsSelected)
					{
						info = item;
						break;
					}
				}

				if (info == null)
				{
					Toast.makeText(context, "Please select a record to edit",
							Toast.LENGTH_SHORT).show();
					return;
				}

				main_foot_radio_edit.setChecked(true);

				Intent intent = new Intent(IncidentContactList.this,
						IncidentContact.class);
				intent.putExtra("type", com.sgwr.app.ActionType.Edit);
				intent.putExtra("IncidentContactInfo", info);
				intent.putExtra("ResultCode", IncidentContactResultCode);
				intent.putExtra("ActivityId", ActivityId);
				intent.putExtra("IncidentId", IncidentId);

				startActivityForResult(intent, REQUEST_CODE);
			}
			else if (v == main_foot_radio_delete)
			{
				StringBuilder sb = new StringBuilder();
				boolean flag = false;
				for (IncidentContactInfo item : list)
				{
					if (item.IsSelected)
					{
						sb.append(item.Id);
						sb.append(",");
						flag = true;
					}
				}
				if (sb.toString() != "")
				{
					sb.deleteCharAt(sb.length() - 1);
				}

				if (!flag)
				{
					Toast.makeText(context, "Please select a record to delete",
							Toast.LENGTH_SHORT).show();
					return;
				}

				main_foot_radio_delete.setChecked(true);
				delete(sb.toString());

			}
			else if (v == inner_header_back)
			{
				finish();
			}
		}

	}
}
