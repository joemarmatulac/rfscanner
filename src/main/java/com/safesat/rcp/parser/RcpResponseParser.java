package com.safesat.rcp.parser;

import com.ad.rfid.ProtocolStandard;
import com.ad.rfid.passive.PassiveBaseParameters;
import com.ad.rfid.passive.PassiveRcp;
import com.ad.utils.ConverterUtil;
import com.safesat.rcp.model.RfCmdTag;
import com.safesat.rcp.model.RfTag;

public class RcpResponseParser{


	public static RfCmdTag getCmdParam(ProtocolStandard psData,PassiveBaseParameters mBaseParameter) {
		RfCmdTag tag = new RfCmdTag();
		if (psData.Length > 0 && psData.Type == 0) {
			mBaseParameter.AddRange(psData.Payload);
		    String strpara = ConverterUtil.ByteArrayToHexString(psData.Payload, 0, psData.Length);
		    tag.setParams(strpara);
		}else if (psData.Type == 0)
		{
			tag.setSuccess(true);
			tag.setStatusMessage("Set parameters succeed!");
		}else{
			tag.setSuccess(true);
			tag.setStatusMessage("Set parameters fail!");
		}
		return tag;
	}
	public static RfCmdTag getCmdInfo(PassiveRcp mADRcp) {
		RfCmdTag tag = new RfCmdTag();
		tag.setMode(mADRcp.getMode());
		tag.setVersion(mADRcp.getVersion());
		tag.setType(mADRcp.getType());
		tag.setAddress(mADRcp.getAddress());
		return tag;
	}
}
