package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShiftTimeInfo implements Serializable {

	@JsonProperty("ID")
	public long ID;
	@JsonProperty("StartTime")
	public String StartTime;
	@JsonProperty("EndTime")
	public String EndTime;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedTime")
	public Date CreatedTime;
	@JsonProperty("ChangedBy")
	public String ChangedBy;
	@JsonProperty("ChangedTime")
	public Date ChangedTime;

}
