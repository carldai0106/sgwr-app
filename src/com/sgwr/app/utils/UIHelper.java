package com.sgwr.app.utils;

import java.util.List;

import com.sgwr.app.AppManager;
import com.sgwr.app.R;
import com.sgwr.app.bean.SpinnerData;
import com.sgwr.app.utils.MultiMailSender.MultiMailSenderInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Spinner;

public class UIHelper {
	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;

	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;

	public final static int LISTVIEW_DATATYPE_INCIDENT = 0x01;

	public static String getEditText(EditText edit)
	{
		String strVal = "";
		if (edit != null)
		{
			Editable edt = edit.getText();
			if (edt != null)
			{
				strVal = edt.toString();
			}
		}

		return strVal;
	}

	public static void setSelection(Spinner spinner, List<SpinnerData> list,
			long id)
	{
		int i = 0;
		for (SpinnerData item : list)
		{
			Object obj = item.getValue();
			long val = Utilities.Object2Long(obj);
			if (val == id)
			{
				break;
			}
			i++;
		}

		spinner.setSelection(i);
	}

	public static void setSelection(Spinner spinner, List<SpinnerData> list,
			int id)
	{
		int i = 0;
		for (SpinnerData item : list)
		{
			Object obj = item.getValue();
			int val = Utilities.Object2Int(obj);
			if (val == id)
			{
				break;
			}
			i++;
		}

		spinner.setSelection(i);
	}

	public static void setSelection(Spinner spinner, List<SpinnerData> list,
			String source)
	{
		int i = 0;
		for (SpinnerData item : list)
		{
			Object obj = item.getValue();
			String val = obj.toString();
			if (val.equals(source))
			{
				break;
			}
			i++;
		}

		spinner.setSelection(i);
	}

	public static void sendAppCrashReport(final Context cont,
			final String crashReport)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_error);
		builder.setMessage(R.string.app_error_message);
		builder.setPositiveButton(R.string.submit_report,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();

						MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
						mailInfo.setMailServerHost("smtp.126.com");
						mailInfo.setMailServerPort("25");
						mailInfo.setValidate(true);
						mailInfo.setUserName("bcstracker@126.com");
						mailInfo.setPassword("bcsint13a5");// 您的邮箱密码
						mailInfo.setFromAddress("bcstracker@126.com");
						mailInfo.setToAddress("carldai@bcsint.com");
						mailInfo.setSubject("BCSTRACKER Crash Report");
						mailInfo.setContent(crashReport);
						// 这个类主要来发送邮件
						MultiMailSender sms = new MultiMailSender();
						sms.sendTextMail(mailInfo);// 发送文体格式

						// 退出
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.setNegativeButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						// 退出
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.show();
	}

	/**
	 * 退出程序
	 * 
	 * @param cont
	 */
	public static void Exit(final Context cont)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						// 退出
						AppManager.getAppManager().AppExit(cont);
					}
				});
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		builder.show();
	}
}
