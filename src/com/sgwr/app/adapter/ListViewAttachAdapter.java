package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.AppContext;
import com.sgwr.app.R;
import com.sgwr.app.bean.AttachmentInfo;
import com.sgwr.app.bean.URLInfo;
import com.sgwr.app.utils.BitmapManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAttachAdapter extends BaseAdapter {

	private List<AttachmentInfo> list;
	private LayoutInflater layout;
	private int viewSource;
	private BitmapManager bmpManager;
	private AppContext appContext;
	private Context context;

	static class ListItemView {
		public ImageView imgIcon;
		public TextView txtNote;
		public ImageView imgFlag;
	}

	public ListViewAttachAdapter(Context context, AppContext appContext,
			List<AttachmentInfo> data, int resource) {
		this.layout = LayoutInflater.from(context);
		this.viewSource = resource;
		this.list = data;
		this.context = context;
		this.appContext = appContext;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.default_pic);
		bmpManager = new BitmapManager(context, bitmap);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parentView)
	{
		// TODO Auto-generated method stub

		ListItemView listItemView = null;
		if (contentView == null)
		{
			contentView = layout.inflate(this.viewSource, null);
			listItemView = new ListItemView();
			listItemView.txtNote = (TextView) contentView
					.findViewById(R.id.text_note);
			listItemView.imgIcon = (ImageView) contentView
					.findViewById(R.id.img_preview);
			listItemView.imgFlag = (ImageView) contentView
					.findViewById(R.id.imageview_flag);

			contentView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) contentView.getTag();
		}

		final AttachmentInfo info = list.get(position);

		String strName = info.FileName != null ? info.FileName.toLowerCase()
				: "";
		if (strName.endsWith(".png") || strName.endsWith(".jpg")
				|| strName.endsWith(".jpeg"))
		{
			String url = URLInfo.getURL(appContext) + info.FilePath + "/"
					+ info.FileName;

			bmpManager.loadBitmap(url, listItemView.imgIcon, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.loading_pic), 40, 35);
		}

		listItemView.txtNote.setText(info.Note);

		final ListItemView view = listItemView;

		if (info.IsSelected)
		{
			listItemView.imgFlag.setImageResource(R.drawable.active);
		}
		else
		{
			listItemView.imgFlag.setImageResource(R.drawable.inactive);
		}

		contentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				if (info.IsSelected)
				{
					info.IsSelected = false;
					view.imgFlag.setImageResource(R.drawable.inactive);
				}
				else
				{
					info.IsSelected = true;
					view.imgFlag.setImageResource(R.drawable.active);
				}

				Object obj = view.imgIcon.getTag();
				if (obj != null)
				{
					if (obj.toString() == "loading")
						info.IsLoaded = false;
					else if (obj.toString() == "loaded")
						info.IsLoaded = true;
				}
			}
		});

		listItemView.txtNote.setTag(info);
		return contentView;
	}

}
