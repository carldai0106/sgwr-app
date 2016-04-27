package com.sgwr.app.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {

	private List<View> viewList;

	public ViewPagerAdapter(List<View> list) {
		this.viewList = list;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{

		return arg0 == arg1;
	}

	@Override
	public int getCount()
	{
		return viewList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView(viewList.get(position));
	}

	@Override
	public int getItemPosition(Object object)
	{

		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		container.addView(viewList.get(position));
		return viewList.get(position);
	}

}
