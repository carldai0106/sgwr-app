package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.sgwr.app.ActionType;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewAttachAdapter;
import com.sgwr.app.bean.AttachmentInfo;
import com.sgwr.app.service.ActivityService;
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

public class AttachListActivity extends Activity {

	// header
	private ImageButton inner_header_back;
	private TextView inner_header_title;
	private ProgressBar inner_header_progress;

	private RadioButton main_foot_radio_add;
	private RadioButton main_foot_radio_edit;
	private RadioButton main_foot_radio_delete;
	private ListView listview_attachs;

	private ListViewAttachAdapter adapter;
	private List<AttachmentInfo> list = new ArrayList<AttachmentInfo>();
	private long ActivityId = 0;
	private AppContext context;
	private static int REQUEST_CODE = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity_attachs);

		context = (AppContext) getApplication();
		// header
		inner_header_back = (ImageButton) findViewById(R.id.inner_header_back);
		inner_header_title = (TextView) findViewById(R.id.inner_header_title);
		inner_header_progress = (ProgressBar) findViewById(R.id.inner_header_progress);
		// footer
		main_foot_radio_add = (RadioButton) findViewById(R.id.main_foot_radio_add);
		main_foot_radio_edit = (RadioButton) findViewById(R.id.main_foot_radio_edit);
		main_foot_radio_delete = (RadioButton) findViewById(R.id.main_foot_radio_delete);

		listview_attachs = (ListView) findViewById(R.id.listview_attachs);

		inner_header_title.setText("Attachement List");

		inner_header_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		main_foot_radio_add.setOnClickListener(new ClickEvent());
		main_foot_radio_edit.setOnClickListener(new ClickEvent());
		main_foot_radio_delete.setOnClickListener(new ClickEvent());

		Intent intent = getIntent();
		ActivityId = intent.getLongExtra("activityid", 0);

		adapter = new ListViewAttachAdapter(this, context, list,
				R.layout.item_attach);
		listview_attachs.setAdapter(adapter);

		if (list.isEmpty())
		{
			loadList();
		}
	}

	private void loadList()
	{
		inner_header_progress.setVisibility(View.VISIBLE);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				inner_header_progress.setVisibility(View.GONE);
				if (msg.what == 1)
				{
					List<AttachmentInfo> attachs = (List<AttachmentInfo>) msg.obj;
					list.clear();
					list.addAll(attachs);
					adapter.notifyDataSetChanged();
				}
				else
				{
					((AppException) msg.obj).makeToast(AttachListActivity.this);
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
					List<AttachmentInfo> attachs = ActivityService
							.getAttachments(context, String.valueOf(ActivityId));
					msg.what = 1;
					msg.obj = attachs;
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
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_CODE && resultCode == 3001)
		{
			loadList();
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
				final Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg)
					{
						// TODO Auto-generated method stub

						if (msg.what == 1)
						{
							loadList();
						}
						else
						{
							((AppException) msg.obj)
									.makeToast(AttachListActivity.this);
						}
					}
				};

				new Thread(new Runnable() {
					@Override
					public void run()
					{
						Message msg = new Message();
						try
						{
							String strMsg = ActivityService.deleteAttachs(
									context, strIds);
							msg.what = 1;
							msg.obj = strMsg;
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
				}).start();
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
			main_foot_radio_add.setChecked(false);
			main_foot_radio_edit.setChecked(false);
			main_foot_radio_delete.setChecked(false);
			if (v == main_foot_radio_add)
			{
				Intent intent = new Intent(AttachListActivity.this,
						CameraPhoto.class);
				intent.putExtra("ActivityId", ActivityId);
				intent.putExtra("type", ActionType.Add);
				startActivityForResult(intent, REQUEST_CODE);
			}
			else if (v == main_foot_radio_edit)
			{
				AttachmentInfo info = null;
				for (AttachmentInfo item : list)
				{
					if (item.IsSelected)
					{
						info = item;
						break;
					}
				}

				if (info == null)
				{
					Toast.makeText(AttachListActivity.this,
							"Please select a record to edit.",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!info.IsLoaded)
				{
					Toast.makeText(AttachListActivity.this,
							"Photo is loading, please wait a moment.",
							Toast.LENGTH_SHORT).show();
					return;
				}

				main_foot_radio_edit.setChecked(true);

				Intent intent = new Intent(AttachListActivity.this,
						CameraPhoto.class);

				intent.putExtra("AttachmentInfo", info);
				intent.putExtra("ActivityId", ActivityId);
				intent.putExtra("type", ActionType.Edit);

				startActivityForResult(intent, REQUEST_CODE);
			}
			else if (v == main_foot_radio_delete)
			{
				boolean flag = false;
				StringBuilder sb = new StringBuilder();
				for (AttachmentInfo info : list)
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
					Toast.makeText(AttachListActivity.this,
							"Please select one record to delete.",
							Toast.LENGTH_SHORT).show();
					return;
				}

				delete(strIds);
				main_foot_radio_delete.setChecked(true);
			}
		}
	}
}
