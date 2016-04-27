package com.sgwr.app.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppConfig;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.bean.Result;
import com.sgwr.app.bean.URLInfo;
import com.sgwr.app.bean.UserGroupRightInfo;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.utils.CyptoUtils;
import com.sgwr.app.utils.HttpUtils;

public class UserService extends BaseService {

	public static UserInfo login(AppContext context, String username,
			String password) throws AppException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", CyptoUtils.encode(AppConfig.CYPTO_KEY, username));
		params.put("password", CyptoUtils.encode(AppConfig.CYPTO_KEY, password));
		params.put("type", "login");

		UserInfo info = new UserInfo();
		String loginurl = URLInfo.getLoginURL(context);
		byte[] bytes = HttpUtils.requestHttpPost(context, loginurl, params, null,
				Charset.forName("utf-8"));

		info = getResult(HttpUtils.Byte2Str(bytes),
				new TypeReference<Result<UserInfo>>() {
				});

		return info;
	}

	public static ArrayList<UserGroupRightInfo> getUserGroupRight(
			AppContext context, String groupId) throws AppException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "usergroupright");
		params.put("groupid", groupId);

		ArrayList<UserGroupRightInfo> list = new ArrayList<UserGroupRightInfo>();
		String loginurl = URLInfo.getLoginURL(context);

		byte[] bytes = HttpUtils.requestHttpPost(context, loginurl, params, null,
				Charset.forName("utf-8"));

		list = getResult(HttpUtils.Byte2Str(bytes),
				new TypeReference<Result<ArrayList<UserGroupRightInfo>>>() {
				});

		return list;
	}
}
