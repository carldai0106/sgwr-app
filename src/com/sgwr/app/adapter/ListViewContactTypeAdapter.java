package com.sgwr.app.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sgwr.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewContactTypeAdapter extends BaseAdapter {

	private Map<String, String> maps;
	private LayoutInflater layout;
	private int viewSource;
	private List<String> listKey;
	private List<String> listValue;

	static class ListItemView {
		public TextView txtType;
		public TextView txtNumber;
	}

	public ListViewContactTypeAdapter(Context context,
			Map<String, String> data, int resource) {
		this.layout = LayoutInflater.from(context);
		this.viewSource = resource;
		this.maps = data;

		listKey = new ArrayList<String>();
		listValue = new ArrayList<String>();

		for (Map.Entry<String, String> entry : data.entrySet())
		{
			listKey.add(entry.getKey());
			listValue.add(entry.getValue());
		}
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return maps.size();
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
			listItemView.txtType = (TextView) contentView
					.findViewById(R.id.text_contact_type);
			listItemView.txtNumber = (TextView) contentView
					.findViewById(R.id.text_type_number);

			contentView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) contentView.getTag();
		}
		String strVal = listValue.get(position);
		if (strVal != "")
		{
			String[] arr = strVal.split(",");
			
			String id = arr[0];
			String count = arr[1];

			listItemView.txtNumber.setText("[ " + count +" ]");
			listItemView.txtNumber.setTag(id);
		}

		listItemView.txtType.setText(listKey.get(position));
		return contentView;

	}

}
