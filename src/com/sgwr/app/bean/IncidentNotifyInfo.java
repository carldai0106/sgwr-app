package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IncidentNotifyInfo implements Serializable {

	@JsonProperty("ID")
	public long ID;
	@JsonProperty("NotifyID")
	public int NotifyID;
	@JsonProperty("NotifyType")
	public String NotifyType;
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
	@JsonProperty("Rpt_NotifyDesc")
	public String Rpt_NotifyDesc;

}
