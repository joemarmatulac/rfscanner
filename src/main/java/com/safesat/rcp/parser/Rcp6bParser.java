package com.safesat.rcp.parser;

import com.ad.rfid.ProtocolStandard;
import com.ad.utils.ConverterUtil;
import com.safesat.rcp.model.Rf6bTag;

public class Rcp6bParser {
	public static Rf6bTag getCmdIso6bRw(ProtocolStandard psData) {
		Rf6bTag tag = new Rf6bTag();
		if (psData.Length > 0 && psData.Type == 0) {
		    final String strread = ConverterUtil.ByteArrayToHexString(psData.Payload,1,psData.Length-1);
			tag.setTid(strread);
		}else if(psData.Length == 0 && psData.Type == 0) {
			tag.setSuccess(true);
			tag.setStatusMessage("Write 6B succeed!");
		}else{
			tag.setSuccess(false);
			tag.setStatusMessage("Action 6B fail!");
		}
		return tag;
	}
	public static Rf6bTag getIso6bIdentity(ProtocolStandard psData) {
		Rf6bTag tag = new Rf6bTag();
		if (psData.Length > 0 && psData.Type == 0x32) {
		    String epcStr = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
			tag.setTid(epcStr);
		}
		else if (psData.Length > 0 && psData.Type == 0x00) {
		    String epcStr = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
		    tag.setTid(epcStr);
		}
		else{
			tag.setStatusMessage("Unidentified 6B TID tag!");
		}
		return tag;
	}
}
