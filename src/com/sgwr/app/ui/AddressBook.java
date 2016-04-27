package com.sgwr.app.ui;

import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.bean.AddressBookInfo;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.service.IncidentService;
import com.sgwr.app.utils.DateTimeUtils;
import com.sgwr.app.utils.JsonUtils;
import com.sgwr.app.utils.StringUtils;
import com.sgwr.app.utils.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddressBook extends Activity {

	// header
	private ImageButton inner_header_back;
	private TextView inner_header_title;
	private ProgressBar inner_header_progress;
	// footer
	private RadioButton inner_foot_radio_save;

	private EditText edit_addrbook_firstname;
	private EditText edit_addrbook_lastname;
	private EditText edit_addrbook_company;
	private EditText edit_addrbook_title;
	private EditText edit_addrbook_email;
	private EditText edit_addrbook_state;
	private EditText edit_addrbook_city;
	private EditText edit_addrbook_zipcode;
	private EditText edit_addrbook_hometel;
	private EditText edit_addrbook_mobile;
	private EditText edit_addrbook_worktel;
	private EditText edit_addrbook_othertel;
	private EditText edit_addrbook_addr1;
	private EditText edit_addrbook_addr2;
	private EditText edit_addrbook_otherinfo;
	private CheckBox chk_addrbook_sendemail;

	private AppContext context;
	private String ClientId;
	private String LocationId;
	private String ActionType;
	private AddressBookInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.frm_addressbook);

		context = (AppContext) getApplication();

		Intent intent = getIntent();
		ActionType = intent.getStringExtra("type");

		inner_header_back = (ImageButton) findViewById(R.id.inner_header_back);
		inner_header_title = (TextView) findViewById(R.id.inner_header_title);
		inner_header_progress = (ProgressBar) findViewById(R.id.inner_header_progress);
		inner_foot_radio_save = (RadioButton) findViewById(R.id.inner_foot_radio_save);
		edit_addrbook_firstname = (EditText) findViewById(R.id.edit_addrbook_firstname);
		edit_addrbook_lastname = (EditText) findViewById(R.id.edit_addrbook_lastname);
		edit_addrbook_company = (EditText) findViewById(R.id.edit_addrbook_company);
		edit_addrbook_title = (EditText) findViewById(R.id.edit_addrbook_title);
		edit_addrbook_email = (EditText) findViewById(R.id.edit_addrbook_email);
		edit_addrbook_state = (EditText) findViewById(R.id.edit_addrbook_state);
		edit_addrbook_city = (EditText) findViewById(R.id.edit_addrbook_city);
		edit_addrbook_zipcode = (EditText) findViewById(R.id.edit_addrbook_zipcode);
		edit_addrbook_hometel = (EditText) findViewById(R.id.edit_addrbook_hometel);
		edit_addrbook_mobile = (EditText) findViewById(R.id.edit_addrbook_mobile);
		edit_addrbook_worktel = (EditText) findViewById(R.id.edit_addrbook_worktel);
		edit_addrbook_othertel = (EditText) findViewById(R.id.edit_addrbook_othertel);
		edit_addrbook_addr1 = (EditText) findViewById(R.id.edit_addrbook_addr1);
		edit_addrbook_addr2 = (EditText) findViewById(R.id.edit_addrbook_addr2);
		edit_addrbook_otherinfo = (EditText) findViewById(R.id.edit_addrbook_otherinfo);
		chk_addrbook_sendemail = (CheckBox) findViewById(R.id.chk_addrbook_sendemail);

		inner_header_back.setOnClickListener(new ClickEvent());
		inner_foot_radio_save.setOnClickListener(new ClickEvent());

		if (ActionType.equals("add"))
		{
			inner_header_title.setText("Add Address Book");
			ClientId = intent.getStringExtra("ClientId");
			LocationId = intent.getStringExtra("LocationId");
			info = new AddressBookInfo();
		}
		else if (ActionType.equals("edit"))
		{
			inner_header_title.setText("Edit Address Book");
			info = (AddressBookInfo) intent.getSerializableExtra("AddressBook");
			edit();
		}
	}

	public void edit()
	{
		if (info != null)
		{
			edit_addrbook_firstname.setText(info.FirstName);
			edit_addrbook_lastname.setText(info.LastName);
			edit_addrbook_company.setText(info.Company);
			edit_addrbook_title.setText(info.Title);
			edit_addrbook_email.setText(info.Email);
			edit_addrbook_state.setText(info.State);
			edit_addrbook_city.setText(info.City);
			edit_addrbook_zipcode.setText(info.ZipCode);
			edit_addrbook_hometel.setText(info.HomeTel);
			edit_addrbook_mobile.setText(info.Mobile);
			edit_addrbook_worktel.setText(info.WorkTel);
			edit_addrbook_othertel.setText(info.OtherTel);
			edit_addrbook_addr1.setText(info.Addr1);
			edit_addrbook_addr2.setText(info.Addr2);
			edit_addrbook_otherinfo.setText(info.Note);
			chk_addrbook_sendemail.setChecked(info.IsAccept);
		}
	}

	public class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v)
		{
			if (v == inner_foot_radio_save)
			{				
				// TODO Auto-generated method stub
				String firstName = Utilities
						.getEditText(edit_addrbook_firstname);
				String lastName = Utilities.getEditText(edit_addrbook_lastname);
				String email = Utilities.getEditText(edit_addrbook_email);
				if (StringUtils.isEmpty(firstName))
				{
					edit_addrbook_firstname.setError("Please enter first name");
					return;
				}
				else if (StringUtils.isEmpty(lastName))
				{
					edit_addrbook_lastname.setError("Please enter last name");
					return;
				}
				else if (!StringUtils.isEmail(email))
				{
					edit_addrbook_email.setError("This is not a valid email.");
					return;
				}
				else
				{
					info.Addr1 = Utilities.getEditText(edit_addrbook_addr1);
					info.Addr2 = Utilities.getEditText(edit_addrbook_addr2);
					info.City = Utilities.getEditText(edit_addrbook_city);

					info.Company = Utilities.getEditText(edit_addrbook_company);
					info.Email = email;
					info.FirstName = firstName;
					info.LastName = lastName;

					info.HomeTel = Utilities.getEditText(edit_addrbook_hometel);
					info.IsAccept = chk_addrbook_sendemail.isChecked();
					info.Mobile = Utilities.getEditText(edit_addrbook_mobile);
					info.Note = Utilities.getEditText(edit_addrbook_otherinfo);
					info.OtherTel = Utilities
							.getEditText(edit_addrbook_othertel);
					info.State = Utilities.getEditText(edit_addrbook_state);
					info.Title = Utilities.getEditText(edit_addrbook_title);
					info.TypeName = "Location";
					info.WorkTel = Utilities.getEditText(edit_addrbook_worktel);
					info.ZipCode = Utilities.getEditText(edit_addrbook_zipcode);

					if (ActionType.equals("add"))
					{
						info.ClientId = ClientId;
						info.ForeignId = LocationId;
						String strJson = JsonUtils.SerializeObject(info);
						Submit("add", strJson);
					}
					else if (ActionType.equals("edit"))
					{
						info.ChangeBy = context.CurrentUser.UserName;
						info.ChangeTime = DateTimeUtils.getCurrentUtcDate();

						String strJson = JsonUtils.SerializeObject(info);
						Submit("edit", strJson);
					}
				}
			}
			else if (v == inner_header_back)
			{
				finish();
			}
		}
	}

	private void Submit(final String actionType, final String strJson)
	{
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					setResult(2);
					finish();
				}
				else
				{
					((AppException) msg.obj).makeToast(AddressBook.this);
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
					String strMsg = IncidentService.operateAddressBook(context,
							actionType, strJson);

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
}
