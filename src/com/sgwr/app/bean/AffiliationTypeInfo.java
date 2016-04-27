package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AffiliationTypeInfo implements Serializable {
	@JsonProperty("Id")
	public int Id;
	@JsonProperty("Affiliation")
	public String Affiliation;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedTime")
	public Date CreatedTime;
	@JsonProperty("ChangedBy")
	public String ChangedBy;
	@JsonProperty("ChangedTime")
	public Date ChangedTime;

}
