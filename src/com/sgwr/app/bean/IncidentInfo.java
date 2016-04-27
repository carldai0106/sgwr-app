package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncidentInfo implements Serializable {
	@JsonProperty("ID")
	public long ID;
	@JsonProperty("CategoryId")
	public String CategoryId;
	@JsonProperty("SubCategoryId")
	public String SubCategoryId;
	@JsonProperty("Reporter")
	public String Reporter;
	@JsonProperty("OccurredTime")
	public Date OccurredTime;
	@JsonProperty("Priority")
	public String Priority;
	@JsonProperty("StateId")
	public String StateId;
	@JsonProperty("ActivityId")
	public String ActivityId;
	@JsonProperty("ClientId")
	public String ClientId;
	@JsonProperty("Class")
	public String Class;
//	@JsonProperty("UrgentID")
//	public String UrgentID;
//	@JsonProperty("NotifyIDs")
//	public String NotifyIDs;
//	@JsonProperty("NotifyTime")
//	public Date NotifyTime;
//	@JsonProperty("UrgentTime")
//	public Date UrgentTime;
	@JsonProperty("WitnessState")
	public String WitnessState;
	@JsonProperty("OccurredAddr")
	public String OccurredAddr;
	@JsonProperty("OccurredCaused")
	public String OccurredCaused;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("Affiliation")
	public String Affiliation;
	@JsonProperty("InjuryDesc")
	public String InjuryDesc;
	@JsonProperty("CheckStatus")
	public int CheckStatus;
	@JsonProperty("Title")
	public String Title;
	@JsonProperty("PrintName")
	public String PrintName;
	@JsonProperty("Signature")
	public String Signature;
	@JsonProperty("SignDate")
	public Date SignDate;
	@JsonProperty("Title1")
	public String Title1;
	@JsonProperty("PrintName1")
	public String PrintName1;
	@JsonProperty("Signature1")
	public String Signature1;
	@JsonProperty("SignDate1")
	public Date SignDate1;
	@JsonProperty("CreatedBy")
	public String CreatedBy;
	@JsonProperty("CreatedDate")
	public Date CreatedDate;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;
}
