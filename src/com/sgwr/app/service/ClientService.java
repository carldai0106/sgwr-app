package com.sgwr.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;
import com.sgwr.app.bean.ClientInfo;
import com.sgwr.app.bean.Result;
import com.sgwr.app.bean.URLInfo;
import com.sgwr.app.utils.HttpUtils;

public class ClientService extends BaseService {

	public static List<ClientInfo> getClientList(AppContext context,
			String userId, String flag) throws AppException
	{
		String clientURL = URLInfo.getClientURL(context);

		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "list");
		params.put("userid", userId);
		params.put("flag", flag);

		String reqURL = HttpUtils.getUrlByParams(clientURL, params, "utf-8");
		byte[] bytes = HttpUtils.requestHttpGet(context, reqURL);
		List<ClientInfo> list = new ArrayList<ClientInfo>();
		list = getResult(HttpUtils.Byte2Str(bytes),
				new TypeReference<Result<List<ClientInfo>>>() {
				});

		return list;
	}

}
