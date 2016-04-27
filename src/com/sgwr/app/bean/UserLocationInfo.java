package com.sgwr.app.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLocationInfo implements Serializable {
	@JsonProperty("IsSelected")
	public boolean IsSelected;
	@JsonProperty("UserId")
	public String UserId;
	@JsonProperty("LocationId")
	public String LocationId;
	@JsonProperty("ClientId")
	public String ClientId;
	@JsonProperty("NT_Id")
	public String NT_Id;
	@JsonProperty("NT_Name")
	public String NT_Name;
	@JsonProperty("NT_Address1")
	public String NT_Address1;
	@JsonProperty("NT_State")
	public String NT_State;
	@JsonProperty("NT_City")
	public String NT_City;

}
