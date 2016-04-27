package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UrgentReplyTypeInfo implements Serializable {
	@JsonProperty("ID")
	public int ID;
	@JsonProperty("RspnType")
	public String RspnType;
	@JsonProperty("RspnDesc")
	public String RspnDesc;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedDate")
	public Date CreatedDate;
	@JsonProperty("ChangedBy")
	public String ChangedBy;
	@JsonProperty("ChangedDate")
	public Date ChangedDate;


}
