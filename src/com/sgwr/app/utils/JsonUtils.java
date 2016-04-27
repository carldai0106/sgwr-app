package com.sgwr.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sgwr.app.AppConfig;
import com.sgwr.app.AppException;

public class JsonUtils {
	
	public static <T> T DeserializeObject(String source, TypeReference<T> type)
			throws AppException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat formatter = new SimpleDateFormat(
				AppConfig.FORMATTER_DATE);		

		objectMapper.setDateFormat(formatter);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		T result = null;
		try
		{
			result = objectMapper.readValue(source, type);
		}
		catch (JsonParseException e)
		{
			// TODO Auto-generated catch block
			throw AppException.json(e);
		}
		catch (JsonMappingException e)
		{
			// TODO Auto-generated catch block
			throw AppException.json(e);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			throw AppException.io(e);
		}
		finally
		{
			objectMapper = null;
			System.gc();
		}

		return result;
	}
	
	public static <T> String SerializeObject(T obj)
	{
		ObjectMapper objectMapper = new ObjectMapper();
		JsonGenerator generator = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			SimpleDateFormat formater = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");

			objectMapper.configure(
					SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

			objectMapper.setDateFormat(formater);
			generator = objectMapper.getFactory().createGenerator(baos,
					JsonEncoding.UTF8);

			objectMapper.writeValue(generator, obj);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (generator != null)
			{
				try
				{
					generator.flush();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!generator.isClosed())
			{
				try
				{
					generator.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.gc();
		}

		return baos.toString();
	}
}
