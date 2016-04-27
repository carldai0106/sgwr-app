package com.sgwr.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttachmentInfo implements Serializable {

	public boolean IsSelected;
	public boolean IsLoaded;
	@JsonProperty("Id")
	public String Id;
	@JsonProperty("FileName")
	public String FileName;
	@JsonProperty("FilePath")
	public String FilePath;
	@JsonProperty("Note")
	public String Note;
	@JsonProperty("ChangeBy")
	public String ChangeBy;
	@JsonProperty("ChangeTime")
	public Date ChangeTime;
	@JsonProperty("ActivityId")
	public String ActivityId;
}
