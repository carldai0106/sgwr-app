package com.sgwr.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.ActionType;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ViewPagerAdapter;
import com.sgwr.app.bean.ActivityInfo;
import com.sgwr.app.bean.AffiliationTypeInfo;
import com.sgwr.app.bean.AreaInfo;
import com.sgwr.app.bean.CategoryInfo;
import com.sgwr.app.bean.ClientInfo;
import com.sgwr.app.bean.IncidentContactInfo;
import com.sgwr.app.bean.IncidentInfo;
import com.sgwr.app.bean.IncidentNotifyInfo;
import com.sgwr.app.bean.IncidentReplyInfo;
import com.sgwr.app.bean.LocationInfo;
import com.sgwr.app.bean.CheckDateItemInfo;
import com.sgwr.app.bean.NotifyTypeInfo;
import com.sgwr.app.bean.ShiftTimeInfo;
import com.sgwr.app.bean.SpinnerData;
import com.sgwr.app.bean.StateTypeInfo;
import com.sgwr.app.bean.UrgentReplyTypeInfo;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.bean.WorkerPostsInfo;
import com.sgwr.app.bean.ZoneInfo;
import com.sgwr.app.service.ActivityService;
import com.sgwr.app.service.CommonService;
import com.sgwr.app.service.IncidentService;
import com.sgwr.app.service.LocationService;
import com.sgwr.app.utils.DateTimeUtils;
import com.sgwr.app.utils.JsonUtils;
import com.sgwr.app.utils.StringUtils;
import com.sgwr.app.utils.UIHelper;
import com.sgwr.app.utils.UserRightHelper;
import com.sgwr.app.utils.Utilities;
import com.sgwr.app.utils.Utilities.Predicate;
import com.sgwr.app.widget.LoadingDialog;
import com.sgwr.app.widget.SgwrViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityIncident extends BaseActivity {

	private final static int REQUEST_CODE = 2000;

	private final static int SPINNER_LOCATION_SHIFTTIME = 0x01;
	private final static int SPINNER_ZONE_ASSIGNED_POST = 0x02;
	private final static int SPINNER_AREA = 0x03;
	private final static int SPINNER_INCIDENT_DATA = 0x06;

	private final static int LIST_INCIDENT_NOTIFIES = 0x11;
	private final static int LIST_INCIDENT_REPLIES = 0x12;
	private final static int MAP_WHO_INVOLVED_DETAILS = 0x08;

	// header
	private ImageButton inner_header_back;
	private TextView inner_header_title;
	private ProgressBar inner_header_progress;
	// footer
	private RadioButton inner_foot_radio_prev;
	private RadioButton inner_foot_radio_save;
	private RadioButton inner_foot_radio_next;

	private ImageView inner_foot_split1;
	private ImageView inner_foot_split2;

	private SgwrViewPager activity_incident_viewpager;
	private View view_activity_part;
	private View view_incident_part;
	private View view_incident_other_part;

	// activity part
	private Spinner spinner_activity_status;
	private CheckBox chk_activity_allow;
	private Spinner spinner_activity_location;
	private Spinner spinner_activity_zone;
	private Spinner spinner_activity_area;
	private Spinner spinner_activity_assignedpost;
	private Spinner spinner_activity_shifttime;
	private EditText edit_activity_brief;

	// incident part
	private EditText edit_incident_reporter;
	private Spinner spinner_incident_incidenttype;
	private Spinner spinner_incident_subtype;
	private Spinner spinner_incident_status;
	private Spinner spinner_incident_priority;
	private Spinner spinner_incident_witness_status;
	private EditText edit_incident_occurred_datetime;

	private EditText edit_incident_memo;
	private EditText edit_incident_caused;
	private EditText edit_incident_where;
	private EditText edit_incident_injury;

	private Button btn_incident_who_involved;
	private Button btn_incident_who_involved_details;
	private Button btn_incident_notify;
	private Button btn_incident_affilication;
	private Button btn_incident_response;
	private Button btn_incident_othercnd;
	private Button btn_incident_setemail;
	private Button btn_incident_review;
	private Button btn_activity_photo;
	private Button btn_activity_photo_list;

	private List<View> viewList = new ArrayList<View>();
	private List<SpinnerData> spinnerLocations = new ArrayList<SpinnerData>();
	private List<SpinnerData> spinnerZones = new ArrayList<SpinnerData>();
	private List<SpinnerData> spinnerAreas = new ArrayList<SpinnerData>();
	private List<SpinnerData> spinnerPosts = new ArrayList<SpinnerData>();
	private List<SpinnerData> spinnerShiftTimes = new ArrayList<SpinnerData>();

	private List<SpinnerData> spinnerCategories = new ArrayList<SpinnerData>();
	private List<CategoryInfo> subcategorys = new ArrayList<CategoryInfo>();
	private List<SpinnerData> spinnerSubCategories = new ArrayList<SpinnerData>();
	private List<SpinnerData> spinnerStatus = new ArrayList<SpinnerData>();
	private List<SpinnerData> spinnerPriorities = new ArrayList<SpinnerData>();
	private List<SpinnerData> spinnerWitnessStatus = new ArrayList<SpinnerData>();

	private List<NotifyTypeInfo> notifytypes = new ArrayList<NotifyTypeInfo>();
	private List<AffiliationTypeInfo> affiliations = new ArrayList<AffiliationTypeInfo>();
	private List<UrgentReplyTypeInfo> replytypes = new ArrayList<UrgentReplyTypeInfo>();
	private List<IncidentNotifyInfo> incidentNotifies = new ArrayList<IncidentNotifyInfo>();
	private List<IncidentReplyInfo> incidentReplies = new ArrayList<IncidentReplyInfo>();

	private ArrayAdapter<SpinnerData> adapterAcitivtyStatus = null;
	private ArrayAdapter<SpinnerData> adapterLocation = null;
	private ArrayAdapter<SpinnerData> adapterShiftTime = null;
	private ArrayAdapter<SpinnerData> adapterPosts = null;
	private ArrayAdapter<SpinnerData> adapterZone = null;
	private ArrayAdapter<SpinnerData> adapterArea = null;

	private ArrayAdapter<SpinnerData> adapterIncidentStatus = null;
	private ArrayAdapter<SpinnerData> adapterCategory = null;

	private Handler locationHandler;
	private Handler zoneHandler;
	private Handler areaHandler;
	private Handler categoryHandler;
	private Handler whoinvolvedHandler;

	private ArrayAdapter<SpinnerData> adapterSubCategory;
	private AppContext context;
	private int currentViewPager = 0;

	// 定义Result Code
	private int NotifyResultCode = 1001;
	private int ReplyResultCode = 1002;
	private int AffilicationResultCode = 1003;
	private int ReviewResultCode = 1005;

	// 定义保存 被选择后的回传值
	private List<CheckDateItemInfo> NotifyResults = null;
	private List<CheckDateItemInfo> ReplyResults = null;
	private List<CheckDateItemInfo> AffiliationResults = null;
	private List<IncidentContactInfo> incidentContactList = new ArrayList<IncidentContactInfo>();
	private HashMap<String, String> reviewMaps = null;

	private String PLEASE_SELECT_ONE = "";

	// 定义添加时 取得的新的ID
	private long ActivityId = 0;
	private long IncidentId = 0;
	private ActionType OperateType;

	private UserInfo userinfo = null;
	private LoadingDialog loadDlg = null;
	private ActivityInfo editActivityInfo = null;
	private IncidentInfo editIncidentInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_incident);

		context = (AppContext) getApplication();
		userinfo = context.CurrentUser;

		PLEASE_SELECT_ONE = getString(R.string.please_select_one);

		Intent intent = getIntent();
		String type = intent.getStringExtra("type");

		if (type.equals("edit"))
		{
			OperateType = ActionType.Edit;
			editActivityInfo = (ActivityInfo) intent
					.getSerializableExtra("ActivityInfo");
			ActivityId = editActivityInfo.Id;
		}
		else
		{

			OperateType = ActionType.Add;
		}

		initMainView();
		initActivityPartView();
		initIncidentPartView();
	}

	private void initMainView()
	{
		// header
		inner_header_back = (ImageButton) findViewById(R.id.inner_header_back);
		inner_header_title = (TextView) findViewById(R.id.inner_header_title);
		inner_header_progress = (ProgressBar) findViewById(R.id.inner_header_progress);
		// footer
		inner_foot_radio_prev = (RadioButton) findViewById(R.id.inner_foot_radio_prev);
		inner_foot_radio_next = (RadioButton) findViewById(R.id.inner_foot_radio_next);
		inner_foot_radio_save = (RadioButton) findViewById(R.id.inner_foot_radio_save);

		inner_foot_split1 = (ImageView) findViewById(R.id.inner_foot_split1);
		inner_foot_split2 = (ImageView) findViewById(R.id.inner_foot_split2);

		inner_foot_split2.setVisibility(View.GONE);
		inner_foot_radio_save.setVisibility(View.GONE);
		if (OperateType == ActionType.Add)
			inner_header_title.setText("New Incident");
		else
			inner_header_title.setText("Edit Incident");

		inner_foot_radio_prev.setOnClickListener(new ClickActionEvent());
		inner_foot_radio_next.setOnClickListener(new ClickActionEvent());
		inner_foot_radio_save.setOnClickListener(new ClickActionEvent());

		activity_incident_viewpager = (SgwrViewPager) findViewById(R.id.activity_incident_viewpager);

		getLayoutInflater();
		LayoutInflater layout = LayoutInflater.from(this);
		view_activity_part = layout.inflate(R.layout.frm_activity_part, null);
		view_incident_part = layout.inflate(R.layout.frm_incident_part, null);
		view_incident_other_part = layout.inflate(
				R.layout.frm_incident_other_part, null);

		viewList.add(view_activity_part);
		viewList.add(view_incident_part);
		viewList.add(view_incident_other_part);

		ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
		activity_incident_viewpager.setAdapter(adapter);

		inner_header_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(ActivityIncident.this, Main.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initActivityPartView()
	{
		spinner_activity_status = (Spinner) view_activity_part
				.findViewById(R.id.spinner_activity_status);
		chk_activity_allow = (CheckBox) view_activity_part
				.findViewById(R.id.chk_activity_allow);
		spinner_activity_location = (Spinner) view_activity_part
				.findViewById(R.id.spinner_activity_location);
		spinner_activity_zone = (Spinner) view_activity_part
				.findViewById(R.id.spinner_activity_zone);
		spinner_activity_area = (Spinner) view_activity_part
				.findViewById(R.id.spinner_activity_area);
		spinner_activity_assignedpost = (Spinner) view_activity_part
				.findViewById(R.id.spinner_activity_assignedpost);
		spinner_activity_shifttime = (Spinner) view_activity_part
				.findViewById(R.id.spinner_activity_shifttime);
		edit_activity_brief = (EditText) view_activity_part
				.findViewById(R.id.edit_activity_brief);

		btn_activity_photo = (Button) view_activity_part
				.findViewById(R.id.btn_activity_photo);
		btn_activity_photo_list = (Button) view_activity_part
				.findViewById(R.id.btn_activity_photo_list);

		btn_activity_photo.setOnClickListener(new ClickActionEvent());
		btn_activity_photo.setVisibility(View.GONE);
		
		btn_activity_photo_list.setOnClickListener(new ClickActionEvent());

		/* Start Activity Status */
		List<SpinnerData> list = new ArrayList<SpinnerData>();
		list.add(new SpinnerData("Open", "Open"));
		list.add(new SpinnerData("Closed", "Closed"));

		adapterAcitivtyStatus = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, list);
		adapterAcitivtyStatus
				.setDropDownViewResource(android.R.layout.select_dialog_item);

		spinner_activity_status.setAdapter(adapterAcitivtyStatus);
		/* End Activity Status */

		/* Start Location */
		adapterLocation = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinnerLocations);
		adapterLocation
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_activity_location.setAdapter(adapterLocation);

		spinner_activity_location
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3)
					{
						SpinnerData data = (SpinnerData) spinner_activity_location
								.getSelectedItem();

						String locationId = data.getValue().toString();
						// Location 发生改变，则 加载 Zone 和 Assigned Post
						loadZoneData(zoneHandler, locationId);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
						// TODO Auto-generated method stub

					}
				});
		locationHandler = this.getHandler();
		/* End Location */

		/* Start Shift time */
		adapterShiftTime = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinnerShiftTimes);
		adapterShiftTime
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_activity_shifttime.setAdapter(adapterShiftTime);
		/* End Shift time */

		/* Start Assigned Post */
		adapterPosts = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinnerPosts);
		adapterPosts
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_activity_assignedpost.setAdapter(adapterPosts);

		/* End Assigned Post */

		/* Start Zone */
		adapterZone = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinnerZones);
		adapterZone
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_activity_zone.setAdapter(adapterZone);
		zoneHandler = this.getHandler();
		spinner_activity_zone
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						SpinnerData data = (SpinnerData) spinner_activity_zone
								.getSelectedItem();
						String zoneId = data.getValue().toString();
						loadAreaData(areaHandler, zoneId);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
						// TODO Auto-generated method stub

					}
				});
		/* End Zone */

		/* Start Area */
		adapterArea = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinnerAreas);
		adapterArea
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_activity_area.setAdapter(adapterArea);
		areaHandler = this.getHandler();
		/* End Area */

		if (OperateType == ActionType.Edit && editActivityInfo != null)
		{
			UIHelper.setSelection(spinner_activity_status, list,
					editActivityInfo.Status);
			edit_activity_brief.setText(editActivityInfo.Description);
		}

		/*
		 * 数据加载顺序 1. 首先加载 Location 和 Shift Time 2. 根据选择的Location 加载 Zone 和
		 * Assigned Post 3. 根据选择的Zone 加载 Area;
		 */
		if (spinnerLocations.isEmpty())
		{
			this.loadLocationData(locationHandler);
		}
	}

	private void initIncidentPartView()
	{
		edit_incident_reporter = (EditText) view_incident_part
				.findViewById(R.id.edit_incident_reporter);

		edit_incident_caused = (EditText) view_incident_other_part
				.findViewById(R.id.edit_incident_caused);
		edit_incident_injury = (EditText) view_incident_other_part
				.findViewById(R.id.edit_incident_injury);
		edit_incident_memo = (EditText) view_incident_other_part
				.findViewById(R.id.edit_incident_memo);
		edit_incident_where = (EditText) view_incident_other_part
				.findViewById(R.id.edit_incident_where);

		spinner_incident_incidenttype = (Spinner) view_incident_part
				.findViewById(R.id.spinner_incident_incidenttype);
		spinner_incident_subtype = (Spinner) view_incident_part
				.findViewById(R.id.spinner_incident_subtype);
		spinner_incident_status = (Spinner) view_incident_part
				.findViewById(R.id.spinner_incident_status);
		spinner_incident_priority = (Spinner) view_incident_part
				.findViewById(R.id.spinner_incident_priority);
		spinner_incident_witness_status = (Spinner) view_incident_part
				.findViewById(R.id.spinner_incident_witness_status);
		edit_incident_occurred_datetime = (EditText) view_incident_part
				.findViewById(R.id.edit_incident_occurred_datetime);
		btn_incident_notify = (Button) view_incident_part
				.findViewById(R.id.btn_incident_notify);
		btn_incident_who_involved = (Button) view_incident_part
				.findViewById(R.id.btn_incident_who_involved);
		btn_incident_who_involved_details = (Button) view_incident_part
				.findViewById(R.id.btn_incident_who_involved_details);
		btn_incident_affilication = (Button) view_incident_part
				.findViewById(R.id.btn_incident_affilication);
		btn_incident_response = (Button) view_incident_part
				.findViewById(R.id.btn_incident_response);
		btn_incident_othercnd = (Button) view_incident_part
				.findViewById(R.id.btn_incident_othercnd);
		btn_incident_setemail = (Button) view_incident_other_part
				.findViewById(R.id.btn_incident_setemail);
		btn_incident_review = (Button) view_incident_other_part
				.findViewById(R.id.btn_incident_review);

		edit_incident_occurred_datetime
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(ActivityIncident.this,
								DateTimePickerDialog.class);
						startActivityForResult(intent, REQUEST_CODE);
					}
				});

		if (OperateType == ActionType.Add)
			btn_incident_who_involved_details.setVisibility(View.GONE);

		// start incident priority
		spinnerPriorities.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
		spinnerPriorities.add(new SpinnerData("High", "High"));
		spinnerPriorities.add(new SpinnerData("Medium", "Medium"));
		spinnerPriorities.add(new SpinnerData("Low", "Low"));

		ArrayAdapter<SpinnerData> adapterPriority = new ArrayAdapter<SpinnerData>(
				this, R.layout.item_spinner, spinnerPriorities);
		adapterPriority
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_incident_priority.setAdapter(adapterPriority);
		// end incident priority

		// start witness status
		spinnerWitnessStatus.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
		spinnerWitnessStatus.add(new SpinnerData("Witness gives statement",
				"Gives"));
		spinnerWitnessStatus.add(new SpinnerData("No witness present",
				"NoWitness"));
		spinnerWitnessStatus.add(new SpinnerData(
				"Witness refused to give statement", "Refused"));

		ArrayAdapter<SpinnerData> adapterWitness = new ArrayAdapter<SpinnerData>(
				this, R.layout.item_spinner, spinnerWitnessStatus);
		adapterWitness
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_incident_witness_status.setAdapter(adapterWitness);
		// end witness status

		// start incident status
		adapterIncidentStatus = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinnerStatus);
		adapterIncidentStatus
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_incident_status.setAdapter(adapterIncidentStatus);
		// end incident status

		btn_incident_who_involved.setOnClickListener(new ClickActionEvent());
		btn_incident_notify.setOnClickListener(new ClickActionEvent());
		btn_incident_response.setOnClickListener(new ClickActionEvent());
		btn_incident_affilication.setOnClickListener(new ClickActionEvent());
		btn_incident_othercnd.setOnClickListener(new ClickActionEvent());
		btn_incident_setemail.setOnClickListener(new ClickActionEvent());
		btn_incident_review.setOnClickListener(new ClickActionEvent());
		btn_incident_who_involved_details
				.setOnClickListener(new ClickActionEvent());

		if (!UserRightHelper.CheckAction(context, "List", "Incident Email"))
			btn_incident_setemail.setVisibility(View.GONE);
		if (!UserRightHelper.CheckAction(context, "List", "Incident Review"))
			btn_incident_review.setVisibility(View.GONE);

		initCategoryData();
	}

	private void initIncidentIfEdit()
	{
		UIHelper.setSelection(spinner_incident_priority, spinnerPriorities,
				editIncidentInfo.Priority);
		UIHelper.setSelection(spinner_incident_witness_status,
				spinnerWitnessStatus, editIncidentInfo.WitnessState);

		String occurredTime = DateTimeUtils.Utc2Local(
				editIncidentInfo.OccurredTime,
				DateTimeUtils.DATE_TIME_NO_SECOND_PATTEN);
		edit_incident_occurred_datetime.setText(occurredTime);
		edit_incident_reporter.setText(editIncidentInfo.Reporter);
		edit_incident_caused.setText(editIncidentInfo.OccurredCaused);
		edit_incident_injury.setText(editIncidentInfo.InjuryDesc);
		edit_incident_memo.setText(Utilities
				.RemoveHtmlTag(editIncidentInfo.Description));
		edit_incident_where.setText(editIncidentInfo.OccurredAddr);

		reviewMaps = new HashMap<String, String>();
		reviewMaps.put("print1", editIncidentInfo.PrintName);
		reviewMaps.put("print2", editIncidentInfo.PrintName1);
		reviewMaps.put("signature1", editIncidentInfo.Signature);
		reviewMaps.put("signature2", editIncidentInfo.Signature1);
		reviewMaps.put("title1", editIncidentInfo.Title);
		reviewMaps.put("title2", editIncidentInfo.Title1);
		String date1 = editIncidentInfo.SignDate != null ? DateTimeUtils
				.Utc2Local(editIncidentInfo.SignDate, DateTimeUtils.DATE_PATTEN)
				: "";
		String date2 = editIncidentInfo.SignDate1 != null ? DateTimeUtils
				.Utc2Local(editIncidentInfo.SignDate1,
						DateTimeUtils.DATE_PATTEN) : "";
		reviewMaps.put("date1", date1);
		reviewMaps.put("date2", date2);
	}

	private void initCategoryData()
	{
		adapterCategory = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinnerCategories);
		adapterCategory
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_incident_incidenttype.setAdapter(adapterCategory);

		adapterSubCategory = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, spinnerSubCategories);

		adapterSubCategory
				.setDropDownViewResource(android.R.layout.select_dialog_item);

		spinner_incident_subtype.setAdapter(adapterSubCategory);

		spinner_incident_incidenttype
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3)
					{

						SpinnerData data = (SpinnerData) spinner_incident_incidenttype
								.getSelectedItem();

						int id = StringUtils.toInt(data.getValue());
						spinnerSubCategories.clear();
						spinnerSubCategories.add(new SpinnerData(
								PLEASE_SELECT_ONE, ""));
						for (CategoryInfo info : subcategorys)
						{
							if (info.ParentId == id)
							{
								spinnerSubCategories.add(new SpinnerData(
										info.TypeName, info.Id));
							}
						}

						if (spinnerSubCategories.size() > 1)
						{
							adapterSubCategory.notifyDataSetChanged();
						}
						if (OperateType == ActionType.Edit
								&& editIncidentInfo != null)
						{

							UIHelper.setSelection(spinner_incident_subtype,
									spinnerSubCategories,
									editIncidentInfo.SubCategoryId);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
						// TODO Auto-generated method stub
					}
				});

		categoryHandler = this.getHandler();
		if (spinnerCategories.isEmpty())
		{
			this.loadIncidentData(categoryHandler);
		}
	}

	private Handler getHandler()
	{
		return new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					handlerData(msg);
					inner_header_progress.setVisibility(View.GONE);
				}
				else
				{
					((AppException) msg.obj).makeToast(ActivityIncident.this);
				}
			}
		};
	}

	private void handlerData(Message msg)
	{
		int type = msg.arg1;
		switch (type)
		{
		case SPINNER_LOCATION_SHIFTTIME:
			// 处理 Location
			List<Object> listObjs = (List<Object>) msg.obj;

			List<LocationInfo> locations = (List<LocationInfo>) listObjs.get(0);
			List<ShiftTimeInfo> shifttimes = (List<ShiftTimeInfo>) listObjs
					.get(1);

			spinnerLocations.clear();
			spinnerLocations.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
			for (LocationInfo info : locations)
			{
				spinnerLocations.add(new SpinnerData(info.Name, info.Id));
			}
			adapterLocation.notifyDataSetChanged();

			// 处理 Shift Time
			spinnerShiftTimes.clear();
			spinnerShiftTimes.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
			for (ShiftTimeInfo info : shifttimes)
			{
				String time = info.StartTime + " - " + info.EndTime;
				spinnerShiftTimes.add(new SpinnerData(time, time));
			}
			// 通知适配器 Shift Time 数据已经改变
			adapterShiftTime.notifyDataSetChanged();

			if (OperateType == ActionType.Edit)
			{
				UIHelper.setSelection(spinner_activity_location,
						spinnerLocations, editActivityInfo.LocationId);
				UIHelper.setSelection(spinner_activity_shifttime,
						spinnerShiftTimes, editActivityInfo.ShiftTime);
			}
			break;
		case SPINNER_ZONE_ASSIGNED_POST:
			List<Object> objList = (List<Object>) msg.obj;

			List<ZoneInfo> zones = (List<ZoneInfo>) objList.get(0);
			spinnerZones.clear();
			spinnerZones.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
			for (ZoneInfo info : zones)
			{
				spinnerZones.add(new SpinnerData(info.Name, info.Id));
			}
			adapterZone.notifyDataSetChanged();

			List<WorkerPostsInfo> posts = (List<WorkerPostsInfo>) objList
					.get(1);
			spinnerPosts.clear();
			spinnerPosts.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
			for (WorkerPostsInfo info : posts)
			{
				spinnerPosts.add(new SpinnerData(info.AssignedPost,
						info.AssignedID));
			}
			adapterPosts.notifyDataSetChanged();

			if (OperateType == ActionType.Edit)
			{
				UIHelper.setSelection(spinner_activity_zone, spinnerZones,
						editActivityInfo.ZoneId);
				UIHelper.setSelection(spinner_activity_assignedpost,
						spinnerPosts, editActivityInfo.AssignedID);
			}
			break;
		case SPINNER_AREA:
			List<AreaInfo> areas = (List<AreaInfo>) msg.obj;
			spinnerAreas.clear();
			spinnerAreas.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
			for (AreaInfo info : areas)
			{
				spinnerAreas.add(new SpinnerData(info.Name, info.Id));
			}
			adapterArea.notifyDataSetChanged();

			if (OperateType == ActionType.Edit)
			{
				UIHelper.setSelection(spinner_activity_area, spinnerAreas,
						editActivityInfo.AreaId);
			}
			break;

		case SPINNER_INCIDENT_DATA:
			List<Object> incidentList = (List<Object>) msg.obj;

			List<CategoryInfo> categories = (List<CategoryInfo>) incidentList
					.get(0);
			spinnerCategories.clear();
			spinnerCategories.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
			for (CategoryInfo info : categories)
			{
				if (info.ParentId == 0)
				{
					spinnerCategories.add(new SpinnerData(info.TypeName,
							info.Id));
				}
				else
				{
					subcategorys.add(info);
				}
			}
			adapterCategory.notifyDataSetChanged();

			List<StateTypeInfo> status = (List<StateTypeInfo>) incidentList
					.get(1);
			spinnerStatus.clear();
			spinnerStatus.add(new SpinnerData(PLEASE_SELECT_ONE, ""));
			for (StateTypeInfo info : status)
			{
				spinnerStatus.add(new SpinnerData(info.TypeName, info.Id));
			}
			adapterIncidentStatus.notifyDataSetChanged();

			notifytypes = (List<NotifyTypeInfo>) incidentList.get(2);
			replytypes = (List<UrgentReplyTypeInfo>) incidentList.get(3);
			affiliations = (List<AffiliationTypeInfo>) incidentList.get(4);

			if (OperateType == ActionType.Edit && editIncidentInfo != null)
			{
				UIHelper.setSelection(spinner_incident_incidenttype,
						spinnerCategories, editIncidentInfo.CategoryId);

				UIHelper.setSelection(spinner_incident_status, spinnerStatus,
						editIncidentInfo.StateId);

				initIncidentIfEdit();
			}
			if (loadDlg != null && loadDlg.isShowing())
				loadDlg.dismiss();
			break;

		case LIST_INCIDENT_NOTIFIES:
			incidentNotifies = (List<IncidentNotifyInfo>) msg.obj;
			break;
		case LIST_INCIDENT_REPLIES:
			incidentReplies = (List<IncidentReplyInfo>) msg.obj;
			break;
		case MAP_WHO_INVOLVED_DETAILS:
			if (loadDlg != null && loadDlg.isShowing())
				loadDlg.dismiss();
			HashMap<String, String> maps = (HashMap<String, String>) msg.obj;
			Intent intent = new Intent(ActivityIncident.this,
					SelectContactType.class);
			intent.putExtra("activityid", ActivityId);
			intent.putExtra("incidentid", IncidentId);
			intent.putExtra("maps", maps);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 导入Location数据，同时加载Shift Time 数据 如果是首次添加的话，创建一个Activity临时数据
	 * 
	 * @param handler
	 */
	private void loadLocationData(final Handler handler)
	{
		loading();
		inner_header_progress.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				try
				{
					// 如果是首次添加的话，创建一个Activity临时数据
					if (OperateType == ActionType.Add)
					{
						ActivityId = ActivityService.getNewId(context,
								userinfo.UserName, userinfo.Password);
					}

					Map<String, String> maps = LocationService
							.getLocationAndShiftTime(context);
					String jsonLocation = maps.get("location");
					String jsonShiftTime = maps.get("shifttime");

					List<LocationInfo> locations = JsonUtils.DeserializeObject(
							jsonLocation,
							new TypeReference<List<LocationInfo>>() {
							});

					List<ShiftTimeInfo> shifttimes = JsonUtils
							.DeserializeObject(jsonShiftTime,
									new TypeReference<List<ShiftTimeInfo>>() {
									});

					List<Object> list = new ArrayList<Object>();
					list.add(locations);
					list.add(shifttimes);

					msg.what = 1;
					msg.obj = list;
				}
				catch (AppException e)
				{
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = SPINNER_LOCATION_SHIFTTIME;
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * load zone information
	 * 
	 * @param handler
	 * @param locationId
	 *            --- selected location id
	 */
	private void loadZoneData(final Handler handler, final String locationId)
	{
		inner_header_progress.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				try
				{
					List<ZoneInfo> zoneList = LocationService.getZoneList(
							context, locationId);
					List<WorkerPostsInfo> postList = CommonService
							.getWorkerPostsList(context, locationId);

					List<Object> list = new ArrayList<Object>();
					list.add(zoneList);
					list.add(postList);
					msg.what = 1;
					msg.obj = list;
				}
				catch (AppException e)
				{
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = SPINNER_ZONE_ASSIGNED_POST;
				handler.sendMessage(msg);
			}

		}.start();
	}

	/**
	 * load area information by zone id
	 * 
	 * @param handler
	 * @param zoneId
	 *            --- selected zone id
	 */
	private void loadAreaData(final Handler handler, final String zoneId)
	{
		inner_header_progress.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				try
				{
					msg.what = 1;
					msg.obj = LocationService.getAreaList(context, zoneId);
				}
				catch (AppException e)
				{
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = SPINNER_AREA;
				handler.sendMessage(msg);
			}

		}.start();
	}

	/**
	 * 同时去获取 1. Category；2. Incident Status； 3. Notify Types; 4. Affilication
	 * Tyeps; 5. Urgent Types; 如果是编辑状态同时去取得 Incident Info 根据 Activity ID
	 * 
	 * @param handler
	 */
	private void loadIncidentData(final Handler handler)
	{
		inner_header_progress.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();
				try
				{
					// 如果是Edit
					if (OperateType == ActionType.Edit)
					{
						editIncidentInfo = IncidentService.getIncident(context,
								String.valueOf(editActivityInfo.Id));
						IncidentId = editIncidentInfo.ID;
					}

					Map<String, String> maps = IncidentService
							.getIncidentOtherInitInfo(context);
					String jsonCategory = maps.get("category");
					String jsonStatus = maps.get("status");
					String jsonNotify = maps.get("notify");
					String jsonReply = maps.get("reply");
					String jsonAffiliation = maps.get("affiliation");

					List<CategoryInfo> categoryList = JsonUtils
							.DeserializeObject(jsonCategory,
									new TypeReference<List<CategoryInfo>>() {
									});

					List<StateTypeInfo> stateList = JsonUtils
							.DeserializeObject(jsonStatus,
									new TypeReference<List<StateTypeInfo>>() {
									});

					List<NotifyTypeInfo> notifyList = JsonUtils
							.DeserializeObject(jsonNotify,
									new TypeReference<List<NotifyTypeInfo>>() {
									});
					List<UrgentReplyTypeInfo> replyList = JsonUtils
							.DeserializeObject(
									jsonReply,
									new TypeReference<List<UrgentReplyTypeInfo>>() {
									});
					List<AffiliationTypeInfo> affiliationList = JsonUtils
							.DeserializeObject(
									jsonAffiliation,
									new TypeReference<List<AffiliationTypeInfo>>() {
									});

					List<Object> list = new ArrayList<Object>();
					list.add(categoryList);
					list.add(stateList);
					list.add(notifyList);
					list.add(replyList);
					list.add(affiliationList);

					msg.what = 1;
					msg.obj = list;
				}
				catch (AppException e)
				{
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = SPINNER_INCIDENT_DATA;
				handler.sendMessage(msg);
			}
		}.start();
	}

	private void loadWhoInvolvedDetails(final Handler handler)
	{
		if (loadDlg != null)
		{
			loadDlg = new LoadingDialog(this);
			loadDlg.show();
		}
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();

				try
				{
					HashMap<String, String> maps = IncidentService
							.getWhoInvolvedDetails(context,
									String.valueOf(ActivityId));

					msg.what = 1;
					msg.obj = maps;

				}
				catch (AppException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = MAP_WHO_INVOLVED_DETAILS;

				handler.sendMessage(msg);
			}
		}.start();

	}

	private void loadIncidentNotify(final Handler handler)
	{
		inner_header_progress.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();

				try
				{
					List<IncidentNotifyInfo> list = IncidentService
							.getIncidentNotifies(context,
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
				msg.arg1 = LIST_INCIDENT_NOTIFIES;
				handler.sendMessage(msg);
			}
		}.start();
	}

	private void loadIncidentReplies(final Handler handler)
	{
		inner_header_progress.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run()
			{
				Message msg = new Message();

				try
				{
					List<IncidentReplyInfo> list = IncidentService
							.getIncidenReplies(context,
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
				msg.arg1 = LIST_INCIDENT_REPLIES;
				handler.sendMessage(msg);
			}
		}.start();
	}

	private void getIncidentId()
	{
		new Thread() {
			@Override
			public void run()
			{
				try
				{
					IncidentId = IncidentService.getNewId(context,
							String.valueOf(ActivityId), userinfo.UserName,
							userinfo.Password);
				}
				catch (AppException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				String datetime = data.getStringExtra("datetime");
				edit_incident_occurred_datetime.setText(datetime);
			}// 来自CheckDateListDialog的回传值 Notify
			else if (resultCode == NotifyResultCode)
			{
				Serializable obj = data
						.getSerializableExtra("chk_date_list_result");
				NotifyResults = (ArrayList<CheckDateItemInfo>) obj;
				if (NotifyResults != null && !NotifyResults.isEmpty())
				{
					addIncidentNotify(NotifyResults);
				}
			}// 来自CheckDateListDialog的回传值 Urgent Reply
			else if (resultCode == ReplyResultCode)
			{
				Serializable obj = data
						.getSerializableExtra("chk_date_list_result");
				ReplyResults = (ArrayList<CheckDateItemInfo>) obj;

				if (ReplyResults != null && !ReplyResults.isEmpty())
				{
					addEmergencyReplies(ReplyResults);
				}
			}
			else if (resultCode == AffilicationResultCode)
			{
				Serializable obj = data
						.getSerializableExtra("chk_date_list_result");
				AffiliationResults = (ArrayList<CheckDateItemInfo>) obj;
			}
			else if (resultCode == ReviewResultCode)
			{
				Serializable obj = data.getSerializableExtra("review");
				reviewMaps = (HashMap<String, String>) obj;
			}
		}
	}

	/*
	 * 添加，编辑 Activity, Incident
	 */
	private void operate()
	{
		loading();

		ClientInfo client = context.CurrentClient;
		com.sgwr.app.bean.ActivityInfo activity = new com.sgwr.app.bean.ActivityInfo();
		activity.ActivityType = "Incident";
		String areaId = Utilities.getSpinnerValue(spinner_activity_area);
		activity.AreaId = areaId;
		String assignedID = Utilities
				.getSpinnerValue(spinner_activity_assignedpost);
		activity.AssignedID = Utilities.Object2Int(assignedID);
		String assignedName = Utilities
				.getSpinnerText(spinner_activity_assignedpost);
		activity.AssignedName = assignedName;
		activity.ClientId = String.valueOf(client.Id);
		activity.CreatedBy = userinfo.UserName;
		activity.CreatedTime = DateTimeUtils.getCurrentUtcDate();
		String description = Utilities.getEditText(edit_activity_brief);
		activity.Description = description;
		activity.Id = ActivityId;
		String locationId = Utilities
				.getSpinnerValue(spinner_activity_location);
		activity.LocationId = locationId;
		String shiftTime = Utilities.getSpinnerText(spinner_activity_shifttime);
		activity.ShiftTime = shiftTime;
		activity.State = "";
		String status = Utilities.getSpinnerText(spinner_activity_status);
		activity.Status = status;
		activity.Visibility = chk_activity_allow.isChecked();
		String zoneId = Utilities.getSpinnerValue(spinner_activity_zone);
		activity.ZoneId = zoneId;

		IncidentInfo incident = new IncidentInfo();
		incident.ActivityId = String.valueOf(ActivityId);

		if (AffiliationResults != null && !AffiliationResults.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			for (CheckDateItemInfo item : AffiliationResults)
			{
				sb.append(item.Id);
				sb.append(",");
			}

			incident.Affiliation = StringUtils.removeLastChar(sb);
		}

		String categoryId = Utilities
				.getSpinnerValue(spinner_incident_incidenttype);
		incident.CategoryId = categoryId;
		incident.CreatedBy = userinfo.UserName;
		incident.CreatedDate = DateTimeUtils.getCurrentUtcDate();
		incident.Class = "Main";
		incident.ClientId = String.valueOf(client.Id);
		String incidentDesc = Utilities.getEditText(edit_incident_memo);
		incident.Description = incidentDesc;
		incident.ID = IncidentId;
		incident.InjuryDesc = Utilities.getEditText(edit_incident_injury);

		incident.OccurredAddr = Utilities.getEditText(edit_incident_where);
		incident.OccurredCaused = Utilities.getEditText(edit_incident_caused);
		String strOccDate = Utilities
				.getEditText(edit_incident_occurred_datetime);

		Date utcOccDate = DateTimeUtils.Local2Utc(strOccDate);
		incident.OccurredTime = utcOccDate;
		incident.Priority = Utilities
				.getSpinnerValue(spinner_incident_priority);
		incident.Reporter = Utilities.getEditText(edit_incident_reporter);

		if (reviewMaps != null)
		{
			incident.PrintName = reviewMaps.get("print1");
			incident.PrintName1 = reviewMaps.get("print2");
			incident.Signature = reviewMaps.get("signature1");
			incident.Signature1 = reviewMaps.get("signature2");
			String date1 = reviewMaps.get("date1");
			String date2 = reviewMaps.get("date2");
			if (!StringUtils.isEmpty(date1))
			{
				incident.SignDate = DateTimeUtils.Local2Utc(date1,
						DateTimeUtils.DATE_PATTEN);
			}
			if (!StringUtils.isEmpty(date2))
			{
				incident.SignDate1 = DateTimeUtils.Local2Utc(date2,
						DateTimeUtils.DATE_PATTEN);
			}
			incident.Title = reviewMaps.get("title1");
			incident.Title1 = reviewMaps.get("title2");
		}
		incident.StateId = Utilities.getSpinnerValue(spinner_incident_status);
		incident.SubCategoryId = Utilities
				.getSpinnerValue(spinner_incident_subtype);

		incident.WitnessState = Utilities
				.getSpinnerValue(spinner_incident_witness_status);

		if (OperateType == ActionType.Edit)
		{
			activity.ChangeBy = userinfo.UserName;
			activity.ChangeTime = DateTimeUtils.getCurrentUtcDate();

			incident.ChangeBy = userinfo.UserName;
			incident.ChangeTime = DateTimeUtils.getCurrentUtcDate();
		}

		String[] strArr = new String[2];
		strArr[0] = JsonUtils.SerializeObject(activity);
		strArr[1] = JsonUtils.SerializeObject(incident);

		final String strJson = JsonUtils.SerializeObject(strArr);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				loadDlg.dismiss();
				if (msg.what == 1)
				{
					Intent intent = new Intent(ActivityIncident.this,
							Main.class);

					intent.putExtra("refresh", true);
					startActivity(intent);
					finish();
				}
				else
				{
					((AppException) msg.obj).makeToast(ActivityIncident.this);
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
					String strMsg = IncidentService.operateActivityIncident(
							context, "edit", strJson);

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
		}.start();
	}

	/*
	 * 检测Activity 必填项
	 */
	private boolean checkActivity()
	{
		SpinnerData data = (SpinnerData) spinner_activity_location
				.getSelectedItem();
		if (data == null || data.getValue().equals(""))
		{
			Toast.makeText(ActivityIncident.this, "Please select a location",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		String post = Utilities.getSpinnerValue(spinner_activity_assignedpost);
		if (spinnerPosts.size() > 1 && post.equals(""))
		{
			Toast.makeText(ActivityIncident.this,
					"Please select an assigned post.", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		String shifttime = Utilities
				.getSpinnerValue(spinner_activity_shifttime);
		if (!post.equals("") && shifttime.equals(""))
		{
			Toast.makeText(ActivityIncident.this,
					"Please select a shift time.", Toast.LENGTH_SHORT).show();
			return false;
		}

		String brief = Utilities.getEditText(edit_activity_brief);
		if (StringUtils.isEmpty(brief))
		{
			Toast.makeText(ActivityIncident.this,
					"Please enter brief description.", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		return true;
	}

	/*
	 * 检测Incident 必填项
	 */
	private boolean checkIncident()
	{
		String strReporter = Utilities.getEditText(edit_incident_reporter);
		if (strReporter.equals(""))
		{
			Toast.makeText(this, "Please enter Person Completing Report.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (Utilities.getSpinnerValue(spinner_incident_incidenttype).equals(""))
		{
			Toast.makeText(this, "Please select a type of incident.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (Utilities.getSpinnerValue(spinner_activity_status).equals(""))
		{
			Toast.makeText(this, "Please select a Incident Status!",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (Utilities.getSpinnerValue(spinner_incident_priority).equals(""))
		{
			Toast.makeText(this, "Please select a Priority.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (Utilities.getEditText(edit_incident_occurred_datetime).equals(""))
		{
			Toast.makeText(this, "Please enter Incident Occurred on.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	/*
	 * 当点击Select Notifies 执行
	 */
	private void initIncidentNotify()
	{
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					handlerData(msg);
					inner_header_progress.setVisibility(View.GONE);

					ArrayList<CheckDateItemInfo> list = new ArrayList<CheckDateItemInfo>();
					for (final NotifyTypeInfo info : notifytypes)
					{
						CheckDateItemInfo _info = new CheckDateItemInfo();
						IncidentNotifyInfo model = Utilities.check(
								incidentNotifies,
								new Predicate<IncidentNotifyInfo>() {
									@Override
									public boolean Predicate(
											IncidentNotifyInfo t)
									{
										if (info.ID == t.NotifyID)
											return true;
										else
											return false;
									}
								});

						if (model != null)
						{
							_info.Id = info.ID;
							_info.IsChecked = true;
							_info.DateTime = DateTimeUtils.Utc2Local(
									model.CreatedTime,
									DateTimeUtils.DATE_TIME_NO_SECOND_PATTEN);
							_info.Title = info.NotifyType;
							list.add(_info);
						}
						else
						{
							_info.Id = info.ID;
							_info.IsChecked = false;
							_info.DateTime = "";
							_info.Title = info.NotifyType;
							list.add(_info);
						}

					}
					Intent intent = new Intent(ActivityIncident.this,
							CheckDateListDialog.class);
					intent.putExtra("chk_date_list_type",
							CheckDateListDialog.CHECKBOX_DATETIME_ITEM);
					intent.putExtra("chk_date_list_title",
							getString(R.string.select_notifications));
					intent.putExtra("chk_date_list_data", list);
					intent.putExtra("chk_date_list_result_code",
							NotifyResultCode);

					startActivityForResult(intent, REQUEST_CODE);
				}
				else
				{
					((AppException) msg.obj).makeToast(ActivityIncident.this);
				}
			}
		};

		loadIncidentNotify(handler);
	}

	private void addIncidentNotify(List<CheckDateItemInfo> list)
	{
		loading();

		List<IncidentNotifyInfo> notifies = new ArrayList<IncidentNotifyInfo>();
		for (CheckDateItemInfo item : list)
		{
			IncidentNotifyInfo model = new IncidentNotifyInfo();
			model.ActivityID = ActivityId;
			model.CreatedBy = userinfo.UserName;
			model.CreatedTime = DateTimeUtils.Local2Utc(item.DateTime);
			model.IncidentID = IncidentId;
			model.NotifyID = item.Id;
			model.NotifyType = item.Title;

			notifies.add(model);
		}

		final String json = JsonUtils.SerializeObject(notifies);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					loadDlg.dismiss();
				}
				else
				{
					((AppException) msg.obj).makeToast(ActivityIncident.this);
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
					String strMsg = IncidentService.operateIncidentNotify(
							context, String.valueOf(IncidentId), "add", json);
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
		}.start();
	}

	/*
	 * 当点击Select Emergency Response 执行
	 */
	private void initIncidentReply()
	{
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					handlerData(msg);
					inner_header_progress.setVisibility(View.GONE);

					ArrayList<CheckDateItemInfo> list = new ArrayList<CheckDateItemInfo>();
					for (final UrgentReplyTypeInfo info : replytypes)
					{
						CheckDateItemInfo _info = new CheckDateItemInfo();
						IncidentReplyInfo model = Utilities.check(
								incidentReplies,
								new Predicate<IncidentReplyInfo>() {
									@Override
									public boolean Predicate(IncidentReplyInfo t)
									{
										if (info.ID == t.UrgentID)
											return true;
										else
											return false;
									}
								});

						if (model != null)
						{
							_info.Id = info.ID;
							_info.IsChecked = true;
							_info.DateTime = DateTimeUtils.Utc2Local(
									model.CreatedTime,
									DateTimeUtils.DATE_TIME_NO_SECOND_PATTEN);
							_info.Title = info.RspnType;
							list.add(_info);
						}
						else
						{
							_info.Id = info.ID;
							_info.IsChecked = false;
							_info.DateTime = "";
							_info.Title = info.RspnType;
							list.add(_info);
						}
					}

					Intent intent = new Intent(ActivityIncident.this,
							CheckDateListDialog.class);

					intent.putExtra("chk_date_list_type",
							CheckDateListDialog.CHECKBOX_DATETIME_ITEM);
					intent.putExtra("chk_date_list_title",
							getString(R.string.select_emergency_response));
					intent.putExtra("chk_date_list_data", list);
					intent.putExtra("chk_date_list_result_code",
							ReplyResultCode);

					startActivityForResult(intent, REQUEST_CODE);
				}
				else
				{
					((AppException) msg.obj).makeToast(ActivityIncident.this);
				}
			}
		};

		loadIncidentReplies(handler);
	}

	private void addEmergencyReplies(List<CheckDateItemInfo> list)
	{
		loading();

		List<IncidentReplyInfo> replies = new ArrayList<IncidentReplyInfo>();
		for (CheckDateItemInfo item : list)
		{
			IncidentReplyInfo model = new IncidentReplyInfo();
			model.ActivityID = ActivityId;
			model.CreatedBy = userinfo.UserName;
			model.CreatedTime = DateTimeUtils.Local2Utc(item.DateTime);
			model.IncidentID = IncidentId;
			model.UrgentID = item.Id;
			model.UrgentType = item.Title;

			replies.add(model);
		}

		final String json = JsonUtils.SerializeObject(replies);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					loadDlg.dismiss();
				}
				else
				{
					((AppException) msg.obj).makeToast(ActivityIncident.this);
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
					String strMsg = IncidentService.operateEmergencyReplies(
							context, String.valueOf(IncidentId), "add", json);
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
		}.start();
	}

	class ClickActionEvent implements OnClickListener {
		@Override
		public void onClick(View view)
		{
			// TODO Auto-generated method stub
			if (view instanceof RadioButton)
			{
				inner_foot_radio_next.setChecked(false);
				inner_foot_radio_prev.setChecked(false);
				inner_foot_radio_save.setChecked(false);
				inner_foot_split2.setVisibility(View.GONE);
				inner_foot_radio_save.setVisibility(View.GONE);

				if (view == inner_foot_radio_next)
				{
					if (currentViewPager == 0 && !checkActivity())
					{
						return;
					}

					if (currentViewPager == 1 && !checkIncident())
					{
						return;
					}

					currentViewPager++;
					if (currentViewPager > viewList.size() - 1)
						currentViewPager = viewList.size() - 1;

					if (currentViewPager == 1)
					{
						edit_incident_reporter.setFocusable(true);
						edit_incident_reporter.setFocusableInTouchMode(true);

						// 如果是添加Activity 并且Next到Incidnt页面时，自动去添加一条空的Incident记录
						if (OperateType == ActionType.Add && IncidentId == 0)
						{
							getIncidentId();
						}
					}

					if (currentViewPager == 2)
					{
						// 权限决定是否在这页显示Save按钮
						inner_foot_split2.setVisibility(View.VISIBLE);
						inner_foot_radio_save.setVisibility(View.VISIBLE);
					}

					inner_foot_radio_next.setChecked(true);
					activity_incident_viewpager.setCurrentItem(
							currentViewPager, true);

				}
				else if (view == inner_foot_radio_prev)
				{
					currentViewPager--;
					if (currentViewPager < 0)
						currentViewPager = 0;

					inner_foot_radio_prev.setChecked(true);
					activity_incident_viewpager.setCurrentItem(
							currentViewPager, true);
				}
				else if (view == inner_foot_radio_save)
				{
					inner_foot_radio_save.setChecked(true);
					operate();
				}
			}
			else if (view instanceof Button)
			{
				if (view == btn_incident_who_involved)
				{
					Intent intent = new Intent(ActivityIncident.this,
							IncidentContact.class);
					intent.putExtra("type", com.sgwr.app.ActionType.Add);
					intent.putExtra("ResultCode", 0);
					intent.putExtra("ActivityId", ActivityId);
					intent.putExtra("IncidentId", IncidentId);

					startActivity(intent);
				}
				else if (view == btn_incident_notify)
				{
					initIncidentNotify();
				}
				else if (view == btn_incident_response)
				{
					initIncidentReply();
				}
				else if (view == btn_incident_affilication)
				{
					Intent intent = new Intent(ActivityIncident.this,
							CheckDateListDialog.class);

					ArrayList<CheckDateItemInfo> list = new ArrayList<CheckDateItemInfo>();

					if (OperateType == ActionType.Edit
							&& editIncidentInfo != null)
					{
						List<String> listAff = new ArrayList<String>();
						String[] affArr = editIncidentInfo.Affiliation
								.split(",");
						for (String str : affArr)
						{
							listAff.add(str);
						}

						for (final AffiliationTypeInfo info : affiliations)
						{
							CheckDateItemInfo _info = new CheckDateItemInfo();
							String strId = "";
							if (!listAff.isEmpty())
							{
								strId = Utilities.check(listAff,
										new Predicate<String>() {
											@Override
											public boolean Predicate(String t)
											{
												if (String.valueOf(info.Id)
														.equals(t))
													return true;
												else
													return false;
											}
										});
								if (strId == null)
									strId = "";
							}

							_info.Id = info.Id;
							_info.IsChecked = (strId.equals("") ? false : true);
							_info.DateTime = "";
							_info.Title = info.Affiliation;

							list.add(_info);
						}
					}
					else
					{
						for (AffiliationTypeInfo info : affiliations)
						{
							CheckDateItemInfo _info = new CheckDateItemInfo();
							_info.Id = info.Id;
							_info.IsChecked = false;
							_info.DateTime = "";
							_info.Title = info.Affiliation;

							list.add(_info);
						}
					}

					intent.putExtra("chk_date_list_type",
							CheckDateListDialog.CHECKBOX_ITEM);
					intent.putExtra("chk_date_list_title",
							getString(R.string.select_affilications));
					intent.putExtra("chk_date_list_data", list);
					intent.putExtra("chk_date_list_result_code",
							AffilicationResultCode);

					startActivityForResult(intent, REQUEST_CODE);
				}
				else if (view == btn_incident_othercnd)
				{
					Intent intent = new Intent(ActivityIncident.this,
							IncidentOtherCnd.class);

					intent.putExtra("ActivityId", ActivityId);
					intent.putExtra("IncidentId", IncidentId);

					startActivity(intent);
				}
				else if (view == btn_incident_setemail)
				{
					Intent intent = new Intent(ActivityIncident.this,
							EmailActivity.class);
					Object obj = spinner_activity_location.getSelectedItem();
					if (obj != null)
					{
						String LocationId = ((SpinnerData) obj).getValue()
								.toString();
						intent.putExtra("LocationId", LocationId);
						startActivity(intent);
					}
					else
					{
						btn_incident_setemail
								.setError("Please select a location");
					}
				}
				else if (view == btn_incident_review)
				{
					Intent intent = new Intent(ActivityIncident.this,
							IncidentReviewDialog.class);
					if (OperateType == ActionType.Edit)
					{
						intent.putExtra("review", reviewMaps);
					}
					startActivityForResult(intent, REQUEST_CODE);
				}
				else if (view == btn_incident_who_involved_details)
				{
					whoinvolvedHandler = getHandler();
					loadWhoInvolvedDetails(whoinvolvedHandler);
				}
				else if (view == btn_activity_photo)
				{
					Intent intent = new Intent(ActivityIncident.this,
							CameraPhoto.class);
					intent.putExtra("ActivityId", ActivityId);
					intent.putExtra("type", ActionType.Add);
					startActivity(intent);
				}
				else if (view == btn_activity_photo_list)
				{
					Intent intent = new Intent(ActivityIncident.this,
							AttachListActivity.class);
					intent.putExtra("activityid", ActivityId);
					startActivity(intent);
				}
			}
		}
	}

	private void loading()
	{
		if (loadDlg != null)
		{
			loadDlg.show();
		}
		else
		{
			loadDlg = new LoadingDialog(this);
			loadDlg.show();
		}
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
