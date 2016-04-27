package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewClientAdapter;
import com.sgwr.app.bean.ClientInfo;
import com.sgwr.app.bean.UserGroupRightInfo;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.service.ClientService;
import com.sgwr.app.service.UserService;
import com.sgwr.app.utils.UIHelper;
import com.sgwr.app.utils.UserRightHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SelectClient extends BaseActivity {

	private ListViewClientAdapter<ClientInfo> adapter;
	private List<ClientInfo> clientList = new ArrayList<ClientInfo>();	
	private AppContext context;

	// head view
	private ImageView headLogo;
	private TextView headTitle;
	private ProgressBar headProgress;

	private ListView lvClients;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_select_client);
		context = (AppContext) getApplication();

		// head view
		headLogo = (ImageView) findViewById(R.id.head_logo);
		headTitle = (TextView) findViewById(R.id.head_title);
		headProgress = (ProgressBar) findViewById(R.id.head_progress);
		lvClients = (ListView) this.findViewById(R.id.select_client_listclient);
		
		headLogo.setImageResource(R.drawable.client_icon);		
		headTitle.setText("Select Client");
		
		adapter = new ListViewClientAdapter<ClientInfo>(
				context, clientList, R.layout.item_client_or_location);
		lvClients.setAdapter(adapter);

		lvClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				TextView tvClientName = (TextView) arg1
						.findViewById(R.id.select_client_clientname);
				Object obj = tvClientName.getTag();
				if (obj != null)
				{
					ClientInfo info = (ClientInfo) obj;
					context.CurrentClient = info;
				}

				List<UserGroupRightInfo> list = context.UserGroupRightList;
				boolean flag = UserRightHelper.HasSetClientAndLocation(list);
				Intent intent = new Intent();
				if (flag)
				{
					intent.setClass(SelectClient.this, SelectLocation.class);
				}
				else
				{
					intent.setClass(SelectClient.this, Main.class);
				}

				startActivity(intent);
				finish();
			}
		});
		
		if(clientList.isEmpty())
		{
			 loadClient();
		}
	}

	private void loadClient()
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
					List<ClientInfo> list = (List<ClientInfo>) msg.obj;
					clientList.clear();
					clientList.addAll(list);
					adapter.notifyDataSetChanged();
				}
				else
				{
					((AppException) msg.obj).makeToast(SelectClient.this);
				}
			}
		};

		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				AppContext context = (AppContext) getApplication();
				try
				{
					UserInfo info = context.CurrentUser;
					ArrayList<UserGroupRightInfo> list = UserService
							.getUserGroupRight(context, info.GroupId);

					context.UserGroupRightList = list;
					boolean flag = UserRightHelper
							.HasSetClientAndLocation(list);
					context.HasSetClientAndLocation = flag;

					// 0 表示不显示所有的Client，根据用户设置来，1表示显示所有的Client
					String strFlag = flag ? "0" : "1";

					List<ClientInfo> clients = ClientService.getClientList(
							context, info.ID + "", strFlag);
					msg.what = 1;
					msg.obj = clients;
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
