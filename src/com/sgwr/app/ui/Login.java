package com.sgwr.app.ui;

import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.service.UserService;
import com.sgwr.app.utils.DataCacheUtils;
import com.sgwr.app.utils.StringUtils;
import com.sgwr.app.utils.UpdateManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class Login extends Activity {

	private Button btnLogin;
	private ImageButton btnSetURL;
	private ViewSwitcher viewSwitcher;
	private EditText edtUserName;
	private EditText edtPassword;
	private View loginLoading;
	private AnimationDrawable loadingAnimation;
	private InputMethodManager inputMethodManager;
	private CheckBox chkRememberMe;
	private AppContext context;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frm_login);

		context = (AppContext) this.getApplication();

		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		btnLogin = (Button) this.findViewById(R.id.login_btnlogin);
		btnSetURL = (ImageButton) this.findViewById(R.id.login_btnset);
		edtUserName = (EditText) this.findViewById(R.id.login_account);
		edtPassword = (EditText) this.findViewById(R.id.login_password);
		chkRememberMe = (CheckBox) findViewById(R.id.login_checkbox_rememberMe);

		viewSwitcher = (ViewSwitcher) this
				.findViewById(R.id.login_viewswitcher);

		if (!context.isNetworkConnected())
		{
			Toast.makeText(this,
					"Network connected failed，please check your network.", 4000).show();
		}

		loginLoading = this.findViewById(R.id.login_loading);
		String strRememberMe = context.getValue("RememberMe");
		if (strRememberMe != null && strRememberMe.equals("1"))
		{
			chkRememberMe.setChecked(true);
			String strUsrName = context.getValue("UserName");
			String strPwd = context.getValue("password");
			edtUserName.setText(strUsrName);
			edtPassword.setText(strPwd);
		}

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
						0);

				String account = edtUserName.getText().toString();
				String password = edtPassword.getText().toString();

				boolean isRememberMe = chkRememberMe.isChecked();

				if (StringUtils.isEmpty(account))
				{
					edtUserName
							.setError(getString(R.string.login_username_hint));
					return;
				}
				if (StringUtils.isEmpty(password))
				{
					edtPassword
							.setError(getString(R.string.login_password_hint));
					return;
				}

				loadingAnimation = (AnimationDrawable) loginLoading
						.getBackground();
				loadingAnimation.start();
				viewSwitcher.showNext();

				// context.setValue("UserName", account);
				// context.setValue("password", password);

				login(account, password, isRememberMe);
			}
		});

		btnSetURL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Login.this, ConfigDialog.class);
				startActivity(intent);
			}
		});

		UpdateManager.getUpdateManager().checkAppUpdate(this, false);
	}

	private void login(final String account, final String password,
			final boolean rememberMe)
	{
		DataCacheUtils.clearAppCache(context);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				AppContext context = (AppContext) getApplication();

				// TODO Auto-generated method stub
				if (msg.what == 1)
				{
					Intent intent = new Intent(Login.this, SelectClient.class);
					startActivity(intent);
					finish();
				}
				else if (msg.what == -1)
				{
					viewSwitcher.showPrevious();
					((AppException) msg.obj).makeToast(Login.this);
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
					UserInfo info = UserService.login(context, account,
							password);
					context.CurrentUser = info;
					context.setValue("RememberMe", rememberMe ? "1" : "0");
					context.setValue("UserName", account);
					context.setValue("password", password);

					msg.what = 1;
					msg.obj = info;
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
