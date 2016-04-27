package com.sgwr.app.ui;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sgwr.app.AppException;
import com.sgwr.app.R;
import com.sgwr.app.adapter.ListViewContactTypeAdapter;
import com.sgwr.app.utils.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SelectContactType extends Activity {

	private ListView listview_select_type;
	private ImageButton dialog_title_btn_close;
	private long ActivityId = 0;
	private long IncidentId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_contact_type);

		Intent intent = getIntent();
		HashMap<String, String> maps = (HashMap<String, String>) intent
				.getSerializableExtra("maps");
		ActivityId = intent.getLongExtra("activityid", 0);
		IncidentId = intent.getLongExtra("incidentid", 0);

		String jsonType = maps.get("Type");
		final String jsonList = maps.get("List");

		Map<String, String> mapType = null;
		try
		{
			mapType = JsonUtils.DeserializeObject(jsonType,
					new TypeReference<Map<String, String>>() {
					});
		}
		catch (AppException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dialog_title_btn_close = (ImageButton) findViewById(R.id.dialog_title_btn_close);
		listview_select_type = (ListView) findViewById(R.id.listview_select_type);

		dialog_title_btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

		if (mapType != null)
		{
			ListViewContactTypeAdapter adapter = new ListViewContactTypeAdapter(
					this, mapType, R.layout.item_contact_type);
			listview_select_type.setAdapter(adapter);
		}

		listview_select_type.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				TextView txtNumber = (TextView) arg1
						.findViewById(R.id.text_type_number);
				if (txtNumber != null)
				{
					String strId = txtNumber.getTag().toString();

					Intent intent = new Intent(SelectContactType.this,
							IncidentContactList.class);
					intent.putExtra("activityid", ActivityId);
					intent.putExtra("incidentid", IncidentId);
					intent.putExtra("id", strId);
					intent.putExtra("json", jsonList);

					startActivity(intent);
					finish();
				}
			}
		});
	}
}
