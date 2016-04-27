package com.sgwr.app.bean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgwr.app.AppConfig;
import com.sgwr.app.AppException;

public class Result<T> {

	@JsonProperty("ResultCode")
	public int ResultCode;
	@JsonProperty("ResultMsg")
	public String ResultMsg;
	@JsonProperty("TObj")
	public T TObj;

	public static <T> T parse(String jsonSource, TypeReference<T> type)
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
			result = objectMapper.readValue(jsonSource, type);
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
}
