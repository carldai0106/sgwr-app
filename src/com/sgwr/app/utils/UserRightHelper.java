package com.sgwr.app.utils;

import java.util.List;

import com.sgwr.app.AppContext;
import com.sgwr.app.bean.UserGroupRightInfo;
import com.sgwr.app.utils.Utilities.Predicate;

public class UserRightHelper {

	public static boolean HasSetClientAndLocation(List<UserGroupRightInfo> list)
	{
		boolean flag = false;
		for (UserGroupRightInfo info : list)
		{
			if (info.NT_ModCode.equals("Show Set Client and Location"))
			{
				flag = info.IsShow;
				break;
			}
		}

		return flag;
	}

	public static boolean CheckAction(AppContext context, String actionName, final String modCode)
	{
		List<UserGroupRightInfo> list = context.UserGroupRightList;
		if (list == null)
			return false;

		UserGroupRightInfo info = Utilities.check(list,
				new Predicate<UserGroupRightInfo>() {
					@Override
					public boolean Predicate(UserGroupRightInfo obj)
					{
						// TODO Auto-generated method stub
						if (obj.NT_ModCode.equals(modCode))
							return true;
						else
							return false;
					}
				});

		boolean blnNoRight = false;

		if (actionName.equals("List"))
		{
			if (info != null && info.IsShow)
				blnNoRight = true;
		}
		else if (actionName.equals("Edit"))
		{
			if (info != null && info.IsEdit)
				blnNoRight = true;
		}

		else if (actionName.equals("Add"))
		{
			if (info != null && info.IsAdd)
				blnNoRight = true;
		}
		else if (actionName.equals("Delete"))
		{

			if (info != null && info.IsDelete)
				blnNoRight = true;
		}

		else if (actionName.equals("Search"))
		{
			if (info != null && info.IsSearch)
				blnNoRight = true;
		}
		else if (actionName.equals("Export"))
		{
			if (info != null && info.IsExport)
				blnNoRight = true;
		}
		else if (actionName.equals("Preview"))
		{
			if (info != null && info.IsView)
				blnNoRight = true;
		}
		else
		{
			blnNoRight = false;
		}
		return blnNoRight;
	}
}
