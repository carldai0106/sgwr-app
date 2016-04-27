package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo implements Serializable {
	
	@JsonProperty("ID")
	public long ID;
	@JsonProperty("UserName")
	public String UserName;
	@JsonProperty("Password")
	public String Password;
	@JsonProperty("FirstName")
	public String FirstName;
	@JsonProperty("LastName")
	public String LastName;
	@JsonProperty("GroupId")
	public String GroupId;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedTime")
	public Date CreatedTime;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;
	@JsonProperty("Email")
	public String Email;
	@JsonProperty("NT_GroupName")
	public String NT_GroupName;
	@JsonProperty("NT_Prority")
	public int NT_Prority;

}
