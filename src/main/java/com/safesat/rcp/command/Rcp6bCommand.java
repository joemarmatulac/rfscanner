package com.safesat.rcp.command;

import com.ad.rfid.OnProtocolListener;
import com.ad.rfid.passive.PassiveBaseParameters;
import com.ad.rfid.passive.PassiveCommand;
import com.safesat.rcp.model.ConnectionType;

public class Rcp6bCommand  extends RcpManager{
	public Rcp6bCommand(OnProtocolListener onProtocolListener, ConnectionType connection) {
		super(onProtocolListener, connection);
	}
	public void identify6B(PassiveBaseParameters param) {
		PassiveCommand.Identify6B(mADRcp);
	}
	//default params are start=18, length=4
	public void read6B(int iLength, int iStart) {
		PassiveCommand.Read6B(mADRcp, iStart, iLength);
	}
	//default params are start=18, length=4
	public void write6B(int iLength, int iStart, byte[] data) {
		PassiveCommand.Write6B(mADRcp, iStart, iLength,data);
	}
}
