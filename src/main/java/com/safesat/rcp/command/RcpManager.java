package com.safesat.rcp.command;

import com.ad.comm.StatusEventArg;
import com.ad.rfid.OnProtocolListener;
import com.ad.rfid.ProtocolEventArg;
import com.ad.rfid.ProtocolStandard;
import com.ad.rfid.passive.PassiveBaseParameters;
import com.ad.rfid.passive.PassiveCommand;
import com.ad.rfid.passive.PassiveRcp;
import com.safesat.rcp.model.ConnectionType;
import com.safesat.rcp.model.RcpStatus;
import com.safesat.rcp.model.RfTag;
import com.safesat.rcp.parser.Rcp6bParser;
import com.safesat.rcp.parser.Rcp6cParser;
import com.safesat.rcp.parser.RcpResponseParser;

public class RcpManager {
	private ConnectionType connection;
	private static PassiveBaseParameters mBaseParameter = new PassiveBaseParameters();
	protected PassiveRcp mADRcp = new PassiveRcp();
	private OnProtocolListener onProtocolListener;
	public RcpManager(OnProtocolListener onProtocolListener,ConnectionType connection ) {
		this.onProtocolListener=onProtocolListener;
		this.connection=connection;
	}
	public  void connect(){
		mADRcp.addOnProtocolListener(onProtocolListener);
		mADRcp.connect(connection.getHost(),connection.getPort(), connection.getCommType());
	
	}
	public  void disConnect(){
		mADRcp.disConnect();
	}
	public RcpStatus getStatus(StatusEventArg arg1){
			return new RcpStatus(arg1);
	}
	
	public void getInformation() {
		PassiveCommand.GetInformation(mADRcp);
	}
	public void getConfig() {
		PassiveCommand.GetConfig(mADRcp);
	}	
	public void setConfig(PassiveBaseParameters param) {
		mBaseParameter=param;
		PassiveCommand.SetConfig(mADRcp,param.ToArray());
	}
	
	public RfTag parse(ProtocolEventArg event) {
	ProtocolStandard psData = event.getProtocolStandard();
		switch(psData.Code){
        case PassiveRcp.RCP_CMD_INFO:
        	return RcpResponseParser.getCmdInfo(mADRcp);
        case PassiveRcp.RCP_CMD_PARA:
        	return RcpResponseParser.getCmdParam(psData,mBaseParameter);
        case PassiveRcp.RCP_CMD_ISO6B_IDEN:
        	return Rcp6bParser.getIso6bIdentity(psData);
        case PassiveRcp.RCP_CMD_ISO6B_RW:
        	return Rcp6bParser.getCmdIso6bRw(psData);
        case PassiveRcp.RCP_CMD_EPC_IDEN:
        	return Rcp6cParser.getEpcIdentity(psData);
        case PassiveRcp.RCP_CMD_EPC_RW:
        	return Rcp6cParser.getEpcReadWrite(psData);
		}
		return null;
	}
}	
