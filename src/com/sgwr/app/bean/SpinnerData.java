package com.sgwr.app.bean;

public class SpinnerData {
	private String text;
	private Object value;

	public SpinnerData(String text, Object value) {
		this.text = text;
		this.value = value;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}
	
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return text;
	}
}
