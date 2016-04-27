package com.sgwr.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	// private final static SimpleDateFormat dateFormater = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// private final static SimpleDateFormat dateFormater2 = new
	// SimpleDateFormat("yyyy-MM-dd");

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	public static Date toDate(String sdate)
	{
		try
		{
			return dateFormater.get().parse(sdate);
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	public static boolean isToday(String sdate)
	{
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null)
		{
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate))
			{
				b = true;
			}
		}
		return b;
	}

	/**
	 * 如果是空 返回 真 否则 假
	 * @param input
	 * @return
	 */
	public static boolean isEmpty(String input)
	{
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++)
		{
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
			{
				return false;
			}
		}
		return true;
	}

	public static String removeLastChar(StringBuilder sb)
	{
		String str = "";
		if (sb.toString() != "")
		{
			 str = sb.deleteCharAt(sb.length() - 1).toString();
		}
		return str;
	}

	public static boolean isEmail(String email)
	{
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	public static int toInt(String str, int defValue)
	{
		try
		{
			return Integer.parseInt(str);
		}
		catch (Exception e)
		{
		}
		return defValue;
	}

	public static int toInt(Object obj)
	{
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	public static long toLong(String obj)
	{
		try
		{
			return Long.parseLong(obj);
		}
		catch (Exception e)
		{
		}
		return 0;
	}

	public static boolean toBool(String b)
	{
		try
		{
			return Boolean.parseBoolean(b);
		}
		catch (Exception e)
		{
		}
		return false;
	}

}
