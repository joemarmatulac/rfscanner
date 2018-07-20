package com.safesat.rcp.model;

public class RfCmdTag extends RfTag {

	private String mode;
	private String version;
	private String type;
	private int address;
	private String param;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public void setParams(String strpara) {
		this.param= strpara;
	}
	public String getParams( ) {
		return this.param;
	}


	

}
