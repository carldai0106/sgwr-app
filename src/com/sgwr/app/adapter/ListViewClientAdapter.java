package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.R;
import com.sgwr.app.bean.ClientInfo;
import com.sgwr.app.bean.LocationInfo;
import com.sgwr.app.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewClientAdapter<T> extends BaseAdapter {

	private List<T> list;
	private LayoutInflater layout;
	private int viewSource;

	static class ListItemView {
		public ImageView imgIcon;
		public TextView txtName;
		public TextView txtAddr;
	}

	public ListViewClientAdapter(Context context, List<T> data, int resource) {
		this.layout = LayoutInflater.from(context);
		this.viewSource = resource;
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
		// TODO Auto-generated method stub

		ListItemView listItemView = null;
		if (contentView == null)
		{
			contentView = layout.inflate(this.viewSource, null);
			listItemView = new ListItemView();
			listItemView.txtAddr = (TextView) contentView
					.findViewById(R.id.select_client_address);
			listItemView.txtName = (TextView) contentView
					.findViewById(R.id.select_client_clientname);
			listItemView.imgIcon = (ImageView) contentView
					.findViewById(R.id.select_client_icon);

			contentView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) contentView.getTag();
		}	

		T obj = list.get(position);
		if (obj instanceof ClientInfo)
		{
			ClientInfo info = (ClientInfo) obj;

			listItemView.txtName.setText(info.ClientName);
			listItemView.txtName.setTag(info);
			listItemView.imgIcon.setImageResource(R.drawable.client_icon);
			String address = getStr(info.Address1, ",")
					+ getStr(info.City, ", ") + info.State;
			listItemView.txtAddr.setText(address);
		}
		else
		{
			LocationInfo info = (LocationInfo) obj;

			listItemView.txtName.setText(info.Name);
			listItemView.txtName.setTag(info);
			listItemView.imgIcon.setImageResource(R.drawable.location_icon);
			String address = getStr(info.Address1, ",")
					+ getStr(info.City, ", ") + info.State;
			listItemView.txtAddr.setText(address);
		}

		return contentView;

	}

	private String getStr(String str, String split)
	{
		if (!StringUtils.isEmpty(str))
		{
			return str + ", ";
		}
		else
			return "";
	}

}
