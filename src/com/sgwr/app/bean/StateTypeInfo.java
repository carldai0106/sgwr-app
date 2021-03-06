package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StateTypeInfo implements Serializable {
	@JsonProperty("IsSelected")
	public boolean IsSelected;
	@JsonProperty("Id")
	public int Id;
	@JsonProperty("TypeName")
	public String TypeName;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;
}
