package com.sgwr.app.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientInfo {
	@JsonProperty("Id")
	public long Id;
	@JsonProperty("ClientName")
	public String ClientName;
	@JsonProperty("StartDate")
	public String StartDate;
	@JsonProperty("EndDate")
	public String EndDate;
	@JsonProperty("Status")
	public String Status;
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
	@JsonProperty("Note")
	public String Note;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;

}
