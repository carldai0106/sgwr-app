package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sgwr.app.AppContext;
import com.sgwr.app.R;
import com.sgwr.app.utils.UserRightHelper;

public class SlideMenuInfo implements Serializable {
	public String MenuName;
	public int IconSource;
	public String MenuCode;
	public String MenuPrevBgColor;
	public String MenuBgColor;

	public static List<SlideMenuInfo> getList(AppContext context)
	{
		List<SlideMenuInfo> list = new ArrayList<SlideMenuInfo>();

		SlideMenuInfo info = null;
		info = new SlideMenuInfo();
		info.IconSource = R.drawable.client_icon;
		info.MenuName = "Select Client";
		info.MenuCode = "Select Client";

		list.add(info);
		if (UserRightHelper.CheckAction(context, "List",
				"Show Set Client and Location"))
		{
			info = new SlideMenuInfo();
			info.IconSource = R.drawable.location_icon;
			info.MenuName = "Select Location";
			info.MenuCode = "Select Location";
			list.add(info);
		}

		return list;
	}
}
