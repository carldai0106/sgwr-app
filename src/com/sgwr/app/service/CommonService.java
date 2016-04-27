package com.sgwr.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.bean.AddressBookInfo;
import com.sgwr.app.bean.AffiliationTypeInfo;
import com.sgwr.app.bean.ApkUpdateInfo;
import com.sgwr.app.bean.CategoryInfo;
import com.sgwr.app.bean.ContactTypeInfo;
import com.sgwr.app.bean.NotifyTypeInfo;
import com.sgwr.app.bean.Result;
import com.sgwr.app.bean.ShiftTimeInfo;
import com.sgwr.app.bean.StateTypeInfo;
import com.sgwr.app.bean.URLInfo;
import com.sgwr.app.bean.UrgentReplyTypeInfo;
import com.sgwr.app.bean.WorkerPostsInfo;
import com.sgwr.app.utils.DataCacheUtils;
import com.sgwr.app.utils.HttpUtils;
import com.sgwr.app.utils.JsonUtils;
import com.sgwr.app.utils.Utilities;

@SuppressWarnings("unchecked")
public class CommonService extends BaseService {

	public static List<WorkerPostsInfo> getWorkerPostsList(AppContext context,
			String locationId) throws AppException
	{
		String key = "worker_post_" + locationId;
		ArrayList<WorkerPostsInfo> list = (ArrayList<WorkerPostsInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getCommonURL(context);

			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "assigned_post_list");
			params.put("locationid", locationId);

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(rs,
					new TypeReference<Result<ArrayList<WorkerPostsInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);
		}
		else if (list == null)
		{			
			list = new ArrayList<WorkerPostsInfo>();
		}

		return list;
	}

	public static List<ShiftTimeInfo> getShiftTimeList(AppContext context)
			throws AppException
	{
		String key = "shift_time";

		ArrayList<ShiftTimeInfo> list = (ArrayList<ShiftTimeInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getCommonURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "shift_time_list");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(rs,
					new TypeReference<Result<ArrayList<ShiftTimeInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new ArrayList<ShiftTimeInfo>();

		return list;
	}

	public static List<CategoryInfo> getCaregroyList(AppContext context)
			throws AppException
	{
		String key = "incident_category";
		ArrayList<CategoryInfo> list = (ArrayList<CategoryInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getCommonURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_category");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(rs,
					new TypeReference<Result<ArrayList<CategoryInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new ArrayList<CategoryInfo>();

		return list;
	}

	public static List<StateTypeInfo> getIncidentStateList(AppContext context)
			throws AppException
	{
		String key = "incident_status";
		ArrayList<StateTypeInfo> list = (ArrayList<StateTypeInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getCommonURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_status");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(rs,
					new TypeReference<Result<ArrayList<StateTypeInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new ArrayList<StateTypeInfo>();

		return list;
	}

	public static List<NotifyTypeInfo> getNotifyTypeList(AppContext context)
			throws AppException
	{
		String key = "incident_notifies";

		ArrayList<NotifyTypeInfo> list = (ArrayList<NotifyTypeInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getCommonURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_notifies");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(rs,
					new TypeReference<Result<ArrayList<NotifyTypeInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new ArrayList<NotifyTypeInfo>();

		return list;
	}

	public static List<UrgentReplyTypeInfo> getUrgentReplyTypeList(
			AppContext context) throws AppException
	{
		String key = "incident_replies";

		ArrayList<UrgentReplyTypeInfo> list = (ArrayList<UrgentReplyTypeInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getCommonURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_replies");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(
					rs,
					new TypeReference<Result<ArrayList<UrgentReplyTypeInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new ArrayList<UrgentReplyTypeInfo>();

		return list;
	}

	public static List<AffiliationTypeInfo> getAffiliationTypeList(
			AppContext context) throws AppException
	{
		String key = "incident_affiliations";

		ArrayList<AffiliationTypeInfo> list = (ArrayList<AffiliationTypeInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getCommonURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_affiliations");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(
					rs,
					new TypeReference<Result<ArrayList<AffiliationTypeInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new ArrayList<AffiliationTypeInfo>();

		return list;
	}

	public static List<ContactTypeInfo> getContactTypeList(AppContext context)
			throws AppException
	{
		String key = "contact_type";

		ArrayList<ContactTypeInfo> list = (ArrayList<ContactTypeInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getCommonURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "contact_type");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(rs,
					new TypeReference<Result<ArrayList<ContactTypeInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new ArrayList<ContactTypeInfo>();

		return list;
	}

	public static List<AddressBookInfo> getEmailList(AppContext context,
			boolean isRefresh, String clientId, String locationId)
			throws AppException
	{
		String key = "email_" + clientId + "_" + locationId;
		ArrayList<AddressBookInfo> list = (ArrayList<AddressBookInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& ((list == null || list.isEmpty()) || isRefresh))
		{
			String url = URLInfo.getCommonURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "email_list");
			params.put("clientid", clientId);
			params.put("locationid", locationId);

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			// HttpEntity entity = HttpUtility.sendHttpGetRequest(context,
			// reqURL);
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
			String rs = HttpUtils.Byte2Str(bytes);

			list = getResult(rs,
					new TypeReference<Result<ArrayList<AddressBookInfo>>>() {
					});
			
			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new ArrayList<AddressBookInfo>();

		return list;
	}

	public static ApkUpdateInfo checkUpdate(AppContext context)
			throws AppException
	{
		String reqURL = URLInfo.getCheckUpdateURL(context);
		byte[] bytes = HttpUtils.requestHttpGet(context, reqURL + "?_=" + new Date().getTime());
		String rs = HttpUtils.Byte2Str(bytes);
		Log.i("TEST", rs);
		ApkUpdateInfo info = JsonUtils.DeserializeObject(rs,
				new TypeReference<ApkUpdateInfo>() {
				});

		return info;
	}
}
