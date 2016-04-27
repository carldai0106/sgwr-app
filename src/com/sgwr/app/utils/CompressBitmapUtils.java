package com.sgwr.app.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class CompressBitmapUtils {

	/**
	 * 压缩图片
	 * 
	 * @param filePath
	 *            文件路径
	 * @param width
	 *            期望的宽度
	 * @param height
	 *            期望的高度
	 * @param limitSize
	 *            期望压缩后的大小,如果是0则不压缩到指定大小
	 * @return
	 */
	public static Bitmap compressBitmap(String filePath, int width, int height,
			int limitSize)
	{
		// 给定的BitmapFactory设置解码的参数
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 从解码器中获得原始图片的宽高，而避免申请内存空间
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		if (limitSize == 0)
			return bitmap;

		return compressBitmap(bitmap, limitSize);
	}	
	

	/**
	 * 获得图片的缩放程度
	 * 
	 * @param options
	 *            Options
	 * @param reqWidth
	 *            期望的宽度
	 * @param reqHeight
	 *            期望的高度
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight)
	{
		// 获得原始图片的宽高
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		int inSimpleSize = 1;
		if (imageHeight > reqHeight || imageWidth > reqWidth)
		{
			// 计算压缩的比例：分为宽高比例
			final int heightRatio = Math.round((float) imageHeight
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) imageWidth
					/ (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSimpleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSimpleSize;
	}

	/**
	 * 根据屏幕的高宽对图片进行压缩
	 * 
	 * @param context
	 *            Context
	 * @param filePath
	 *            图片路径
	 * @param limitSize
	 *            期望压缩后的大小,如果是0则不压缩到指定大小
	 * @return
	 */
	public static Bitmap compressBitmap(Context context, String filePath,
			int limitSize)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// 这个isjustdecodebounds很重要
		opt.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, opt);

		// 获取到这个图片的原始宽度和高度
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		Activity activity = null;
		if (context instanceof Activity)
		{
			activity = (Activity) context;
		}
		else
		{
			return null;
		}

		// 获取屏的宽度和高度
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();

		// isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
		opt.inSampleSize = 1;
		// 根据屏的大小和图片大小计算出缩放比例
		if (picWidth > picHeight)
		{
			if (picWidth > screenWidth)
				opt.inSampleSize = picWidth / screenWidth;
		}
		else
		{
			if (picHeight > screenHeight)
				opt.inSampleSize = picHeight / screenHeight;
		}
		// 这次再真正地生成一个有像素的，经过缩放了的bitmap
		opt.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filePath, opt);

		if (limitSize == 0)
			return bitmap;

		return compressBitmap(bitmap, limitSize);

	}
	

	/**
	 * 给出一个期望的图片压缩大小 kb
	 * 
	 * @param image
	 *            图片
	 * @param limitSize
	 *            期望的图片大小 kb
	 * @return
	 */
	public static Bitmap compressBitmap(Bitmap image, int limitSize)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		int options = 100;
		// 循环判断如果压缩后图片是否大于limitSize kb,大于继续压缩
		while (baos.toByteArray().length / 1024 > limitSize)
		{
			// 重置baos即清空baos
			baos.reset();
			// 这里压缩比options = 50，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			// 每次都减少5
			options -= 5;
			
			int len = baos.toByteArray().length / 1024;
			Log.i("Compress", String.valueOf(len) );
		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream compressedBAIS = new ByteArrayInputStream(
				baos.toByteArray());		
		
		Bitmap bitmap = BitmapFactory.decodeStream(compressedBAIS, null, null);
		
		return bitmap;
	}
}
