package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactTypeInfo implements Serializable {
	@JsonProperty("ID")
	public int ID;
	@JsonProperty("ContactType")
	public String ContactType;
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
