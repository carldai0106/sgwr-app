package com.sgwr.app.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkerPostsInfo implements Serializable {
	@JsonProperty("IsSelected")
	public boolean IsSelected;
	@JsonProperty("AssignedID")
	public int AssignedID;
	@JsonProperty("AssignedPost")
	public String AssignedPost;
	@JsonProperty("LocationID")
	public int LocationID;
	@JsonProperty("ClientID")
	public int ClientID;

}
