package com.sgwr.app.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.sgwr.app.ActionType;
import com.sgwr.app.AppContext;
import com.sgwr.app.R;
import com.sgwr.app.bean.AttachmentInfo;
import com.sgwr.app.bean.URLInfo;
import com.sgwr.app.bean.UserInfo;
import com.sgwr.app.utils.CompressBitmapUtils;
import com.sgwr.app.utils.FileUtils;
import com.sgwr.app.utils.UIHelper;
import com.sgwr.app.utils.UploadFile;
import com.sgwr.app.utils.UploadFile.OnUploadListener;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class CameraPhoto extends Activity {

	// header
	private ImageButton inner_header_back;
	private TextView inner_header_title;
	private ProgressBar inner_header_progress;

	// footer
	private RadioButton inner_foot_radio_save;

	private Button btn_take_photo;
	private Button btn_picker_photo;
	private EditText edt_activity_photo_brief;
	private ImageView img_view;

	private AppContext context;
	private long ActivityId = 0;

	private int SELECT_PIC_BY_PICK_PHOTO = 0x01;
	private int SELECT_PIC_BY_TACK_PHOTO = 0x02;
	private Uri photoUri = null;
	private String picPath = null;
	private ActionType operateType = null;
	private String attachId = "";
	private boolean isOperated = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frm_take_photo);

		context = (AppContext) getApplication();
		Intent intent = getIntent();
		ActivityId = intent.getLongExtra("ActivityId", 0);
		operateType = (ActionType) intent.getSerializableExtra("type");

		inner_header_back = (ImageButton) findViewById(R.id.inner_header_back);
		inner_header_title = (TextView) findViewById(R.id.inner_header_title);
		inner_header_progress = (ProgressBar) findViewById(R.id.inner_header_progress);
		inner_foot_radio_save = (RadioButton) findViewById(R.id.inner_foot_radio_save);

		inner_header_title.setText("Take & Pick Photo");
		inner_foot_radio_save.setText("Upload");

		btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
		btn_picker_photo = (Button) findViewById(R.id.btn_picker_photo);
		edt_activity_photo_brief = (EditText) findViewById(R.id.edt_activity_photo_brief);
		img_view = (ImageView) findViewById(R.id.img_view);

		btn_take_photo.setOnClickListener(new ClickEvent());
		btn_picker_photo.setOnClickListener(new ClickEvent());
		inner_foot_radio_save.setOnClickListener(new ClickEvent());
		inner_header_back.setOnClickListener(new ClickEvent());

		if (operateType == ActionType.Edit)
		{
			AttachmentInfo info = (AttachmentInfo) intent
					.getSerializableExtra("AttachmentInfo");

			attachId = info.Id;
			edt_activity_photo_brief.setText(info.Note);

			String filePath = FileUtils.getFilesPath(this) + File.separator
					+ info.FileName;

			if (FileUtils.checkFilePathExists(filePath))
			{
				Bitmap bitmap = CompressBitmapUtils.compressBitmap(this,
						filePath, 0);
				img_view.setImageBitmap(bitmap);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == SELECT_PIC_BY_PICK_PHOTO)
			{
				if (data == null)
				{
					Toast.makeText(this, "Please select a photo.",
							Toast.LENGTH_SHORT).show();
					return;
				}

				photoUri = data.getData();
				if (photoUri == null)
				{
					Toast.makeText(this, "Please select a photo.",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			else
			{
				if (photoUri == null)
				{
					Toast.makeText(this, "Can not read the taked photo.",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}

			showPhoto();
		}
		else
		{
			if (requestCode == SELECT_PIC_BY_TACK_PHOTO)
			{
				picPath = getPhotoPath();
				if (picPath != null)
				{
					File file = new File(picPath);
					file.delete();
					picPath = null;
				}
			}
		}
	}

	private String getPhotoPath()
	{
		String path = null;
		String[] pojo = { MediaColumns.DATA };
		Cursor cursor = managedQuery(photoUri, pojo, null, null, null);

		if (cursor != null)
		{
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			path = cursor.getString(columnIndex);
			cursor.close();
		}

		return path;
	}

	private void showPhoto()
	{
		picPath = getPhotoPath();
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG")
						|| picPath.endsWith(".jpeg") || picPath
							.endsWith(".JPEG")))
		{
			Bitmap bm = CompressBitmapUtils.compressBitmap(this, picPath, 0);
			img_view.setImageBitmap(bm);
		}
		else
		{
			Toast.makeText(this, "Please select a valid photo.",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void pickerPhoto()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}

	private void takePhoto()
	{
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED))
		{
			// "android.media.action.IMAGE_CAPTURE"
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);

			/** ----------------- */
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		}
		else
		{
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_SHORT).show();
		}
	}

	private void upload(String strType)
	{
		String brief = UIHelper.getEditText(edt_activity_photo_brief);
		String reqUrl = URLInfo.getUploadURL(context);

		Map<String, String> maps = new HashMap<String, String>();
		UserInfo user = context.CurrentUser;
		maps.put("type", "upload_activity_pic");
		maps.put("subtype", strType);
		maps.put("username", user.UserName);
		maps.put("password", user.Password);
		maps.put("activityid", String.valueOf(ActivityId));
		maps.put("attachid", attachId);
		maps.put("brief", brief);

		UploadFile upload = new UploadFile(CameraPhoto.this, context, picPath,
				maps);
		upload.execute(reqUrl);
		upload.setOnUploadListener(new OnUploadListener() {
			@Override
			public void OnUploadDone(String result)
			{
				// TODO Auto-generated method stub
				edt_activity_photo_brief.setText("");
				img_view.setImageBitmap(null);
				photoUri = null;
				picPath = null;
				isOperated = true;
			}

			@Override
			public void OnPreUpload()
			{
				// TODO Auto-generated method stub

			}
		});
	}

	class ClickEvent implements OnClickListener {
		@Override
		public void onClick(View view)
		{
			// TODO Auto-generated method stub
			if (view == btn_take_photo)
			{
				takePhoto();
			}
			else if (view == btn_picker_photo)
			{
				pickerPhoto();
			}
			else if (view == inner_foot_radio_save)
			{
				if (operateType == ActionType.Add)
				{
					if (picPath != null)
					{
						upload("add");
					}
					else
					{
						Toast.makeText(
								CameraPhoto.this,
								"Please take a photo or pick a photo to uplaod.",
								Toast.LENGTH_SHORT).show();
					}
				}
				else if (operateType == ActionType.Edit)
				{
					upload("edit");
				}
			}
			else if (view == inner_header_back)
			{
				if (isOperated)
				{
					setResult(3001);
					finish();
				}
				else
					finish();
			}
		}
	}
}
