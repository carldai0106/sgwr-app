package com.sgwr.app.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sgwr.app.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.LinearLayout.LayoutParams;

public class DateTimePickerDialog extends Activity {
	private Button btnCancel, btnOk;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private LinearLayout pickerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_datetimepicker);

		btnCancel = (Button) findViewById(R.id.datetimepicker_btn_cancel);
		btnOk = (Button) findViewById(R.id.datetimepicker_btn_ok);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		pickerLayout = (LinearLayout) findViewById(R.id.pickerLayout);

		int version = VERSION.SDK_INT;
		if (version >= 11)
		{
			pickerLayout.setOrientation(LinearLayout.HORIZONTAL);
			resizePicker(datePicker);
			resizePicker(timePicker);
		}
		else
		{
			pickerLayout.setOrientation(LinearLayout.VERTICAL);
		}

		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(Calendar.getInstance().get(
				Calendar.HOUR_OF_DAY));

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				int year = datePicker.getYear();
				int month = datePicker.getMonth() + 1;
				int day = datePicker.getDayOfMonth();
				int hour = timePicker.getCurrentHour();
				int minute = timePicker.getCurrentMinute();
				String datetime = year + "-" + addZero(month) + "-"
						+ addZero(day) + " " + addZero(hour) + ":"
						+ addZero(minute);
				Intent intent = new Intent();
				intent.putExtra("datetime", datetime);

				// 设置Activity回传值
				setResult(2, intent);
				finish();
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void resizePicker(ViewGroup viewGroup)
	{
		List<NumberPicker> npList = findNumberPicker(viewGroup);

		for (NumberPicker np : npList)
		{
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					85, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(1, 0, 1, 0);
			np.setLayoutParams(params);
		}

		for (NumberPicker np : npList)
		{
			EditText edt = null;
			int count = np.getChildCount();
			for (int i = 0; i < count; i++)
			{
				View child = np.getChildAt(i);
				if (child instanceof EditText)
				{
					edt = (EditText) child;
					break;
				}
			}

			edt.setFocusable(false);
			edt.setGravity(Gravity.CENTER);
			edt.setTextSize(14);
		}
	}

	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup)
	{
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;

		if (null != viewGroup)
		{
			int count = viewGroup.getChildCount();
			for (int i = 0; i < count; i++)
			{
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker)
				{
					npList.add((NumberPicker) child);
				}
				else if (child instanceof LinearLayout)
				{
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0)
					{
						return result;
					}
				}
			}
		}

		return npList;
	}

	private String addZero(int val)
	{
		return val < 10 ? "0" + val : val + "";
	}
}
