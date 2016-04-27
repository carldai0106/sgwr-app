package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressBookInfo implements Serializable {
	
	public boolean IsSelected;
	@JsonProperty("IsAccept")
	public boolean IsAccept;
	@JsonProperty("Id")
	public long Id;
	@JsonProperty("FirstName")
	public String FirstName;
	@JsonProperty("LastName")
	public String LastName;
	@JsonProperty("Company")
	public String Company;
	@JsonProperty("Title")
	public String Title;
	@JsonProperty("Email")
	public String Email;
	@JsonProperty("HomeTel")
	public String HomeTel;
	@JsonProperty("WorkTel")
	public String WorkTel;
	@JsonProperty("Mobile")
	public String Mobile;
	@JsonProperty("OtherTel")
	public String OtherTel;
	@JsonProperty("Addr1")
	public String Addr1;
	@JsonProperty("Addr2")
	public String Addr2;
	@JsonProperty("Note")
	public String Note;
	@JsonProperty("TypeName")
	public String TypeName;
	@JsonProperty("ForeignId")
	public String ForeignId;
	@JsonProperty("ClientId")
	public String ClientId;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;
	@JsonProperty("City")
	public String City;
	@JsonProperty("State")
	public String State;
	@JsonProperty("ZipCode")
	public String ZipCode;

}
