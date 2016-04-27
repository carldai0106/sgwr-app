package com.sgwr.app;

import java.util.List;
import java.util.UUID;
import org.apache.http.client.CookieStore;
import com.sgwr.app.bean.ClientInfo;
import com.sgwr.app.bean.LocationInfo;
import com.sgwr.app.bean.UserGroupRightInfo;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.utils.CyptoUtils;
import com.sgwr.app.utils.StringUtils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppContext extends Application {

	/**
	 * 定义 一个变量 保存用户组权限列表
	 */
	public List<UserGroupRightInfo> UserGroupRightList;

	/**
	 * 定义一个变量保存用户是否需要选择location
	 */
	public boolean HasSetClientAndLocation;
	public UserInfo CurrentUser;
	public ClientInfo CurrentClient;
	public LocationInfo CurrentLocation;	
	public CookieStore CurrentCookie;
	
	@Override
	public void onCreate()
	{		
		super.onCreate();
        //注册App异常崩溃处理器
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());		
	}
	
	public PackageInfo getPackageInfo()
	{
		PackageInfo info = null;
		try
		{
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	public String getAppId()
	{
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if (StringUtils.isEmpty(uniqueID))
		{
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}

	public boolean isNetworkConnected()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	public void setProperty(String key, String value)
	{
		AppConfig.getAppConfig(this).set(key, value);
	}

	public String getProperty(String key)
	{
		return AppConfig.getAppConfig(this).get(key);
	}

	public String getValue(String key)
	{
		SharedPreferences sp = this.getSharedPreferences("BCSTRACKER",
				MODE_PRIVATE);
		String strVal = sp.getString(key, null);
		if(strVal == null)
			return null;
		
		return CyptoUtils.decode(AppConfig.CYPTO_KEY, strVal);
	}

	public boolean setValue(String key, String value)
	{
		SharedPreferences sp = this.getSharedPreferences("BCSTRACKER",
				MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, CyptoUtils.encode(AppConfig.CYPTO_KEY, value));
		return editor.commit();
	}
}
