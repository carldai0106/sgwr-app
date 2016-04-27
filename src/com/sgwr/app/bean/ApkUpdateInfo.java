package com.sgwr.app.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApkUpdateInfo implements Serializable {
	@JsonProperty("VersionCode")
	public int VersionCode;
	@JsonProperty("VersionName")
	public String VersionName;
	@JsonProperty("DownloadUrl")
	public String DownloadUrl;
	@JsonProperty("UpdateLog")
	public String UpdateLog;
}
