package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActivityInfo implements Serializable {

	public boolean IsSelected;

	@JsonProperty("Id")
	public long Id;
	@JsonProperty("ShamID")
	public long ShamID;
	@JsonProperty("ActivityType")
	public String ActivityType;
	@JsonProperty("Status")
	public String Status;
	@JsonProperty("State")
	public String State;
	@JsonProperty("Visibility")
	public boolean Visibility;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;
	@JsonProperty("ClientId")
	public String ClientId;

	@JsonProperty("LocationId")
	public String LocationId;
	@JsonProperty("ZoneId")
	public String ZoneId;
	@JsonProperty("AreaId")
	public String AreaId;
	@JsonProperty("AssignedID")
	public int AssignedID;
	@JsonProperty("AssignedName")
	public String AssignedName;
	@JsonProperty("ShiftTime")
	public String ShiftTime;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedTime")
	public Date CreatedTime;

	@JsonProperty("NT_Client")
	public String NT_Client;
	@JsonProperty("NT_Location")
	public String NT_Location;

	@JsonProperty("NT_IsAlarm")
	public boolean NT_IsAlarm;
	@JsonProperty("NT_Zone")
	public String NT_Zone;
	@JsonProperty("NT_Area")
	public String NT_Area;
	@JsonProperty("NT_CheckStatus")
	public int NT_CheckStatus;
	@JsonProperty("NT_LogType")
	public String NT_LogType;
	@JsonProperty("NT_OccurredDate")
	public String NT_OccurredDate;
	@JsonProperty("NT_OccurredTime")
	public String NT_OccurredTime;
	@JsonProperty("Rpt_IsRead")
	public boolean Rpt_IsRead;
	@JsonProperty("NT_LocationType")
	public String NT_LocationType;
}
