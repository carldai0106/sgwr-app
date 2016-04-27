package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncidentConditionInfo implements Serializable {
	
	public boolean IsSelected;
	@JsonProperty("ID")
	public long ID;
	@JsonProperty("CndID")
	public int CndID;
	@JsonProperty("CndType")
	public String CndType;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("ActivityID")
	public long ActivityID;
	@JsonProperty("IncidentID")
	public long IncidentID;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedTime")
	public Date CreatedTime;
	@JsonProperty("ChangedBy")
	public String ChangedBy;
	@JsonProperty("ChangedTime")
	public Date ChangedTime;
}
