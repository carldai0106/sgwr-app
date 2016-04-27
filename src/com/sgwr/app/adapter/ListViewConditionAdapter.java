package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.R;
import com.sgwr.app.bean.IncidentConditionInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewConditionAdapter extends BaseAdapter {

	private Context context;
	private List<IncidentConditionInfo> listItems;
	private int itemViewResource;
	private LayoutInflater listContainer;

	static class ListItemView {
		public TextView txtCndType;
		public TextView txtDesc;
		public ImageView imgFlag;
	}

	public ListViewConditionAdapter(Context context, int source,
			List<IncidentConditionInfo> list) {
		this.context = context;
		this.listItems = list;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = source;
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
		// TODO Auto-generated method stub
		ListItemView listItemView = null;
		if (itemView == null)
		{
			itemView = listContainer.inflate(this.itemViewResource, null);
			listItemView = new ListItemView();
			listItemView.txtCndType = (TextView) itemView
					.findViewById(R.id.text_condition);
			listItemView.txtDesc = (TextView) itemView
					.findViewById(R.id.text_description);
			listItemView.imgFlag = (ImageView) itemView
					.findViewById(R.id.imageview_flag);

			itemView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) itemView.getTag();
		}

		final IncidentConditionInfo info = this.listItems.get(position);
		final ListItemView view = listItemView;

		listItemView.txtCndType.setText(info.CndType);

		listItemView.txtDesc.setText(info.Description);

		if (info.IsSelected)
		{
			listItemView.imgFlag.setImageResource(R.drawable.active);
		}
		else
		{
			listItemView.imgFlag.setImageResource(R.drawable.inactive);
		}

		itemView.setOnClickListener(new OnClickListener() {
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
			}
		});

		return itemView;
	}

}
