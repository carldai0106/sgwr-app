package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.R;
import com.sgwr.app.bean.SlideMenuInfo;
import com.sgwr.app.utils.StringUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideMenuAdapter extends BaseAdapter {
	private List<SlideMenuInfo> list;
	private LayoutInflater layout;
	private int itemSource;

	static class ListItemView {
		public ImageView slide_menu_prev_bgcolor;
		public ImageView slide_menu_icon;
		public TextView slide_menu_text;
		public LinearLayout slide_menu_background;
	}

	public SlideMenuAdapter(Context context, List<SlideMenuInfo> data,
			int resource) {
		this.layout = LayoutInflater.from(context);
		this.itemSource = resource;
		this.list = data;
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
		ListItemView listItemView = null;
		if (contentView == null)
		{
			contentView = layout.inflate(this.itemSource, null);
			listItemView = new ListItemView();
			listItemView.slide_menu_icon = (ImageView) contentView
					.findViewById(R.id.slide_menu_icon);
			listItemView.slide_menu_text = (TextView) contentView
					.findViewById(R.id.slide_menu_text);
			listItemView.slide_menu_background = (LinearLayout) contentView
					.findViewById(R.id.slide_menu_background);
			listItemView.slide_menu_prev_bgcolor = (ImageView) contentView
					.findViewById(R.id.slide_menu_prev_bgcolor);

			contentView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) contentView.getTag();
		}

		SlideMenuInfo info = list.get(position);
		listItemView.slide_menu_icon.setImageResource(info.IconSource);
		listItemView.slide_menu_text.setText(info.MenuName);
		listItemView.slide_menu_text.setTag(info.MenuCode);
		if (!StringUtils.isEmpty(info.MenuBgColor))
		{
			listItemView.slide_menu_background.setBackgroundColor(Color
					.parseColor(info.MenuBgColor));
		}

		if (!StringUtils.isEmpty(info.MenuPrevBgColor))
		{
			listItemView.slide_menu_prev_bgcolor.setBackgroundColor(Color
					.parseColor(info.MenuPrevBgColor));
		}

		return contentView;
	}

}
