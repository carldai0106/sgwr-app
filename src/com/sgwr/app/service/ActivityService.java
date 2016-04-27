package com.sgwr.app.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppConfig;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.bean.ActivityInfo;
import com.sgwr.app.bean.AttachmentInfo;
import com.sgwr.app.bean.ClientInfo;
import com.sgwr.app.bean.LocationInfo;
import com.sgwr.app.bean.Result;
import com.sgwr.app.bean.URLInfo;
import com.sgwr.app.utils.DataCacheUtils;
import com.sgwr.app.utils.HttpUtils;
import com.sgwr.app.utils.Utilities;

public class ActivityService extends BaseService {

	public static List<AttachmentInfo> getAttachments(AppContext context,
			String activityId) throws AppException
	{
		List<AttachmentInfo> list = new ArrayList<AttachmentInfo>();
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getActivityURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "attachments");
			params.put("activityid", activityId);

			String reqUrl = HttpUtils.getUrlByParams(url, params, "utf-8");

			byte[] bytes = HttpUtils.requestHttpGet(context, reqUrl);
			String json = HttpUtils.Byte2Str(bytes);

			list = getResult(json,
					new TypeReference<Result<ArrayList<AttachmentInfo>>>() {
					});
		}

		return list;
	}

	public static String operateActivity(AppContext context, String subType,
			String json) throws AppException
	{
		String strMsg = "";
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getActivityURL(context);
			strMsg = quickOperate(context, url, "operate_activity", subType,
					json);
		}

		return strMsg;
	}

	public static String deleteAttachs(AppContext context, String json)
			throws AppException
	{
		String strMsg = "";
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getActivityURL(context);
			strMsg = quickOperate(context, url, "delete_attachs", "", json);
		}
		return strMsg;
	}

	public static long getNewId(AppContext context, String userName,
			String password) throws AppException
	{
		long id = 0;
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getActivityURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "new_id");
			params.put("username", userName);
			params.put("password", password);

			byte[] bytes = HttpUtils.requestHttpPost(context, url, params, null,
					Charset.forName("UTF-8"));

			String strRs = HttpUtils.Byte2Str(bytes);
			id = getResult(strRs, new TypeReference<Result<Long>>() {
			});
		}

		return id;
	}

	public static List<ActivityInfo> getActivityList(AppContext context,
			String category, int pageIndex, String status, boolean isRefresh)
			throws AppException
	{
		String groupName = context.CurrentUser.NT_GroupName;
		ClientInfo client = context.CurrentClient;
		String clientid = client != null ? String.valueOf(client.Id) : "";
		LocationInfo location = context.CurrentLocation;
		String locationid = location != null ? String.valueOf(location.Id) : "";

		String key = "activitylist_" + category + "_" + status + "_" + clientid
				+ "_" + locationid + "_" + pageIndex;

		ArrayList<ActivityInfo> list = null;

		ArrayList<ActivityInfo> activityList = (ArrayList<ActivityInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (activityList == null || activityList.isEmpty() || isRefresh))
		{
			String clientURL = URLInfo.getActivityURL(context);

			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "list");
			params.put("groupname", groupName);
			params.put("category", category);
			params.put("status", status);
			params.put("clientid", clientid);
			params.put("locationid", locationid);
			params.put("pageindex", String.valueOf(pageIndex));
			params.put("pagesize", String.valueOf(AppConfig.PAGE_SIZE));

			String reqURL = HttpUtils
					.getUrlByParams(clientURL, params, "utf-8");

			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			list = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<ActivityInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);
		}
		else
		{
			list = activityList;
			if (list == null)
				list = new ArrayList<ActivityInfo>();
		}

		return list;
	}
}
