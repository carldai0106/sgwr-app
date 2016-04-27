package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotifyTypeInfo implements Serializable {
	@JsonProperty("ID")
	public int ID;
	@JsonProperty("NotifyType")
	public String NotifyType;
	@JsonProperty("NotifyDesc")
	public String NotifyDesc;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedDate")
	public Date CreatedDate;
	@JsonProperty("ChangedBy")
	public String ChangedBy;
	@JsonProperty("ChangedDate")
	public Date ChangedDate;
}
