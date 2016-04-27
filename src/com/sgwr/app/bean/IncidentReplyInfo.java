package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncidentReplyInfo implements Serializable {
	@JsonProperty("ID")
	public long ID;
	@JsonProperty("UrgentID")
	public int UrgentID;
	@JsonProperty("UrgentType")
	public String UrgentType;
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
	@JsonProperty("Rpt_UrgentDesc")
	public String Rpt_UrgentDesc;
}
