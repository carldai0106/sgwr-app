package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConditionTypeInfo implements Serializable {
	public boolean IsSelected;
	@JsonProperty("Id")
	public int Id;
	@JsonProperty("CndType")
	public String CndType;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedTime")
	public Date CreatedTime;
	@JsonProperty("ChangedBy")
	public String ChangedBy;
	@JsonProperty("ChangedTime")
	public Date ChangedTime;
}
