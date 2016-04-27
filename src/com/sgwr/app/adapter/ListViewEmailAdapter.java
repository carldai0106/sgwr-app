package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.R;
import com.sgwr.app.bean.AddressBookInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewEmailAdapter extends BaseAdapter {

	private Context context;
	private List<AddressBookInfo> listItems;
	private int itemViewResource;
	private LayoutInflater listContainer;

	static class ListItemView {
		public TextView txtName;
		public TextView txtEmail;
		public ImageView imgFlag;
		public CheckBox chkStatus;
	}

	public ListViewEmailAdapter(Context context, int source,
			List<AddressBookInfo> list) {
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
			listItemView.chkStatus = (CheckBox) itemView
					.findViewById(R.id.chk_email_status);
			listItemView.txtEmail = (TextView) itemView
					.findViewById(R.id.text_email);
			listItemView.txtName = (TextView) itemView
					.findViewById(R.id.text_first_last_name);
			listItemView.imgFlag = (ImageView) itemView
					.findViewById(R.id.imageview_flag);

			itemView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) itemView.getTag();
		}

		final AddressBookInfo info = this.listItems.get(position);
		final ListItemView view = listItemView;

		listItemView.txtName.setText(info.FirstName + "  " + info.LastName);
		listItemView.txtEmail.setText(info.Email);
		listItemView.chkStatus.setChecked(info.IsAccept);

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
		listItemView.txtName.setTag(info);
		return itemView;
	}

}
