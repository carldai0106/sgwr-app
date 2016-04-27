package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncidentContactInfo implements Serializable {
	public boolean IsSelected; 
	
	@JsonProperty("Id")
	public long Id;
	@JsonProperty("ContactType")
	public String ContactType;
	@JsonProperty("ContactID")
	public int ContactID;
	@JsonProperty("FirstName")
	public String FirstName;
	@JsonProperty("MiddleName")
	public String MiddleName;
	@JsonProperty("LastName")
	public String LastName;
	@JsonProperty("Addr1")
	public String Addr1;
	@JsonProperty("Addr2")
	public String Addr2;
	@JsonProperty("City")
	public String City;
	@JsonProperty("State")
	public String State;
	@JsonProperty("Zip")
	public String Zip;
	@JsonProperty("Height")
	public String Height;
	@JsonProperty("Weight")
	public String Weight;
	@JsonProperty("Gender")
	public String Gender;
	@JsonProperty("HomeTel")
	public String HomeTel;
	@JsonProperty("WorkTel")
	public String WorkTel;
	@JsonProperty("Mobile")
	public String Mobile;
	@JsonProperty("OtherTel")
	public String OtherTel;
	@JsonProperty("Note")
	public String Note;
	@JsonProperty("ID1Type")
	public String ID1Type;
	@JsonProperty("ID2Type")
	public String ID2Type;
	@JsonProperty("ID3Type")
	public String ID3Type;
	@JsonProperty("ID1")
	public String ID1;
	@JsonProperty("ID2")
	public String ID2;
	@JsonProperty("ID3")
	public String ID3;
	@JsonProperty("Make")
	public String Make;
	@JsonProperty("Model")
	public String Model;
	@JsonProperty("Year")
	public String Year;
	@JsonProperty("License")
	public String License;
	@JsonProperty("Color")
	public String Color;
	@JsonProperty("InsuranceCo")
	public String InsuranceCo;
	@JsonProperty("Policy")
	public String Policy;
	@JsonProperty("ActivityId")
	public String ActivityId;
	@JsonProperty("IncidentId")
	public String IncidentId;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;

}
