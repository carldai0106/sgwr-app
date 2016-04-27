package com.sgwr.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

	public final static String DATE_TIME_PATTEN = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_TIME_NO_SECOND_PATTEN = "yyyy-MM-dd HH:mm";
	public final static String DATE_PATTEN = "yyyy-MM-dd";

	/**
	 * 取得系统当前时间
	 * @return
	 */
	public static Date getCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * 取得系统当前的UTC时间
	 * @return
	 */
	public static Date getCurrentUtcDate()
	{
		return Local2Utc(getCurrentDate(), DATE_TIME_PATTEN);
	}

	/**
	 * 将Local 时间 转换为UTC 时间 转换后没有秒
	 * @param date
	 * @return
	 */
	public static String getUtcDateTime(Date date)
	{
		return getUtcDateTime(date, DATE_TIME_PATTEN);
	}

	/**
	 * 将Local 时间 转换为UTC 时间 转换后没有秒
	 * @param date
	 * @return
	 */
	public static String getUtcDateTime(Date date, String utcTimePatten)
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(utcTimePatten);
		cal.setTime(date);
		int TimeZoneOffset = cal.get(Calendar.ZONE_OFFSET);
		cal.add(Calendar.MILLISECOND, -TimeZoneOffset);

		return format.format(cal.getTime());
	}

	/**
	 * 字符串转换为日期
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Date Str2Date(String source) throws ParseException
	{
		return Str2Date(source, DATE_TIME_PATTEN);
	}

	/**
	 * 字符串转换为日期
	 * @param source
	 * @param patten
	 * @return
	 * @throws ParseException
	 */
	public static Date Str2Date(String source, String patten)
			throws ParseException

	{
		source = source.replace("/", "-");
		SimpleDateFormat formater = new SimpleDateFormat(patten);
		Date date = new Date();
		try
		{
			date = formater.parse(source);
		}
		catch (ParseException e)
		{
			SimpleDateFormat dateformater = new SimpleDateFormat(
					DATE_TIME_NO_SECOND_PATTEN);
			date = dateformater.parse(source);
		}

		return date;
	}

	/**
	 * 将UTC 时间 转换为 Local 时间 转换后没有秒
	 * @param utcDateTime
	 * @param localPatten
	 * @return
	 */
	public static String getLocalDateTime(Date utcDateTime)
	{
		return getLocalDateTime(utcDateTime, DATE_TIME_PATTEN);
	}

	/**
	 * 将UTC 时间 转换为 Local 时间 转换后没有秒
	 * @param utcDateTime
	 * @param localPatten
	 * @return
	 */
	public static String getLocalDateTime(Date utcDateTime, String localPatten)
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(localPatten);
		cal.setTime(utcDateTime);
		int TimeZoneOffset = cal.get(Calendar.ZONE_OFFSET);
		cal.add(Calendar.MILLISECOND, TimeZoneOffset);

		return format.format(cal.getTime());
	}
	
	/**
	 * 本地时间转换为 UTC 时间
	 * @param localDate
	 * @return
	 */
	public static Date Local2Utc(String localDate)
	{
		return Local2Utc(localDate, DATE_TIME_PATTEN);
	}
	
	/**
	 * 本地时间转换为 UTC 时间
	 * @param localDate
	 * @return
	 */
	public static Date Local2Utc(String localDate, String patten)
	{
		Date date = new Date();
		try
		{
			date = Str2Date(localDate);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Local2Utc(date, patten);
	}

	/**
	 * 本地时间转换为 UTC 时间
	 * @param localDate
	 * @return
	 */
	public static Date Local2Utc(Date localDate)
	{
		return Local2Utc(localDate, DATE_TIME_PATTEN);
	}

	/**
	 * 本地时间转换为 UTC 时间
	 * @param localDate
	 * @param patten
	 * @return
	 */
	public static Date Local2Utc(Date localDate, String patten)
	{
		
		SimpleDateFormat utcFormater = new SimpleDateFormat(DATE_TIME_PATTEN);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));

		SimpleDateFormat formater = new SimpleDateFormat(patten);
		Date utcDate = localDate;
		try
		{
			utcDate = formater.parse(utcFormater.format(localDate));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return utcDate;
	}

	/**
	 * UTC 时间 转换为 本地时间
	 * @param utcTime
	 * @return
	 */
	public static String Utc2Local(String utcTime)
	{
		return Utc2Local(utcTime, DATE_TIME_PATTEN);
	}

	/**
	 * UTC 时间 转换为 本地时间
	 * @param utcTime
	 * @param localTimePatten
	 * @return
	 */
	public static String Utc2Local(String utcTime, String localTimePatten)
	{
		utcTime = utcTime.replace("/", "-");
		SimpleDateFormat utcFormater = new SimpleDateFormat(localTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date gpsUTCDate = null;
		try
		{
			gpsUTCDate = utcFormater.parse(utcTime);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}

	/**
	 * UTC 时间 转换为 本地时间
	 * @param utcTime
	 * @return
	 */
	public static String Utc2Local(Date utcTime)
	{
		return Utc2Local(utcTime, DATE_TIME_PATTEN);
	}

	/**
	 * UTC 时间 转换为 本地时间
	 * @param utcTime
	 * @param localTimePatten
	 * @return
	 */
	public static String Utc2Local(Date utcTime, String localTimePatten)
	{
		SimpleDateFormat formatStr = new SimpleDateFormat(localTimePatten);
		String strDate = formatStr.format(utcTime);

		SimpleDateFormat utcFormater = new SimpleDateFormat(localTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();
		try
		{
			date = utcFormater.parse(strDate);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(date.getTime());

		return localTime;
	}
}
