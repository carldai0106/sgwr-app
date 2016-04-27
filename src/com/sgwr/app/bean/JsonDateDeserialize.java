package com.sgwr.app.bean;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.sgwr.app.AppConfig;

public class JsonDateDeserialize extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext arg1)
			throws IOException, JsonProcessingException
	{
		// TODO Auto-generated method stub

		SimpleDateFormat formater = new SimpleDateFormat(
				AppConfig.FORMATTER_DATE);
		try
		{
			Date date = formater.parse(parser.getText());

			DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,
					DateFormat.LONG, Locale.CHINESE);
			
			String str = format.format(date);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
