package com.sgwr.app.bean;

import java.io.Serializable;

import com.sgwr.app.AppContext;
import com.sgwr.app.utils.StringUtils;

public class URLInfo implements Serializable {
	public final static String HOST = "www.bcstracker.com";
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";

	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";

	private final static boolean isDebug = false;

	public static String getHost(AppContext context)
	{
		if (context == null || isDebug)
		{
			return "192.168.1.113:4939";
		}
		else
		{
			String hostName = context.getValue("HostName");
			if (!StringUtils.isEmpty(hostName))
				return hostName;
			else
				return "192.168.1.113:4939";
		}
	}

	public static String getURL(AppContext context)
	{
		return HTTP + getHost(context);
	}

	public static String getLoginURL(AppContext context)
	{
		// http://localhost:4939/Handler/UserHandler.ashx
		return HTTP + getHost(context) + URL_SPLITTER
				+ "Handler/UserHandler.ashx";
	}

	public static String getClientURL(AppContext context)
	{
		return HTTP + getHost(context) + URL_SPLITTER
				+ "Handler/ClientHandler.ashx";
	}

	public static String getActivityURL(AppContext context)
	{
		return HTTP + getHost(context) + URL_SPLITTER
				+ "Handler/ActivityHandler.ashx";
	}

	public static String getUserLocationURL(AppContext context)
	{
		return HTTP + getHost(context) + URL_SPLITTER
				+ "Handler/LocationHandler.ashx";
	}

	public static String getCommonURL(AppContext context)
	{
		return HTTP + getHost(context) + URL_SPLITTER
				+ "Handler/CommonHandler.ashx";
	}

	public static String getIncidentURL(AppContext context)
	{
		return HTTP + getHost(context) + URL_SPLITTER
				+ "Handler/IncidentHandler.ashx";
	}

	public static String getUploadURL(AppContext context)
	{
		return HTTP + getHost(context) + URL_SPLITTER
				+ "Handler/UploadHandler.ashx";
	}

	public static String getCheckUpdateURL(AppContext context)
	{
		return HTTP + "mobile.bcstracker.com:8003" + URL_SPLITTER
				+ "MobileAppVersion.html";
	}
}
