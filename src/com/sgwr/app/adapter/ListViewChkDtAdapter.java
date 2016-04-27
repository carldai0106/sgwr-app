package com.sgwr.app.adapter;

import java.util.List;

import com.sgwr.app.R;
import com.sgwr.app.bean.CheckDateItemInfo;
import com.sgwr.app.ui.DateTimePickerDialog;
import com.sgwr.app.ui.CheckDateListDialog;
import com.sgwr.app.ui.CheckDateListDialog.IActivityResult;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ListViewChkDtAdapter extends BaseAdapter {

	private Context context;
	private List<CheckDateItemInfo> listItems;
	private LayoutInflater listContainer;
	private int itemViewResource;

	static class ListItemView {
		public CheckBox chk_status;
		public TextView text_datetime;
		public EditText edit_datetime;
		public ImageButton imgbtn_clear;
	}

	public ListViewChkDtAdapter(Context context, List<CheckDateItemInfo> data,
			int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.listItems = data;
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
			itemView = listContainer.inflate(itemViewResource, null);
			listItemView = new ListItemView();
			listItemView.chk_status = (CheckBox) itemView
					.findViewById(R.id.chk_dt_status);
			listItemView.text_datetime = (TextView) itemView
					.findViewById(R.id.chk_dt_text_datetime);
			listItemView.edit_datetime = (EditText) itemView
					.findViewById(R.id.chk_dt_edt_datetime);
			listItemView.imgbtn_clear = (ImageButton) itemView
					.findViewById(R.id.chk_dt_imgbtn_clear);

			itemView.setTag(listItemView);
		}
		else
		{
			listItemView = (ListItemView) itemView.getTag();
		}

		final CheckDateItemInfo info = listItems.get(position);

		CheckBox chk_status = listItemView.chk_status;

		chk_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				// TODO Auto-generated method stub
				info.IsChecked = isChecked;
			}
		});

		chk_status.setChecked(info.IsChecked);
		chk_status.setText(info.Title);

		final EditText edt_datetime = listItemView.edit_datetime;
		listItemView.imgbtn_clear
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						edt_datetime.setText("");
					}
				});

		edt_datetime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, DateTimePickerDialog.class);

				if (context instanceof CheckDateListDialog)
				{
					CheckDateListDialog activity = (CheckDateListDialog) context;
					activity.startActivityForResult(intent, 1);
					activity.setActivityResult(new IActivityResult() {

						@Override
						public void setIntentResult(Intent data)
						{
							// TODO Auto-generated method stub
							String datetime = data.getStringExtra("datetime");
							info.DateTime = datetime;
							edt_datetime.setText(datetime);
						}
					});
				}
			}
		});

		String date = info.DateTime;
		listItemView.edit_datetime.setText(date);

		return itemView;
	}

}
