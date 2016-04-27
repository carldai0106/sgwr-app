package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.R;
import com.sgwr.app.bean.CheckDateItemInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ListViewMultiChkAdapter extends BaseAdapter {

	private Context context;
	private List<CheckDateItemInfo> listItems;
	private LayoutInflater listContainer;
	private int itemViewResource;

	public ListViewMultiChkAdapter(Context context,
			List<CheckDateItemInfo> list, int resource) {
		this.context = context;
		this.listItems = list;
		this.itemViewResource = resource;
		this.listContainer = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return listItems.size();
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
	public View getView(int position, View itemView, ViewGroup parentView)
	{
		CheckBox checkbox = null;
		// TODO Auto-generated method stub
		if (itemView == null)
		{
			itemView = listContainer.inflate(itemViewResource, null);
			checkbox = (CheckBox) itemView.findViewById(R.id.checkbox_status);

			itemView.setTag(checkbox);
		}
		else
		{
			checkbox = (CheckBox) itemView.getTag();
		}

		final CheckDateItemInfo info = listItems.get(position);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				// TODO Auto-generated method stub
				info.IsChecked = isChecked;
			}
		});

		checkbox.setChecked(info.IsChecked);
		checkbox.setText(info.Title);

		return itemView;
	}

}
