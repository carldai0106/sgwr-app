package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewClientAdapter;
import com.sgwr.app.bean.LocationInfo;
import com.sgwr.app.service.LocationService;
import com.sgwr.app.utils.UIHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SelectLocation extends Activity {

	private ImageView headLogo;
	private TextView headTitle;
	private ProgressBar headProgress;

	private ListView listview_location;
	private List<LocationInfo> locations = new ArrayList<LocationInfo>();
	private AppContext context = null;
	private ListViewClientAdapter<LocationInfo> adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_select_location);

		context = (AppContext) getApplication();

		headLogo = (ImageView) findViewById(R.id.head_logo);
		headTitle = (TextView) findViewById(R.id.head_title);
		headProgress = (ProgressBar) findViewById(R.id.head_progress);
		listview_location = (ListView) this
				.findViewById(R.id.listview_location);

		headLogo.setImageResource(R.drawable.location_icon);
		headTitle.setText("Select Location");

		adapter = new ListViewClientAdapter<LocationInfo>(this, locations,
				R.layout.item_client_or_location);
		listview_location.setAdapter(adapter);

		listview_location.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				TextView tvClientName = (TextView) arg1
						.findViewById(R.id.select_client_clientname);
				Object obj = tvClientName.getTag();
				if (obj != null)
				{
					LocationInfo info = (LocationInfo) obj;
					context.CurrentLocation = info;
				}

				Intent intent = new Intent();
				intent.setClass(SelectLocation.this, Main.class);
				startActivity(intent);
				finish();
			}
		});

		if (locations.isEmpty())
		{
			loadLocation();
		}
	}

	private void loadLocation()
	{
		headProgress.setVisibility(View.VISIBLE);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				headProgress.setVisibility(View.GONE);
				if (msg.what == 1)
				{
					List<LocationInfo> list = (List<LocationInfo>) msg.obj;
					locations.clear();
					locations.addAll(list);

					adapter.notifyDataSetChanged();
				}
				else
				{
					((AppException) msg.obj).makeToast(SelectLocation.this);
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
					List<LocationInfo> list = LocationService
							.getUserLocationList(context);
					msg.obj = list;
					msg.what = 1;
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
	
	/**
	 * 监听返回--是否退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// 是否退出应用
			UIHelper.Exit(this);
		}
		else
		{
			flag = super.onKeyDown(keyCode, event);
		}
		return flag;
	}
}
