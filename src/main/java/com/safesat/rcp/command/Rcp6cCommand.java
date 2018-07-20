package com.safesat.rcp.command;

import java.security.InvalidParameterException;

import com.ad.rfid.OnProtocolListener;
import com.ad.rfid.passive.PassiveCommand;
import com.safesat.rcp.model.ConnectionType;

public class Rcp6cCommand extends RcpManager{
	public Rcp6cCommand(OnProtocolListener onProtocolListener, ConnectionType connection) {
		super(onProtocolListener, connection);
	}
	public void identify6c() {
		PassiveCommand.Identify6C(mADRcp);
	}
	public void read6c(int memory ,int startaddress,int length) {
		if(memory>3) {
			throw new InvalidParameterException(
					"Invalid memory: 0 Reserved, 1 EPC, 2 TID, 3 User ");
		}
		if(startaddress<2) {
			throw new InvalidParameterException(
					"Starting Address must be greater or equal to 2.");
		}
		PassiveCommand.Read6C(mADRcp, memory, startaddress, length);
	}
	public void write6c(int memory ,int startaddress,int length, byte[] data) {
		if(memory>3) {
			throw new InvalidParameterException(
					"Invalid memory: 0 Reserved, 1 EPC, 2 TID, 3 User ");
		}
		if(startaddress<2) {
			throw new InvalidParameterException(
					"Starting Address must be greater or equal to 2.");
		}
		PassiveCommand.Write6C(mADRcp, memory, startaddress, length,data);
	}		
	
	
}
