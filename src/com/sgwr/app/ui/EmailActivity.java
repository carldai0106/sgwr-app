package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewEmailAdapter;
import com.sgwr.app.bean.AddressBookInfo;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.service.CommonService;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EmailActivity extends Activity {

	private final static int REQUEST_CODE = 2000;

	// header
	private ImageButton inner_header_back;
	private TextView inner_header_title;
	private ProgressBar inner_header_progress;

	private RadioButton main_foot_radio_add, main_foot_radio_edit,
			main_foot_radio_delete;

	private ListView listview_emails;
	private List<AddressBookInfo> emailList = new ArrayList<AddressBookInfo>();
	private ListViewEmailAdapter emailsAdapter;

	private AppContext context;
	private String LocationId;
	private String ClientId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_email);

		context = (AppContext) getApplication();
		Intent intent = getIntent();
		LocationId = intent.getStringExtra("LocationId");
		ClientId = String.valueOf(context.CurrentClient.Id);

		initView();
	}

	private void initView()
	{
		// header
		inner_header_back = (ImageButton) findViewById(R.id.inner_header_back);
		inner_header_title = (TextView) findViewById(R.id.inner_header_title);
		inner_header_progress = (ProgressBar) findViewById(R.id.inner_header_progress);
		// footer
		main_foot_radio_add = (RadioButton) findViewById(R.id.main_foot_radio_add);
		main_foot_radio_edit = (RadioButton) findViewById(R.id.main_foot_radio_edit);
		main_foot_radio_delete = (RadioButton) findViewById(R.id.main_foot_radio_delete);

		listview_emails = (ListView) findViewById(R.id.listview_emails);

		inner_header_title.setText("Address Book");

		emailsAdapter = new ListViewEmailAdapter(EmailActivity.this,
				R.layout.item_email, emailList);
		listview_emails.setAdapter(emailsAdapter);

		inner_header_back.setOnClickListener(new ClickEvent());
		main_foot_radio_add.setOnClickListener(new ClickEvent());
		main_foot_radio_edit.setOnClickListener(new ClickEvent());
		main_foot_radio_delete.setOnClickListener(new ClickEvent());

		if (emailList.isEmpty())
		{
			loadEmails(true);
		}
	}

	private void loadEmails(final boolean isRefresh)
	{
		inner_header_progress.setVisibility(View.VISIBLE);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					emailList.clear();					
					List<AddressBookInfo> list = (List<AddressBookInfo>) msg.obj;
					emailList.addAll(list);
					emailsAdapter.notifyDataSetChanged();
				}
				else
				{
					((AppException) msg.obj).makeToast(EmailActivity.this);
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
					msg.what = 1;
					msg.obj = CommonService.getEmailList(context, isRefresh,
							ClientId, LocationId);
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
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE)
		{
			// 来自日期时间选择的回传值
			if (resultCode == 2)
			{
				loadEmails(true);
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
							loadEmails(true);
						}
						else
						{
							((AppException) msg.obj)
									.makeToast(EmailActivity.this);
						}
					}
				};

				new Thread() {
					@Override
					public void run()
					{
						Message msg = new Message();
						UserInfo info = context.CurrentUser;

						try
						{
							String strMsg = IncidentService.operateAddressBook(
									context, "delete", strIds);

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

	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View view)
		{
			// TODO Auto-generated method stub
			if (view instanceof RadioButton)
			{
				main_foot_radio_add.setChecked(false);
				main_foot_radio_edit.setChecked(false);
				main_foot_radio_delete.setChecked(false);

				if (view == main_foot_radio_add)
				{
					main_foot_radio_add.setChecked(true);

					Intent intent = new Intent(EmailActivity.this,
							AddressBook.class);

					intent.putExtra("type", "add");
					intent.putExtra("ClientId", ClientId);
					intent.putExtra("LocationId", LocationId);

					startActivityForResult(intent, REQUEST_CODE);
				}
				else if (view == main_foot_radio_edit)
				{
					AddressBookInfo info = null;
					for (AddressBookInfo item : emailList)
					{
						if (item.IsSelected)
						{
							info = item;
							break;
						}
					}

					if (info == null)
					{
						Toast.makeText(EmailActivity.this,
								"Please select one record to edit.",
								Toast.LENGTH_SHORT).show();
						return;
					}

					main_foot_radio_edit.setChecked(true);

					Intent intent = new Intent(EmailActivity.this,
							AddressBook.class);
					intent.putExtra("type", "edit");
					intent.putExtra("AddressBook", info);

					startActivityForResult(intent, REQUEST_CODE);

				}
				else if (view == main_foot_radio_delete)
				{
					boolean flag = false;
					StringBuilder sb = new StringBuilder();
					for (AddressBookInfo info : emailList)
					{
						if (info.IsSelected)
						{
							sb.append("'");
							sb.append(info.Id);
							sb.append("',");
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
						Toast.makeText(EmailActivity.this,
								"Please select one record to delete.",
								Toast.LENGTH_SHORT).show();
						return;
					}

					main_foot_radio_delete.setChecked(true);
					delete(strIds);
				}
			}
			else
			{
				if (view == inner_header_back)
				{
					finish();
				}
			}
		}
	}
}
