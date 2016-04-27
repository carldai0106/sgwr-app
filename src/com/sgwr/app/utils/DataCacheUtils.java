package com.sgwr.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.sgwr.app.AppContext;

public class DataCacheUtils {
	private static final int CACHE_TIME = 60 * 60000;

	public static boolean saveObject(AppContext context, Serializable ser,
			String file)
	{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try
		{
			String path = FileUtils.getCachePath(context) + "/" + file;
			File dataFile = new File(path);
			if (!dataFile.exists())
				dataFile.createNewFile();

			fos = new FileOutputStream(path);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				oos.close();
			}
			catch (Exception e)
			{
			}
			try
			{
				fos.close();
			}
			catch (Exception e)
			{
			}
		}
	}

	public static boolean isCacheDataFailure(AppContext context,
			String cachefile)
	{
		boolean failure = false;
		File data = context.getFileStreamPath(cachefile);
		if (data.exists()
				&& (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
			failure = true;
		else if (!data.exists())
			failure = true;
		return failure;
	}

	public static void clearAppCache(AppContext context)
	{
		String cachePath = FileUtils.getCachePath(context);
		String filesPath = FileUtils.getFilesPath(context);

		File cacheFile = new File(cachePath);
		File filesFile = new File(filesPath);

		Date date = DateTimeUtils.getCurrentDate();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);

		clearCacheFolder(cacheFile, date.getTime());
		clearCacheFolder(filesFile, cal.getTime().getTime());
	}

	private static int clearCacheFolder(File dir, long curTime)
	{
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory())
		{
			try
			{
				for (File child : dir.listFiles())
				{
					if (child.isDirectory())
					{
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime)
					{
						if (child.delete())
						{
							deletedFiles++;
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return deletedFiles;
	}

	private static boolean isExistDataCache(AppContext context, String cachefile)
	{
		boolean exist = false;
		String path = FileUtils.getCachePath(context) + "/" + cachefile;
		File file = new File(path);
		if (file.exists())
			exist = true;
		return exist;
	}

	public static Serializable readObject(AppContext context, String file)
	{
		if (!isExistDataCache(context, file))
			return null;

		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try
		{

			String path = FileUtils.getCachePath(context) + "/" + file;
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);

			return (Serializable) ois.readObject();
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			if (e instanceof InvalidClassException)
			{
				String path = FileUtils.getCachePath(context) + "/" + file;
				File data = new File(path);

				data.delete();
			}
		}
		finally
		{
			try
			{
				ois.close();
			}
			catch (Exception e)
			{
			}
			try
			{
				fis.close();
			}
			catch (Exception e)
			{
			}
		}
		return null;
	}
}
