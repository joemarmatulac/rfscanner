package com.safesat.rcp.model;

public class RfTag {
	private  String tid;
	private  String data;
	private  boolean success;
	private  String statusMessage;

public String getTid() {
	return tid;
}

public void setTid(String tid) {
	this.tid = tid;
}

public String getStatusMessage() {
	return statusMessage;
}

public void setStatusMessage(String statusMessage) {
	this.statusMessage = statusMessage;
}

public boolean isSuccess() {
	return success;
}

public void setSuccess(boolean success) {
	this.success = success;
}

public String getData() {
	return data;
}

public void setData(String data) {
	this.data = data;
}
}
