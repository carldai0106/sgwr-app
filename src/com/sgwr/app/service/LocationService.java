package com.sgwr.app.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.bean.AreaInfo;
import com.sgwr.app.bean.LocationInfo;
import com.sgwr.app.bean.Result;
import com.sgwr.app.bean.URLInfo;
import com.sgwr.app.bean.ZoneInfo;
import com.sgwr.app.utils.DataCacheUtils;
import com.sgwr.app.utils.HttpUtils;
import com.sgwr.app.utils.Utilities;

@SuppressWarnings("unchecked")
public class LocationService extends BaseService {

	public static HashMap<String, String> getLocationAndShiftTime(
			AppContext context) throws AppException
	{
		long userid = context.CurrentUser.ID;
		long clientid = context.CurrentClient.Id;

		String key = "location_shittime_" + userid + "_" + clientid;

		HashMap<String, String> list = (HashMap<String, String>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getUserLocationURL(context);

			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "location_shifttime");
			params.put("flag", context.HasSetClientAndLocation ? "0" : "1");
			params.put("userid", userid + "");
			params.put("clientid", clientid + "");

			byte[] bytes = HttpUtils.requestHttpPost(context, url, params,
					null, Charset.forName("utf-8"));
			list = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<HashMap<String, String>>>() {
					});

			DataCacheUtils.saveObject(context, list, key);

		}
		else if (list == null)
			list = new HashMap<String, String>();

		return list;
	}

	public static List<LocationInfo> getUserLocationList(AppContext context)
			throws AppException
	{
		long userid = context.CurrentUser.ID;
		long clientid = context.CurrentClient.Id;
		String key = "location_" + userid + "_" + clientid;
		ArrayList<LocationInfo> list = (ArrayList<LocationInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getUserLocationURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "user_location");
			params.put("flag", context.HasSetClientAndLocation ? "0" : "1");
			params.put("userid", userid + "");
			params.put("clientid", clientid + "");

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			list = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<LocationInfo>>>() {
					});
			DataCacheUtils.saveObject(context, list, key);
		}
		else if (list == null)
			list = new ArrayList<LocationInfo>();

		return list;
	}

	public static List<ZoneInfo> getZoneList(AppContext context,
			String locationId) throws AppException
	{

		String key = "zone_list_" + locationId;
		ArrayList<ZoneInfo> list = (ArrayList<ZoneInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getUserLocationURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "zone_list");
			params.put("locationid", locationId);

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			list = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<ZoneInfo>>>() {
					});
			DataCacheUtils.saveObject(context, list, key);
		}
		else if (list == null)
			list = new ArrayList<ZoneInfo>();
		return list;
	}

	public static List<AreaInfo> getAreaList(AppContext context, String zoneId)
			throws AppException
	{
		String key = "area_list_" + zoneId;
		ArrayList<AreaInfo> list = (ArrayList<AreaInfo>) DataCacheUtils
				.readObject(context, key);

		if (Utilities.isNetworkConnected(context)
				&& (list == null || list.isEmpty()))
		{
			String url = URLInfo.getUserLocationURL(context);
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", "area_list");
			params.put("zoneid", zoneId);

			String reqURL = HttpUtils.getUrlByParams(url, params, "utf-8");
			byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);

			list = getResult(HttpUtils.Byte2Str(bytes),
					new TypeReference<Result<ArrayList<AreaInfo>>>() {
					});
			DataCacheUtils.saveObject(context, list, key);
		}
		else if (list == null)
			list = new ArrayList<AreaInfo>();
		return list;
	}
}
