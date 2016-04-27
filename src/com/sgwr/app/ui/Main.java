package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sgwr.app.AppConfig;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewActivityAdapter;
import com.sgwr.app.adapter.SlideMenuAdapter;
import com.sgwr.app.bean.ActivityInfo;
import com.sgwr.app.bean.SlideMenuInfo;
import com.sgwr.app.service.ActivityService;
import com.sgwr.app.utils.StringUtils;
import com.sgwr.app.utils.UIHelper;
import com.sgwr.app.utils.UserRightHelper;
import com.sgwr.app.widget.CustomDialog;
import com.sgwr.app.widget.PullToRefreshListView;
import com.sgwr.app.widget.SlidingMenuLayout;
import com.sgwr.app.widget.SlidingMenuLayout.DisabledType;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends BaseActivity {

	private AppContext context;

	// head view
	private ImageButton head_menu;
	private TextView head_title;
	private ProgressBar head_progress;

	// slide menu view
	private SlidingMenuLayout sliding_menu_layout;
	private LinearLayout main_content_layout;
	private ListView slide_listview;
	private View Last_Selected_View;

	// buttons opne closed all
	private Button btn_activity_open;
	private Button btn_activity_closed;
	private Button btn_activity_all;
	private String activity_status;

	// footer view
	private RadioButton main_foot_radio_add;
	private RadioButton main_foot_radio_edit;
	private RadioButton main_foot_radio_delete;
	private ImageView main_foot_split1;
	private ImageView main_foot_split2;

	private View lvActivityFooter;
	private TextView lvActivity_footer_more;
	private ProgressBar lvActivity_footer_progress;
	private PullToRefreshListView lvActivity;
	private ListViewActivityAdapter lvActivityAdapter;

	private Handler lvIncidentHandler;
	private int lvActivitySumData;
	private List<ActivityInfo> lvActivityData = new ArrayList<ActivityInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_main);

		context = (AppContext) getApplication();

		initView();
		initIncidentListView();
		initSlidingMenu();
	}

	private void initView()
	{
		// header
		head_menu = (ImageButton) this.findViewById(R.id.inner_header_back);
		head_title = (TextView) this.findViewById(R.id.inner_header_title);
		head_progress = (ProgressBar) this
				.findViewById(R.id.inner_header_progress);
		head_title.setText(getString(R.string.incident));
		head_menu.setBackgroundResource(R.drawable.menu);

		btn_activity_open = (Button) this.findViewById(R.id.btn_activity_open);
		btn_activity_closed = (Button) this
				.findViewById(R.id.btn_activity_closed);
		btn_activity_all = (Button) this.findViewById(R.id.btn_activity_all);

		btn_activity_open.setOnClickListener(ActivityStatusBtnClick(
				btn_activity_open, "open"));
		btn_activity_closed.setOnClickListener(ActivityStatusBtnClick(
				btn_activity_closed, "closed"));
		btn_activity_all.setOnClickListener(ActivityStatusBtnClick(
				btn_activity_all, "all"));
		btn_activity_open.setEnabled(false);

		// footer
		main_foot_radio_add = (RadioButton) findViewById(R.id.main_foot_radio_add);
		main_foot_radio_edit = (RadioButton) findViewById(R.id.main_foot_radio_edit);
		main_foot_radio_delete = (RadioButton) findViewById(R.id.main_foot_radio_delete);
		main_foot_split1 = (ImageView) findViewById(R.id.main_foot_split1);
		main_foot_split2 = (ImageView) findViewById(R.id.main_foot_split2);

		main_foot_radio_add
				.setOnClickListener(RadioBtnClick(main_foot_radio_add));
		main_foot_radio_edit
				.setOnClickListener(RadioBtnClick(main_foot_radio_edit));
		main_foot_radio_delete
				.setOnClickListener(RadioBtnClick(main_foot_radio_delete));

		if (!UserRightHelper.CheckAction(context, "Add", "Incident"))
		{
			main_foot_radio_add.setVisibility(View.GONE);
			main_foot_split1.setVisibility(View.GONE);
		}

		if (!UserRightHelper.CheckAction(context, "Add", "Incident"))
		{
			main_foot_radio_edit.setVisibility(View.GONE);
			main_foot_split2.setVisibility(View.GONE);
		}

		if (!UserRightHelper.CheckAction(context, "Add", "Incident"))
			main_foot_radio_delete.setVisibility(View.GONE);
	}

	private void initSlidingMenu()
	{
		sliding_menu_layout = (SlidingMenuLayout) findViewById(R.id.sliding_menu_layout);
		main_content_layout = (LinearLayout) findViewById(R.id.main_content_layout);

		sliding_menu_layout.setDisabledType(DisabledType.DisabledRight);
		sliding_menu_layout.setScrollEvent(lvActivity);
		slide_listview = (ListView) this.findViewById(R.id.slide_listview);

		List<SlideMenuInfo> list = SlideMenuInfo.getList(context);

		SlideMenuAdapter adapter = new SlideMenuAdapter(this, list,
				R.layout.item_slide_menu);
		slide_listview.setAdapter(adapter);

		head_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				if (sliding_menu_layout.isLeftLayoutVisible())
				{
					sliding_menu_layout.scrollToContentFromLeftMenu();
				}
				else
				{
					sliding_menu_layout.initShowLeftState();
					sliding_menu_layout.scrollToLeftMenu();
				}
			}
		});

		slide_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View itemView,
					int arg2, long arg3)
			{
				ImageView slide_menu_prev_bgcolor = null;
				LinearLayout slide_menu_background = null;

				if (Last_Selected_View != null)
				{
					slide_menu_prev_bgcolor = (ImageView) Last_Selected_View
							.findViewById(R.id.slide_menu_prev_bgcolor);
					slide_menu_background = (LinearLayout) Last_Selected_View
							.findViewById(R.id.slide_menu_background);

					slide_menu_prev_bgcolor.setBackgroundColor(Color
							.parseColor("#3d4140"));
					slide_menu_background.setBackgroundColor(Color
							.parseColor("#3d4140"));
				}

				slide_menu_prev_bgcolor = (ImageView) itemView
						.findViewById(R.id.slide_menu_prev_bgcolor);
				slide_menu_background = (LinearLayout) itemView
						.findViewById(R.id.slide_menu_background);

				slide_menu_prev_bgcolor.setBackgroundColor(Color
						.parseColor("#649900"));
				slide_menu_background.setBackgroundColor(Color
						.parseColor("#2c2e2b"));

				Last_Selected_View = itemView;

				TextView text = (TextView) itemView
						.findViewById(R.id.slide_menu_text);
				Object obj = text.getTag();
				if (obj != null)
				{
					String modCode = obj.toString();
					if (modCode == "Select Client")
					{
						Intent intent = new Intent(Main.this,
								SelectClient.class);
						startActivity(intent);
						finish();
					}
					else if (modCode == "Select Location")
					{
						Intent intent = new Intent(Main.this,
								SelectLocation.class);
						startActivity(intent);
						finish();
					}
				}
			}
		});
	}

	private void initIncidentListView()
	{
		lvActivityAdapter = new ListViewActivityAdapter(context,
				lvActivityData, R.layout.item_incident);

		lvActivityFooter = getLayoutInflater().inflate(
				R.layout.listview_footer, null);
		lvActivity_footer_more = (TextView) lvActivityFooter
				.findViewById(R.id.listview_foot_more);
		lvActivity_footer_progress = (ProgressBar) lvActivityFooter
				.findViewById(R.id.listview_foot_progress);

		lvActivity = (PullToRefreshListView) findViewById(R.id.listview_activity);
		// 添加底部视图 必须在setAdapter前
		lvActivity.addFooterView(lvActivityFooter);
		lvActivity.setAdapter(lvActivityAdapter);

		lvActivity.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				lvActivity.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvActivityData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try
				{
					if (view.getPositionForView(lvActivityFooter) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				}
				catch (Exception e)
				{
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvActivity.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE)
				{
					lvActivity.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvActivity_footer_more.setText(R.string.loading);
					lvActivity_footer_progress.setVisibility(View.VISIBLE);

					// 当前pageIndex
					int pageIndex = lvActivitySumData / AppConfig.PAGE_SIZE;
					loadLvActivityData(activity_status, pageIndex,
							lvIncidentHandler, UIHelper.LISTVIEW_ACTION_SCROLL,
							false);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{
				lvActivity.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});

		lvActivity
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					@Override
					public void onRefresh()
					{
						loadLvActivityData(activity_status, 0,
								lvIncidentHandler,
								UIHelper.LISTVIEW_ACTION_REFRESH, false);
					}
				});

		initListViewData();
	}

	private void initListViewData()
	{
		// init handler
		lvIncidentHandler = this.getLvHandler(lvActivity, lvActivityAdapter,
				lvActivity_footer_more, lvActivity_footer_progress, 20);

		// loading activity data
		if (lvActivityData.isEmpty())
		{
			activity_status = "open";
			Intent intent = getIntent();
			boolean isRefresh = intent.getBooleanExtra("refresh", false);
			loadLvActivityData("open", 0, lvIncidentHandler,
					UIHelper.LISTVIEW_ACTION_INIT, isRefresh);
		}
	}

	private Handler getLvHandler(final PullToRefreshListView lv,
			final BaseAdapter adapter, final TextView more,
			final ProgressBar progress, final int pageSize)
	{
		return new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what >= 0)
				{
					// listview数据处理
					handleLvData(msg.what, msg.obj, msg.arg2, msg.arg1);
					if (msg.what < pageSize)
					{
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.loading_full);
					}
					else if (msg.what == pageSize)
					{
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.loading_more);
					}
				}
				else if (msg.what == -1)
				{
					// 有异常--显示加载出错 & 弹出错误消息
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.loading_error);
					((AppException) msg.obj).makeToast(Main.this);
				}
				if (adapter.getCount() == 0)
				{
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.loading_empty);
				}
				progress.setVisibility(View.GONE);
				head_progress.setVisibility(View.GONE);
				if (msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH)
				{
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update)
							+ new Date().toLocaleString());
					lv.setSelection(0);
				}
				else if (msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
				{
					lv.onRefreshComplete();
					lv.setSelection(0);
				}

				// 一定要加上这个，如果不加 测初次显示，数据无法显示
				// sliding_menu_layout.scrollToContentFromLeftMenu();
			}
		};
	}

	private void loadLvActivityData(final String status, final int pageIndex,
			final Handler handler, final int action, final boolean isRefresh)
	{
		head_progress.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				boolean _isRefresh = isRefresh;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					_isRefresh = true;
				try
				{
					List<ActivityInfo> list = ActivityService.getActivityList(
							context, "incident", pageIndex, status, _isRefresh);
					msg.what = list.size();
					msg.obj = list;
				}
				catch (AppException e)
				{
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}

				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_INCIDENT;

				handler.sendMessage(msg);
			}
		}.start();
	}

	private void handleLvData(int what, Object obj, int objtype, int actiontype)
	{
		switch (actiontype)
		{
		case UIHelper.LISTVIEW_ACTION_INIT:
		case UIHelper.LISTVIEW_ACTION_REFRESH:
		case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
			int newdata = 0;// 新加载数据-只有刷新动作才会使用到
			switch (objtype)
			{
			case UIHelper.LISTVIEW_DATATYPE_INCIDENT:
				List<ActivityInfo> list = (ArrayList<ActivityInfo>) obj;
				lvActivitySumData = what;
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
				{
					if (lvActivityData.size() > 0)
					{
						for (ActivityInfo newInfo : list)
						{
							boolean b = false;
							for (ActivityInfo oldInfo : lvActivityData)
							{
								if (newInfo.Id == oldInfo.Id)
								{
									b = true;
									break;
								}
							}
							if (!b)
								newdata++;
						}
					}
					else
					{
						newdata = what;
					}
				}
				// 先清除原有数据
				lvActivityData.clear();
				lvActivityData.addAll(list);
				break;
			}
			break;
		case UIHelper.LISTVIEW_ACTION_SCROLL:
			switch (objtype)
			{
			case UIHelper.LISTVIEW_DATATYPE_INCIDENT:
				List<ActivityInfo> slist = (ArrayList<ActivityInfo>) obj;
				lvActivitySumData += what;
				if (lvActivityData.size() > 0)
				{
					for (ActivityInfo newInfo : slist)
					{
						boolean b = false;
						for (ActivityInfo oldInfo : lvActivityData)
						{
							if (newInfo.Id == oldInfo.Id)
							{
								b = true;
								break;
							}
						}
						if (!b)
							lvActivityData.add(newInfo);
					}
				}
				else
				{
					lvActivityData.addAll(slist);
				}
				break;

			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
							loadLvActivityData(activity_status, 0,
									lvIncidentHandler,
									UIHelper.LISTVIEW_ACTION_INIT, true);
						}
						else
						{
							((AppException) msg.obj).makeToast(Main.this);
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
							String strMsg = ActivityService.operateActivity(
									context, "delete", json);

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

	private View.OnClickListener RadioBtnClick(final RadioButton radio)
	{
		return new View.OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				main_foot_radio_add.setChecked(false);
				main_foot_radio_edit.setChecked(false);
				main_foot_radio_delete.setChecked(false);
				if (radio == main_foot_radio_add)
				{
					main_foot_radio_add.setChecked(true);
					Intent intent = new Intent(Main.this,
							ActivityIncident.class);
					intent.putExtra("type", "add");

					startActivity(intent);
					finish();
				}
				else if (radio == main_foot_radio_edit)
				{
					ActivityInfo info = null;
					for (ActivityInfo item : lvActivityData)
					{
						if (item.IsSelected)
						{
							info = item;
							break;
						}
					}
					if (info != null)
					{
						main_foot_radio_edit.setChecked(true);
						Intent intent = new Intent(Main.this,
								ActivityIncident.class);
						intent.putExtra("type", "edit");
						intent.putExtra("ActivityInfo", info);

						startActivity(intent);
						finish();
					}
					else
					{
						Toast.makeText(Main.this,
								"Please select an item to edit.",
								Toast.LENGTH_SHORT).show();
					}
				}
				else if (radio == main_foot_radio_delete)
				{
					main_foot_radio_delete.setChecked(true);
					boolean flag = false;
					StringBuilder sb = new StringBuilder();
					for (ActivityInfo item : lvActivityData)
					{
						if (item.IsSelected)
						{
							sb.append(item.Id);
							sb.append(",");
							flag = true;
						}
					}

					if (!flag)
					{
						Toast.makeText(Main.this,
								"Please select a record to delete.",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (sb.toString() != "")
					{
						sb.deleteCharAt(sb.length() - 1);
					}

					delete(sb.toString());
				}

			}
		};
	}

	private View.OnClickListener ActivityStatusBtnClick(final Button btn,
			final String category)
	{
		return new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if (btn == btn_activity_open)
				{
					btn_activity_open.setEnabled(false);
				}
				else
				{
					btn_activity_open.setEnabled(true);
				}

				if (btn == btn_activity_closed)
				{
					btn_activity_closed.setEnabled(false);
				}
				else
				{
					btn_activity_closed.setEnabled(true);
				}

				if (btn == btn_activity_all)
				{
					btn_activity_all.setEnabled(false);
				}
				else
				{
					btn_activity_all.setEnabled(true);
				}

				activity_status = category;
				loadLvActivityData(activity_status, 0, lvIncidentHandler,
						UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG, false);
			}
		};
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
