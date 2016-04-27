package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.R;
import com.sgwr.app.bean.ActivityInfo;
import com.sgwr.app.utils.DateTimeUtils;
import com.sgwr.app.utils.StringUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewActivityAdapter extends BaseAdapter {

	private Context context;// 运行上下文
	private List<ActivityInfo> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合
		public TextView title;
		public TextView location;
		public TextView author;
		public TextView occurred_date;
		public ImageView flag;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewActivityAdapter(Context context, List<ActivityInfo> data,
			int resource) {
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
			listItemView.title = (TextView) convertView
					.findViewById(R.id.activity_listitem_title);
			listItemView.author = (TextView) convertView
					.findViewById(R.id.activity_listitem_author);
			listItemView.occurred_date = (TextView) convertView
					.findViewById(R.id.activity_listitem_occurred_date);
			listItemView.location = (TextView) convertView
					.findViewById(R.id.activity_listitem_location);
			listItemView.flag = (ImageView) convertView
					.findViewById(R.id.activity_listitem_flag);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		final ActivityInfo info = listItems.get(position);

		listItemView.title.setText(info.Description);
		listItemView.author.setText(info.CreatedBy);
		String occurred_date = info.NT_OccurredDate + " "
				+ info.NT_OccurredTime;

		String strDateTime = "";
		if (!StringUtils.isEmpty(occurred_date))
		{
			strDateTime = DateTimeUtils.Utc2Local(occurred_date,
					DateTimeUtils.DATE_TIME_NO_SECOND_PATTEN);
		}
		
		Log.i("occurred_date", occurred_date);

		listItemView.occurred_date.setText(strDateTime);
		listItemView.location.setText(info.NT_Location);
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

		listItemView.title.setTag(info);// 设置隐藏参数(实体类)

		return convertView;
	}

}
