package com.sgwr.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Spinner;

import com.sgwr.app.AppConfig;
import com.sgwr.app.AppContext;
import com.sgwr.app.bean.SpinnerData;

public class Utilities {

	public interface Predicate<T> {
		boolean Predicate(T obj);
	}

	public static boolean isNetworkConnected(AppContext context)
	{
		if (AppConfig.IS_DEBUG || context == null)
		{
			return true;
		}
		else
		{
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			return ni != null && ni.isConnectedOrConnecting();
		}
	}

	public static boolean isNullSpinnerData(SpinnerData data)
	{
		boolean flag = false;
		if (data == null)
			flag = true;
		else if (data.getValue() == null)
			flag = true;
		else if (StringUtils.isEmpty(data.getValue().toString()))
		{
			flag = true;
		}

		return flag;
	}

	public static int Object2Int(Object object)
	{
		int iVal = 0;
		if (object != null)
		{
			try
			{
				String str = object.toString();
				iVal = Integer.parseInt(str);
			}
			catch (Exception e)
			{
				iVal = 0;
			}
		}

		return iVal;
	}

	public static long Object2Long(Object object)
	{
		long val = 0;
		if (object != null)
		{
			try
			{
				String str = object.toString();
				val = Long.parseLong(str);
			}
			catch (Exception e)
			{
				val = 0;
			}
		}
		return val;
	}

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

	public static String getSpinnerValue(Spinner spinner)
	{
		String strVal = "";
		if (spinner != null)
		{
			Object obj = spinner.getSelectedItem();
			if (obj != null)
			{
				SpinnerData data = (SpinnerData) obj;
				if (data != null)
				{
					if (!isNullSpinnerData(data))
					{
						strVal = data.getValue().toString();
					}
				}
			}
		}

		return strVal;
	}

	public static String getSpinnerText(Spinner spinner)
	{
		String strText = "";
		if (spinner != null)
		{
			Object obj = spinner.getSelectedItem();
			if (obj != null)
			{
				SpinnerData data = (SpinnerData) obj;
				if (data != null)
				{
					if (!isNullSpinnerData(data))
					{
						strText = data.getText();
					}
				}
			}
		}

		return strText;
	}

	

	public static <T> T check(List<T> t, Predicate<T> predicate)
	{		
		T obj = null;
		for (T item : t)
		{
			if (predicate.Predicate(item))
			{
				obj = item;
				break;
			}
		}

		return obj;
	}
	
	public static <T> List<T> mapList(List<T> list, Predicate<T> predicate)
	{
		List<T> tempList = new ArrayList<T>();
		for (T item : list)
		{
			if (predicate.Predicate(item))
			{
				tempList.add(item);
			}
		}
		return tempList;
	}

	public static String RemoveHtmlTag(String html)
	{
		if (html != null && html != "")
		{
			// 先将换行标签替换成★
			html = html.replaceAll("<br />", "★");
			html = html.replaceAll("</p><p", "</p>★<p");

			String strScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";
			String strStyle = "<style(.+?)/style>";
			String strHtml = "<[^>]+>";

			Pattern patternScript = Pattern.compile(strScript,
					Pattern.CASE_INSENSITIVE);
			Matcher matcherScript = patternScript.matcher(html);
			html = matcherScript.replaceAll("");

			Pattern patternStyle = Pattern.compile(strStyle,
					Pattern.CASE_INSENSITIVE);
			Matcher matcherStyle = patternStyle.matcher(html);
			html = matcherStyle.replaceAll("");

			Pattern patternHtml = Pattern.compile(strHtml,
					Pattern.CASE_INSENSITIVE);
			Matcher matcherHtml = patternHtml.matcher(html);
			html = matcherHtml.replaceAll("");

			html = html.replaceAll("★", " \r\n ");
			return html.replaceAll("&nbsp;", " ");
		}
		else
		{
			return html;
		}
	}

	public static String RelpaceRN(String value)
	{
		if (value != null && value != "")
		{
			value = value.replaceAll("\r\n", "<br />");
			return value;
		}
		else
		{
			return "";
		}
	}
}
