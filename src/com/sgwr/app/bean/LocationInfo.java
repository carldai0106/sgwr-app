package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationInfo implements Serializable {
	@JsonProperty("Id")
	public int Id;
	@JsonProperty("Name")
	public String Name;
	@JsonProperty("Address1")
	public String Address1;
	@JsonProperty("Address2")
	public String Address2;
	@JsonProperty("City")
	public String City;
	@JsonProperty("State")
	public String State;
	@JsonProperty("Zip")
	public String Zip;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("ClientId")
	public String ClientId;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;

	@JsonProperty("LctnTypeId")
	public String LctnTypeId;

}
