package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AreaInfo implements Serializable {
	@JsonProperty("Id")
	public int Id;
	@JsonProperty("Name")
	public String Name;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("ZoneId")
	public String ZoneId;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;
}
