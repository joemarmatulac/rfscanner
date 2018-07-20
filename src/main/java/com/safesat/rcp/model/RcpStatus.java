package com.safesat.rcp.model;

import com.ad.comm.CommStatus;
import com.ad.comm.StatusEventArg;

public class RcpStatus {

	private String status;
	private String msg;

	public RcpStatus(String status, String msg) {
	
	}

	public RcpStatus(StatusEventArg s) {
		this.status= CommStatus.format(s.getStatus());
		this.msg= s.getMsg();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
