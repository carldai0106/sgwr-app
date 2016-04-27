package com.sgwr.app.service;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.bean.Result;
import com.sgwr.app.bean.UserInfo;

import com.sgwr.app.utils.HttpUtils;
import com.sgwr.app.utils.JsonUtils;

public class BaseService {

	public static <T> T getResult(String source, TypeReference<Result<T>> ref)
			throws AppException
	{
		Result<T> rs = JsonUtils.DeserializeObject(source, ref);

		if (rs.ResultCode == 1)
			return rs.TObj;
		else
			throw AppException.server(new Exception(rs.ResultMsg));

	}

	public static String quickOperate(AppContext context, String url,
			String type, String subType, String json, Map<String, String> maps)
			throws AppException
	{
		String strMsg = "";

		UserInfo user = context.CurrentUser;
		String userName = user.UserName;
		String password = user.Password;
		String strJson = json;
		try
		{
			strJson = URLEncoder.encode(json, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AppException.run(e);
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("subtype", subType);
		params.put("username", userName);
		params.put("password", password);
		params.put("json", strJson);
		if (maps != null && !maps.isEmpty())
			params.putAll(maps);

		byte[] bytes = HttpUtils.requestHttpPost(context, url, params, null,
				Charset.forName("UTF-8"));

		String result = HttpUtils.Byte2Str(bytes);
		Result<String> rs = Result.parse(result,
				new TypeReference<Result<String>>() {
				});

		if (rs.ResultCode == 1)
		{
			strMsg = rs.ResultMsg;
		}
		else
		{
			throw AppException.server(new Exception(rs.ResultMsg));
		}

		return strMsg;
	}

	public static String quickOperate(AppContext context, String url,
			String type, String subType, String json) throws AppException
	{
		return quickOperate(context, url, type, subType, json, null);
	}
}
