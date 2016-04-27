package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryInfo implements Serializable {
	@JsonProperty("Id")
	public int Id;
	@JsonProperty("TypeName")
	public String TypeName;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("ParentId")
	public int ParentId;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;
}
