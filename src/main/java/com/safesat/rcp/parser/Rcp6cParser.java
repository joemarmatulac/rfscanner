package com.safesat.rcp.parser;

import com.ad.rfid.ProtocolStandard;
import com.ad.utils.ConverterUtil;
import com.safesat.rcp.model.Rf6cTag;
public class Rcp6cParser {
	public static Rf6cTag getEpcReadWrite(ProtocolStandard psData) {
		Rf6cTag tag = new Rf6cTag();
		if (psData.Length > 0 && psData.Type == 0) {
		    final String strread = ConverterUtil.ByteArrayToHexString(psData.Payload,1,psData.Length-1);
			tag.setData(strread);
		}else if(psData.Length == 0 && psData.Type == 0) {
			tag.setSuccess(true);
			tag.setStatusMessage("Write Epc succeed!");
		}else{
			tag.setSuccess(false);
			tag.setStatusMessage("Action Epc fail!");
		}
		return tag;
	}
	public static Rf6cTag getEpcIdentity(ProtocolStandard psData) {
		Rf6cTag tag = new Rf6cTag();
		if (psData.Length > 0 && psData.Type == 0x32) {
		    String epcStr = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
			tag.setTid(epcStr);
		}
		else if (psData.Length > 0 && psData.Type == 0x00) {
		    String epcStr = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
			tag.setTid(epcStr);
		}
		else{
			tag.setStatusMessage("Unidentified Epc tag!");
		}
		return tag;
	}
}
