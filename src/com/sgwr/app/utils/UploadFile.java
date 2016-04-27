package com.sgwr.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppContext;
import com.sgwr.app.bean.Result;
import com.sgwr.app.widget.LoadingDialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class UploadFile extends AsyncTask<String, Integer, String> {

	private String filePath;
	private LoadingDialog loadDlg;
	private Context context;
	private String TAG = "UploadFile";
	private AppContext appContext;
	private Map<String, String> maps = new HashMap<String, String>();

	private OnUploadListener onUploadListener;

	public void setOnUploadListener(OnUploadListener listener)
	{
		onUploadListener = listener;
	}

	public UploadFile(Context context, AppContext appContext, String filePath,
			Map<String, String> params) {
		this.context = context;
		this.appContext = appContext;
		this.filePath = filePath;
		this.maps = params;
		loadDlg = new LoadingDialog(context);
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		if (onUploadListener != null)
		{
			onUploadListener.OnPreUpload();
		}
		loadDlg.show();
	}

	@Override
	protected String doInBackground(String... params)
	{
		// TODO Auto-generated method stub
		String reqUrl = params[0];

		Map<String, File> files = null;
		try
		{
			// 如果图片路径为空, 则不上传图片
			File file = null;
			if (!StringUtils.isEmpty(filePath))
			{
				files = new HashMap<String, File>();
				Bitmap bitmap = CompressBitmapUtils.compressBitmap(filePath,
						800, 600, 80);
				// 生成本来的文件名
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/"
						+ System.currentTimeMillis()
						+ ".jpg";
				// 将压缩之后的图片保存到本地
				FileOutputStream fileOutputStream = new FileOutputStream(
						new File(path));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
						fileOutputStream);

				file = new File(path);
				files.put("file", file);

			}

			byte[] bytes = HttpUtils.requestHttpPost(appContext, reqUrl, maps,
					files, Charset.forName("utf-8"));
			String strRS = HttpUtils.Byte2Str(bytes);

			Result<String> result = JsonUtils.DeserializeObject(strRS,
					new TypeReference<Result<String>>() {
					});

			// 删除压缩在本地的图片
			if (file != null && file.exists())
				file.delete();

			return result.ResultMsg;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result)
	{
		loadDlg.dismiss();
		if (result == null)
		{
			Toast.makeText(context, "Uploaded file unsuccessful!",
					Toast.LENGTH_SHORT).show();
		}
		else if (result.equals("OK"))
		{
			Toast.makeText(context, "Uploaded file successful! ",
					Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(context, "Uploaded file unsuccessful! " + result,
					Toast.LENGTH_SHORT).show();
		}
		if (onUploadListener != null)
		{
			onUploadListener.OnUploadDone(result);
		}
		super.onPostExecute(result);
	}

	public interface OnUploadListener {
		// 准备上传
		void OnPreUpload();

		// 上传完毕
		void OnUploadDone(String result);
	}
}
