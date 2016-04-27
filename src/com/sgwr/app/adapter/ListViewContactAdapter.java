package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.R;
import com.sgwr.app.bean.IncidentContactInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewContactAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<IncidentContactInfo> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合
		public TextView userName;
		public TextView mobile;
		public TextView workPhone;
		public TextView homePhone;
		public ImageView flag;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewContactAdapter(Context context,
			List<IncidentContactInfo> data, int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}

	@Override
	public int getCount()
	{
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.userName = (TextView) convertView
					.findViewById(R.id.incident_contact_name);
			listItemView.mobile = (TextView) convertView
					.findViewById(R.id.text_mobile);
			listItemView.homePhone = (TextView) convertView
					.findViewById(R.id.text_homephone);
			listItemView.workPhone = (TextView) convertView
					.findViewById(R.id.text_workphone);
			listItemView.flag = (ImageView) convertView
					.findViewById(R.id.listitem_flag);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		final IncidentContactInfo info = listItems.get(position);

		listItemView.userName.setText(info.FirstName + " " + info.MiddleName
				+ " " + info.LastName);
		listItemView.mobile.setText(info.Mobile);
		listItemView.homePhone.setText(info.HomeTel);
		listItemView.workPhone.setText(info.WorkTel);
		if (info.IsSelected)
		{
			listItemView.flag.setImageResource(R.drawable.small_active);
		}
		else
		{
			listItemView.flag.setImageResource(R.drawable.small_inactive);
		}

		final ListItemView view = listItemView;

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				if (info.IsSelected)
				{
					info.IsSelected = false;
					view.flag.setImageResource(R.drawable.small_inactive);
				}
				else
				{
					info.IsSelected = true;
					view.flag.setImageResource(R.drawable.small_active);
				}
			}
		});

		listItemView.userName.setTag(info);// 设置隐藏参数(实体类)

		return convertView;
	}

}
