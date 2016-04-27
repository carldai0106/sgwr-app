package com.sgwr.app.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.bean.ConditionTypeInfo;
import com.sgwr.app.bean.IncidentConditionInfo;
import com.sgwr.app.bean.IncidentContactInfo;
import com.sgwr.app.bean.IncidentInfo;
import com.sgwr.app.bean.IncidentNotifyInfo;
import com.sgwr.app.bean.IncidentReplyInfo;
import com.sgwr.app.bean.Result;
import com.sgwr.app.bean.URLInfo;
import com.sgwr.app.utils.HttpUtils;
import com.sgwr.app.utils.Utilities;

public class IncidentService extends BaseService {
	public static long getNewId(AppContext context, String activityId,
			String userName, String password) throws AppException
	{
		long id = 0;
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "new_id");
			params.put("activityid", activityId);
			params.put("username", userName);
			params.put("password", password);

			// HttpEntity entity = HttpUtility.sendHttpPostRequest(context, url,
			// params, null, Charset.forName("UTF-8"));
			byte[] bytes = HttpUtils.requestHttpPost(context, url, params, null,
					Charset.forName("UTF-8"));

			id = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<Long>>() {
					});

		}

		return id;
	}

	public static IncidentInfo getIncident(AppContext context, String ActivityId)
			throws AppException
	{
		IncidentInfo info = null;
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident");
			params.put("activityid", ActivityId);
		
			byte[] bytes = HttpUtils.requestHttpPost(context, url, params, null,
					Charset.forName("UTF-8"));

			info = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<IncidentInfo>>() {
					});

		}

		return info;
	}

	public static Map<String, String> getIncidentOtherInitInfo(
			AppContext context) throws AppException
	{
		Map<String, String> maps = new HashMap<String, String>();
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_init_otherinfo");

		
			byte[] bytes = HttpUtils.requestHttpPost(context, url, params, null,
					Charset.forName("UTF-8"));

			maps = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<Map<String, String>>>() {
					});

		}

		return maps;
	}

	public static ArrayList<ConditionTypeInfo> getConditionTypes(
			AppContext context) throws AppException
	{

		ArrayList<ConditionTypeInfo> list = null;
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "condition_types");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");			
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			list = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<ConditionTypeInfo>>>() {
					});

		}
		else
			list = new ArrayList<ConditionTypeInfo>();

		return list;
	}

	public static List<IncidentConditionInfo> getIncidentConditionList(
			AppContext context, String ActivityId) throws AppException
	{

		List<IncidentConditionInfo> list = new ArrayList<IncidentConditionInfo>();
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_condition_list");
			params.put("activityid", ActivityId);

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			list = getResult(
					HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<IncidentConditionInfo>>>() {
					});
		}

		return list;
	}

	public static List<IncidentNotifyInfo> getIncidentNotifies(
			AppContext context, String ActivityId) throws AppException
	{

		List<IncidentNotifyInfo> list = new ArrayList<IncidentNotifyInfo>();
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_notifies");
			params.put("activityid", ActivityId);

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			list = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<IncidentNotifyInfo>>>() {
					});
		}

		return list;
	}

	public static List<IncidentReplyInfo> getIncidenReplies(AppContext context,
			String ActivityId) throws AppException
	{

		List<IncidentReplyInfo> list = new ArrayList<IncidentReplyInfo>();
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_replies");
			params.put("activityid", ActivityId);
			
			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			list = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<IncidentReplyInfo>>>() {
					});
		}

		return list;
	}

	public static HashMap<String, String> getWhoInvolvedDetails(
			AppContext context, String ActivityId) throws AppException
	{
		HashMap<String, String> maps = new HashMap<String, String>();
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "who_involved_details");
			params.put("activityid", ActivityId);

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			maps = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<HashMap<String, String>>>() {
					});
		}

		return maps;
	}

	public static List<IncidentContactInfo> getIncidentContactList(
			AppContext context, String ActivityId) throws AppException
	{
		List<IncidentContactInfo> list = new ArrayList<IncidentContactInfo>();
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "incident_contact_list");
			params.put("activityid", ActivityId);

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			list = getResult(
					HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<IncidentContactInfo>>>() {
					});
		}

		return list;
	}

	public static String operateIncidentCondition(AppContext context,
			String subType, String json) throws AppException
	{

		String strMsg = "";
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			strMsg = quickOperate(context, url, "operate_incident_condition",
					subType, json);
		}

		return strMsg;
	}

	public static String operateActivityIncident(AppContext context,
			String subType, String json) throws AppException
	{

		String strMsg = "";
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			strMsg = quickOperate(context, url, "operate_activity_incident",
					subType, json);
		}

		return strMsg;
	}

	public static String operateIncidentContact(AppContext context,
			String subType, String json) throws AppException
	{

		String strMsg = "";
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			strMsg = quickOperate(context, url, "operate_incident_contact",
					subType, json);
		}

		return strMsg;
	}

	public static String operateAddressBook(AppContext context, String subType,
			String json) throws AppException
	{

		String strMsg = "";
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			strMsg = quickOperate(context, url, "operate_address_book",
					subType, json);
		}

		return strMsg;
	}

	public static String operateIncidentNotify(AppContext context,
			String incidentId, String subType, String json) throws AppException
	{

		String strMsg = "";
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("incidentid", incidentId);
			strMsg = quickOperate(context, url, "operate_incident_notify",
					subType, json, maps);
		}

		return strMsg;
	}

	public static String operateEmergencyReplies(AppContext context,
			String incidentId, String subType, String json) throws AppException
	{

		String strMsg = "";
		if (Utilities.isNetworkConnected(context))
		{
			String url = URLInfo.getIncidentURL(context);
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("incidentid", incidentId);
			strMsg = quickOperate(context, url, "operate_incident_replies",
					subType, json, maps);
		}

		return strMsg;
	}

}
