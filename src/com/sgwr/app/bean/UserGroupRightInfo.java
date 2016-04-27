package com.sgwr.app.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserGroupRightInfo implements Serializable {

	@JsonProperty("Id")
	public int Id;
	@JsonProperty("GroupId")
	public String GroupId;
	@JsonProperty("ModuleId")
	public int ModuleId;
	@JsonProperty("IsAdd")
	public boolean IsAdd;
	@JsonProperty("IsEdit")
	public boolean IsEdit;
	@JsonProperty("IsDelete")
	public boolean IsDelete;
	@JsonProperty("IsView")
	public boolean IsView;
	@JsonProperty("IsShow")
	public boolean IsShow;
	@JsonProperty("IsExport")
	public boolean IsExport;
	@JsonProperty("IsSearch")
	public boolean IsSearch;
	@JsonProperty("IsUsrGrpRight")
	public boolean IsUsrGrpRight;
	@JsonProperty("NT_GroupName")
	public String NT_GroupName;
	@JsonProperty("NT_ModCode")
	public String NT_ModCode;
	@JsonProperty("NT_ModName")
	public String NT_ModName;
	@JsonProperty("NT_ParentCode")
	public String NT_ParentCode;
	@JsonProperty("NT_Sequence")
	public int NT_Sequence;
	@JsonProperty("NT_SpaceCount")
	public int NT_SpaceCount;
}
