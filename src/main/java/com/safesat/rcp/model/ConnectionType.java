package com.safesat.rcp.model;

import com.ad.comm.CommType;

public class ConnectionType {

	private  int commType = CommType.NET;
	private  String host = "192.168.2.116";
	private  int port = 49152;
	
	
	public ConnectionType(int commType, String host, int port) {
		super();
		this.commType = commType;
		this.host = host;
		this.port = port;
	}
	public int getCommType() {
		return commType;
	}
	public void setCommType(int commType) {
		this.commType = commType;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
