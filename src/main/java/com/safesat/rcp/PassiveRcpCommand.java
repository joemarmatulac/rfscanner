package com.safesat.rcp;

import com.ad.comm.StatusEventArg;
import com.ad.rfid.ProtocolEventArg;

public interface PassiveRcpCommand {
    void setCommands(ProtocolEventArg event);
    void status(StatusEventArg eventArg);
    void initializePassiveRcp(String host, int port, int communicationType);
    void execute() throws InterruptedException;
}
