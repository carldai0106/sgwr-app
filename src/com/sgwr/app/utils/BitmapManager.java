package com.sgwr.app.utils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sgwr.app.AppException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * 异步线程加载图片工具类 使用说明： BitmapManager bmpManager; bmpManager = new
 * BitmapManager(BitmapFactory.decodeResource(context.getResources(),
 * R.drawable.loading)); bmpManager.loadBitmap(imageURL, imageView);
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-6-25
 */
public class BitmapManager {

	private static HashMap<String, SoftReference<Bitmap>> cache;
	private static ExecutorService pool;
	private static Map<ImageView, String> imageViews;
	private Bitmap defaultBmp;
	private Context context;
	private static String cacheFilePath;

	static
	{
		cache = new HashMap<String, SoftReference<Bitmap>>();
		pool = Executors.newFixedThreadPool(5); // 固定线程池
		imageViews = Collections
				.synchronizedMap(new WeakHashMap<ImageView, String>());
	}

	public BitmapManager(Context context, Bitmap def) {
		this.defaultBmp = def;
		this.context = context;
		cacheFilePath = FileUtils.getFilesPath(context);
	}

	/**
	 * 设置默认图片
	 * 
	 * @param bmp
	 */
	public void setDefaultBmp(Bitmap bmp)
	{
		defaultBmp = bmp;
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @param imageView
	 */
	public void loadBitmap(String url, ImageView imageView)
	{
		loadBitmap(url, imageView, this.defaultBmp, 0, 0);
	}

	/**
	 * 加载图片-可设置加载失败后显示的默认图片
	 * 
	 * @param url
	 * @param imageView
	 * @param defaultBmp
	 */
	public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp)
	{
		loadBitmap(url, imageView, defaultBmp, 0, 0);
	}

	/**
	 * 加载图片-可指定显示图片的高宽
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp,
			int width, int height)
	{
		imageViews.put(imageView, url);
		Bitmap bitmap = getBitmapFromCache(url);

		if (bitmap != null)
		{
			// 显示缓存图片
			imageView.setImageBitmap(bitmap);
		}
		else
		{
			// 加载SD卡中的图片缓存 以s开头表示压缩过的
			String fileName = "s_" + FileUtils.getFileName(url);
			String filePath = cacheFilePath + File.separator + fileName;
			Log.i("FilePath", filePath);
			File file = new File(filePath);
			if (file.exists())
			{
				// 显示SD卡中的图片缓存
				Bitmap bmp = ImageUtils.getBitmapByFile(file);
				imageView.setImageBitmap(bmp);
				imageView.setTag("loaded");
			}
			else
			{
				// 线程加载网络图片
				imageView.setImageBitmap(defaultBmp);
				imageView.setTag("loading");
				queueJob(url, imageView, width, height);
			}
		}
	}

	/**
	 * 从缓存中获取图片
	 * 
	 * @param url
	 */
	public Bitmap getBitmapFromCache(String url)
	{
		Bitmap bitmap = null;
		if (cache.containsKey(url))
		{
			bitmap = cache.get(url).get();
		}
		return bitmap;
	}

	/**
	 * 从网络中加载图片
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void queueJob(final String url, final ImageView imageView,
			final int width, final int height)
	{
		/* Create handler in UI thread. */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(url))
				{
					if (msg.obj != null)
					{
						imageView.setImageBitmap((Bitmap) msg.obj);
						imageView.setTag("loaded");
						try
						{
							// 向SD卡中写入图片缓存
							String fileName = FileUtils.getFileName(url);
							String filePath = cacheFilePath + File.separator
									+ "s_" + fileName;
							ImageUtils.saveImage(filePath, (Bitmap) msg.obj);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		};

		pool.execute(new Runnable() {
			@Override
			public void run()
			{
				Message message = Message.obtain();
				message.obj = downloadBitmap(url, width, height);
				handler.sendMessage(message);
			}
		});
	}

	/**
	 * 下载图片-可指定显示图片的高宽
	 * 
	 * @param url
	 * @param width
	 * @param height
	 */
	private Bitmap downloadBitmap(String url, int width, int height)
	{
		Bitmap bitmap = null;
		try
		{
			// http加载图片
			byte[] bytes = HttpUtils.requestHttpGet(null, url);
			String fileName = FileUtils.getFileName(url);
			String filePath = cacheFilePath + File.separator + fileName;
			File file = null;
			try
			{
				file = ImageUtils.saveImage(filePath, bytes);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (file != null)
			{
				// 对图片进行等比例压缩
				bitmap = CompressBitmapUtils.compressBitmap(filePath, width,
						height, 0);
				// 放入缓存
				cache.put(url, new SoftReference<Bitmap>(bitmap));
			}
		}
		catch (AppException e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}
}