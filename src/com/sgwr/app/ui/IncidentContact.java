package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.sgwr.app.ActionType;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ViewPagerAdapter;
import com.sgwr.app.bean.ContactTypeInfo;
import com.sgwr.app.bean.IncidentContactInfo;
import com.sgwr.app.bean.SpinnerData;
import com.sgwr.app.service.CommonService;
import com.sgwr.app.service.IncidentService;
import com.sgwr.app.utils.DateTimeUtils;
import com.sgwr.app.utils.JsonUtils;
import com.sgwr.app.utils.StringUtils;
import com.sgwr.app.utils.UIHelper;
import com.sgwr.app.utils.Utilities;
import com.sgwr.app.widget.LoadingDialog;
import com.sgwr.app.widget.SgwrViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class IncidentContact extends Activity {

	private View view_contatct_info;
	private View view_vehicle_info;
	private List<View> viewList = new ArrayList<View>();
	private List<String> viewTitle = new ArrayList<String>();
	private SgwrViewPager viewPager;

	// header
	private ImageButton inner_header_back;
	private TextView inner_header_title;
	private ProgressBar inner_header_progress;
	// footer
	private RadioButton inner_foot_radio_prev;
	private RadioButton inner_foot_radio_next;
	private RadioButton inner_foot_radio_save;

	// contact info
	private Spinner spinner_contact_type;
	private RadioGroup radgrp_contact_gender;
	private EditText edit_contact_firstname;
	private EditText edit_contact_midname;
	private EditText edit_contact_lastname;
	private EditText edit_contact_addr1;
	private EditText edit_contact_addr2;
	private EditText edit_contact_city;
	private EditText edit_contact_provice;
	private EditText edit_contact_zipcode;
	private EditText edit_contact_hometel;
	private EditText edit_contact_worktel;
	private EditText edit_contact_celltel;
	private EditText edit_contact_othertel;
	private EditText edit_contact_height;
	private EditText edit_contact_weight;
	private Spinner spinner_contact_id1type;
	private EditText edit_contact_id1;
	private Spinner spinner_contact_id2type;
	private EditText edit_contact_id2;
	private Spinner spinner_contact_id3type;
	private EditText edit_contact_id3;
	private EditText edit_contact_otherinfo;

	// vehicle info
	private EditText edit_contact_make;
	private EditText edit_contact_model;
	private EditText edit_contact_year;
	private EditText edit_contact_license;
	private EditText edit_contact_color;
	private EditText edit_contact_insurance;
	private EditText edit_contact_policy;

	private List<SpinnerData> IDTypes;
	private List<SpinnerData> contactTypes = new ArrayList<SpinnerData>();
	private ArrayAdapter<SpinnerData> contactAdapter;

	private int currentViewPager = 0;
	private AppContext context;

	private int ResultCode = 0;
	private long ActivityId = 0;
	private long IncidentId = 0;
	private String ContactTypeId = "";

	private ActionType OperateType;
	private LoadingDialog loadDlg = null;

	private IncidentContactInfo contactInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_incident_contact);

		context = (AppContext) getApplication();

		Intent intent = getIntent();
		OperateType = (ActionType) intent.getSerializableExtra("type");
		ResultCode = intent.getIntExtra("ResultCode", 0);
		ActivityId = intent.getLongExtra("ActivityId", 0);
		IncidentId = intent.getLongExtra("IncidentId", 0);
		if (OperateType == ActionType.Edit)
		{
			contactInfo = (IncidentContactInfo) intent
					.getSerializableExtra("IncidentContactInfo");
		}
		ContactTypeId = intent.getStringExtra("ContactTypeId");

		initMain();
		initView();
	}

	private void initMain()
	{
		// header
		inner_header_back = (ImageButton) findViewById(R.id.inner_header_back);
		inner_header_title = (TextView) findViewById(R.id.inner_header_title);
		inner_header_progress = (ProgressBar) findViewById(R.id.inner_header_progress);
		// footer
		inner_foot_radio_prev = (RadioButton) findViewById(R.id.inner_foot_radio_prev);
		inner_foot_radio_next = (RadioButton) findViewById(R.id.inner_foot_radio_next);
		inner_foot_radio_save = (RadioButton) findViewById(R.id.inner_foot_radio_save);
		inner_header_back.setOnClickListener(new ClickActionEvent());
		inner_foot_radio_prev.setOnClickListener(new ClickActionEvent());
		inner_foot_radio_next.setOnClickListener(new ClickActionEvent());
		inner_foot_radio_save.setOnClickListener(new ClickActionEvent());

		viewPager = (SgwrViewPager) findViewById(R.id.incident_contact_viewpager);

		this.getLayoutInflater();
		LayoutInflater layout = LayoutInflater.from(this);
		view_contatct_info = layout.inflate(R.layout.frm_contact, null);
		view_vehicle_info = layout.inflate(R.layout.frm_vehicle, null);

		viewList.add(view_contatct_info);
		viewTitle.add("Contact Information");
		viewList.add(view_vehicle_info);
		viewTitle.add("Vehicle Information");

		inner_header_title.setText(viewTitle.get(currentViewPager));
		ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
		viewPager.setAdapter(adapter);
	}

	private void initView()
	{
		// contact view
		spinner_contact_type = (Spinner) view_contatct_info
				.findViewById(R.id.spinner_contact_type);
		radgrp_contact_gender = (RadioGroup) view_contatct_info
				.findViewById(R.id.radgrp_contact_gender);
		edit_contact_firstname = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_firstname);
		edit_contact_midname = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_midname);
		edit_contact_lastname = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_lastname);
		edit_contact_addr1 = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_addr1);
		edit_contact_addr2 = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_addr2);
		edit_contact_city = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_city);
		edit_contact_provice = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_provice);
		edit_contact_zipcode = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_zipcode);
		edit_contact_hometel = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_hometel);
		edit_contact_worktel = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_worktel);
		edit_contact_celltel = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_celltel);
		edit_contact_othertel = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_othertel);
		edit_contact_height = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_height);
		edit_contact_weight = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_weight);
		spinner_contact_id1type = (Spinner) view_contatct_info
				.findViewById(R.id.spinner_contact_id1type);
		edit_contact_id1 = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_id1);
		spinner_contact_id2type = (Spinner) view_contatct_info
				.findViewById(R.id.spinner_contact_id2type);
		edit_contact_id2 = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_id2);
		spinner_contact_id3type = (Spinner) view_contatct_info
				.findViewById(R.id.spinner_contact_id3type);
		edit_contact_id3 = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_id3);
		edit_contact_otherinfo = (EditText) view_contatct_info
				.findViewById(R.id.edit_contact_otherinfo);

		// vehicle view
		edit_contact_make = (EditText) view_vehicle_info
				.findViewById(R.id.edit_contact_make);
		edit_contact_model = (EditText) view_vehicle_info
				.findViewById(R.id.edit_contact_model);
		edit_contact_year = (EditText) view_vehicle_info
				.findViewById(R.id.edit_contact_year);
		edit_contact_license = (EditText) view_vehicle_info
				.findViewById(R.id.edit_contact_license);
		edit_contact_color = (EditText) view_vehicle_info
				.findViewById(R.id.edit_contact_color);
		edit_contact_insurance = (EditText) view_vehicle_info
				.findViewById(R.id.edit_contact_insurance);
		edit_contact_policy = (EditText) view_vehicle_info
				.findViewById(R.id.edit_contact_policy);

		contactAdapter = new ArrayAdapter<SpinnerData>(this,
				R.layout.item_spinner, contactTypes);

		contactAdapter
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		spinner_contact_type.setAdapter(contactAdapter);

		IDTypes = new ArrayList<SpinnerData>();
		IDTypes.add(new SpinnerData("SSN", "SSN"));
		IDTypes.add(new SpinnerData("Badge Number", "Badge Number"));
		IDTypes.add(new SpinnerData("Employee Number", "Employee Number"));

		ArrayAdapter<SpinnerData> adapterIDType = new ArrayAdapter<SpinnerData>(
				this, R.layout.item_spinner, IDTypes);
		adapterIDType
				.setDropDownViewResource(android.R.layout.select_dialog_item);

		spinner_contact_id1type.setAdapter(adapterIDType);
		spinner_contact_id2type.setAdapter(adapterIDType);
		spinner_contact_id3type.setAdapter(adapterIDType);

		if (contactTypes.isEmpty())
		{
			loadContactType();
		}
	}

	private void setModel()
	{
		edit_contact_firstname.setText(contactInfo.FirstName);
		edit_contact_midname.setText(contactInfo.MiddleName);
		edit_contact_lastname.setText(contactInfo.LastName);
		edit_contact_addr1.setText(contactInfo.Addr1);
		edit_contact_addr2.setText(contactInfo.Addr2);
		edit_contact_city.setText(contactInfo.City);
		edit_contact_provice.setText(contactInfo.State);
		edit_contact_zipcode.setText(contactInfo.Zip);
		edit_contact_hometel.setText(contactInfo.HomeTel);
		edit_contact_worktel.setText(contactInfo.WorkTel);
		edit_contact_celltel.setText(contactInfo.Mobile);
		edit_contact_othertel.setText(contactInfo.OtherTel);
		edit_contact_height.setText(contactInfo.Height);
		edit_contact_weight.setText(contactInfo.Weight);
		edit_contact_id1.setText(contactInfo.ID1);
		edit_contact_id2.setText(contactInfo.ID2);
		edit_contact_id3.setText(contactInfo.ID3);
		edit_contact_otherinfo.setText(contactInfo.Note);

		edit_contact_make.setText(contactInfo.Make);
		edit_contact_model.setText(contactInfo.Model);
		edit_contact_year.setText(contactInfo.Year);
		edit_contact_license.setText(contactInfo.License);
		edit_contact_color.setText(contactInfo.Color);
		edit_contact_insurance.setText(contactInfo.InsuranceCo);
		edit_contact_policy.setText(contactInfo.Policy);

		RadioButton radbtn1 = (RadioButton) radgrp_contact_gender.getChildAt(0);
		RadioButton radbtn2 = (RadioButton) radgrp_contact_gender.getChildAt(1);
		if (contactInfo.Gender.equals("Male"))
			radbtn1.setChecked(true);
		else
			radbtn2.setChecked(true);

		spinner_contact_type = (Spinner) view_contatct_info
				.findViewById(R.id.spinner_contact_type);

		UIHelper.setSelection(spinner_contact_type, contactTypes,
				contactInfo.ContactID);
		UIHelper.setSelection(spinner_contact_id1type, IDTypes,
				contactInfo.ID1Type);
		UIHelper.setSelection(spinner_contact_id2type, IDTypes,
				contactInfo.ID2Type);
		UIHelper.setSelection(spinner_contact_id3type, IDTypes,
				contactInfo.ID3Type);
	}

	private class ClickActionEvent implements OnClickListener {
		@Override
		public void onClick(View view)
		{
			// TODO Auto-generated method stub
			if (view instanceof RadioButton)
			{
				inner_foot_radio_next.setChecked(false);
				inner_foot_radio_prev.setChecked(false);
				inner_foot_radio_save.setChecked(false);

				if (view == inner_foot_radio_next)
				{
					inner_foot_radio_next.setChecked(true);
					if (checkInput())
					{
						currentViewPager++;
						if (currentViewPager > viewList.size() - 1)
							currentViewPager = viewList.size() - 1;

						inner_header_title.setText(viewTitle
								.get(currentViewPager));
						viewPager.setCurrentItem(currentViewPager, true);

					}
				}
				else if (view == inner_foot_radio_prev)
				{
					currentViewPager--;
					if (currentViewPager < 0)
						currentViewPager = 0;

					inner_header_title.setText(viewTitle.get(currentViewPager));
					viewPager.setCurrentItem(currentViewPager, true);
					inner_foot_radio_prev.setChecked(true);
				}
				else if (view == inner_foot_radio_save)
				{
					inner_foot_radio_save.setChecked(true);
					if (checkInput())
					{
						IncidentContactInfo info = getModel();
						String json = JsonUtils.SerializeObject(info);

						String subType = "";
						if (OperateType == ActionType.Add)
							subType = "add";
						else
							subType = "edit";

						operate(subType, json);
					}
				}
			}
			else if (view instanceof ImageView)
			{
				if (view == inner_header_back)
				{
					finish();
				}
			}
		}
	}

	private boolean checkInput()
	{
		boolean flag = true;
		SpinnerData contactType = (SpinnerData) spinner_contact_type
				.getSelectedItem();
		String firstName = edit_contact_firstname.getText().toString();
		String lastName = edit_contact_lastname.getText().toString();
		if (Utilities.isNullSpinnerData(contactType))
		{
			Toast.makeText(this, "Please select a contact type.",
					Toast.LENGTH_SHORT).show();
			flag = false;
		}
		else if (StringUtils.isEmpty(firstName))
		{
			edit_contact_firstname.setError("Please enter first name");
			flag = false;
		}
		else if (StringUtils.isEmpty(lastName))
		{
			edit_contact_lastname.setError("Please enter last name");
			flag = false;
		}

		return flag;
	}

	private IncidentContactInfo getModel()
	{
		SpinnerData contactType = (SpinnerData) spinner_contact_type
				.getSelectedItem();
		String firstName = edit_contact_firstname.getText().toString();
		String lastName = edit_contact_lastname.getText().toString();

		IncidentContactInfo info = null;
		if (OperateType == ActionType.Add)
		{
			info = new IncidentContactInfo();
		}
		else
		{
			info = contactInfo;
			info.ChangeBy = context.CurrentUser.UserName;
			info.ChangeTime = DateTimeUtils.getCurrentUtcDate();
		}

		info.ActivityId = String.valueOf(ActivityId);
		info.IncidentId = String.valueOf(IncidentId);

		info.Addr1 = Utilities.getEditText(edit_contact_addr1);
		info.Addr2 = Utilities.getEditText(edit_contact_addr2);

		info.City = Utilities.getEditText(edit_contact_city);
		info.Color = Utilities.getEditText(edit_contact_color);
		info.ContactID = Utilities.Object2Int(contactType.getValue());
		info.ContactType = contactType.getText();

		info.FirstName = firstName;

		int radid = radgrp_contact_gender.getCheckedRadioButtonId();
		if (radid == R.id.rad_contact_male)
		{
			info.Gender = "Male";
		}
		else
		{
			info.Gender = "Female";
		}
		info.Height = Utilities.getEditText(edit_contact_height);
		info.HomeTel = Utilities.getEditText(edit_contact_hometel);

		info.ID1 = Utilities.getEditText(edit_contact_id1);
		info.ID1Type = Utilities.getSpinnerValue(spinner_contact_id1type);

		info.ID2 = Utilities.getEditText(edit_contact_id2);
		info.ID2Type = Utilities.getSpinnerValue(spinner_contact_id2type);

		info.ID3 = Utilities.getEditText(edit_contact_id3);
		info.ID3Type = Utilities.getSpinnerValue(spinner_contact_id3type);

		info.InsuranceCo = Utilities.getEditText(edit_contact_insurance);

		info.LastName = lastName;
		info.License = Utilities.getEditText(edit_contact_license);
		info.Make = Utilities.getEditText(edit_contact_make);
		info.MiddleName = Utilities.getEditText(edit_contact_midname);
		info.Mobile = Utilities.getEditText(edit_contact_celltel);
		info.Model = Utilities.getEditText(edit_contact_model);
		info.Note = Utilities.getEditText(edit_contact_otherinfo);
		info.OtherTel = Utilities.getEditText(edit_contact_othertel);
		info.Policy = Utilities.getEditText(edit_contact_policy);
		info.State = Utilities.getEditText(edit_contact_provice);
		info.Weight = Utilities.getEditText(edit_contact_weight);
		info.WorkTel = Utilities.getEditText(edit_contact_worktel);
		info.Year = Utilities.getEditText(edit_contact_year);
		info.Zip = Utilities.getEditText(edit_contact_zipcode);

		return info;
	}

	private void operate(final String subType, final String json)
	{
		loadDlg = new LoadingDialog(this);
		loadDlg.show();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					loadDlg.dismiss();
					setResult(ResultCode);
					finish();
				}
				else
				{
					((AppException) msg.obj).makeToast(IncidentContact.this);
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
					String strMsg = IncidentService.operateIncidentContact(
							context, subType, json);
					msg.obj = strMsg;
					msg.what = 1;
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

	private void loadContactType()
	{
		loadDlg = new LoadingDialog(this);
		loadDlg.show();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				loadDlg.dismiss();
				if (msg.what == 1)
				{
					List<ContactTypeInfo> list = (List<ContactTypeInfo>) msg.obj;
					contactTypes.clear();
					contactTypes.add(new SpinnerData(
							getString(R.string.please_select_one), ""));
					for (ContactTypeInfo info : list)
					{
						contactTypes.add(new SpinnerData(info.ContactType,
								info.ID));
					}

					contactAdapter.notifyDataSetChanged();

					if (OperateType == ActionType.Edit)
					{
						setModel();
					}
					else
					{
						if (!StringUtils.isEmpty(ContactTypeId))
						{
							UIHelper.setSelection(spinner_contact_type,
									contactTypes, ContactTypeId);
							spinner_contact_type.setEnabled(false);
						}
					}
				}
				else
				{
					((AppException) msg.obj).makeToast(IncidentContact.this);
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
					msg.what = 1;
					msg.obj = CommonService.getContactTypeList(context);
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
