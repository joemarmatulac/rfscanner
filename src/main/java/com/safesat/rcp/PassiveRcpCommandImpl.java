package com.safesat.rcp;

import com.ad.comm.CommStatus;
import com.ad.comm.StatusEventArg;
import com.ad.rfid.OnProtocolListener;
import com.ad.rfid.ProtocolEventArg;
import com.ad.rfid.ProtocolStandard;
import com.ad.rfid.passive.PassiveBaseParameters;
import com.ad.rfid.passive.PassiveCommand;
import com.ad.rfid.passive.PassiveRcp;
import com.ad.utils.ConverterUtil;
import com.safesat.http.HttpWebUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PassiveRcpCommandImpl implements PassiveRcpCommand {
    private String httpEndPoint;
    private PassiveBaseParameters passiveBaseParameters;
    private PassiveRcp passiveRcp;

    public PassiveRcpCommandImpl(PassiveBaseParameters passiveBaseParameters, PassiveRcp passiveRcp, String httpEndPoint) {
        this.passiveBaseParameters = passiveBaseParameters;
        this.passiveRcp = passiveRcp;
        this.httpEndPoint = httpEndPoint;
    }

    @Override
    public void initializePassiveRcp(String host, int port, int communicationType) {
        passiveRcp.setOnProtocolListener(new OnProtocolListener() {
            @Override
            public void onReceived(Object o, ProtocolEventArg protocolEventArg) {
                byte[] receivedBytes = protocolEventArg.getData();
                System.out.println("recv data Len1: " + receivedBytes.length + " Recv data: " + ConverterUtil.ByteArrayToHexString(receivedBytes));
                setCommands(protocolEventArg);
            }

            @Override
            public void onStatus(Object o, StatusEventArg statusEventArg) {
                System.out.println("Object1: " + statusEventArg.toString() + " status: " + CommStatus.format(statusEventArg.getStatus()) + " msg: " + statusEventArg.getMsg());
                status(statusEventArg);
            }
        });
        passiveRcp.connect(host, port, communicationType);
    }

    @Override
    public void setCommands(ProtocolEventArg event) {
        ProtocolStandard psData = event.getProtocolStandard();

        switch (psData.Code) {
            case PassiveRcp.RCP_CMD_INFO:
                System.out.println("Response Information [Type: " + passiveRcp.getType() + " " +
                        "Mode: " + passiveRcp.getMode() + " " +
                        "Version: " + passiveRcp.getVersion() + " " +
                        "Address: " + passiveRcp.getAddress() + "]");
                break;
            case PassiveRcp.RCP_CMD_PARA:
                if (psData.Length > 0 && psData.Type == 0) {
                    passiveBaseParameters.AddRange(psData.Payload);
                    String strpara = ConverterUtil.ByteArrayToHexString(psData.Payload, 0, psData.Length);
                    System.out.println("Response parameters [" + strpara + "]");
                } else if (psData.Type == 0) {
                    System.out.println("Set parameters succeed!");
                } else {
                    System.out.println("Action parameters fail!");
                }
                break;
            case PassiveRcp.RCP_CMD_ISO6B_IDEN:
                if (psData.Length > 0 && psData.Type == 0x32) {
                    String epcStr = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
                    System.out.println("Auto Send 6B TID No.(hex):" + epcStr);
                } else if (psData.Length > 0 && psData.Type == 0x00) {
                    String epcStr = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
                    System.out.println("Response 6B TID No.(hex):" + epcStr);
                    try {
                        System.out.println("******* posting rfid tag TID to server");
                        HttpWebUtil.GET(new URI(httpEndPoint.concat(epcStr)));
                        System.out.println("******* posting rfid tag done");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Unidentified 6B TID tag!");
                }
                break;
            case PassiveRcp.RCP_CMD_ISO6B_RW:
                if (psData.Length > 0 && psData.Type == 0) {
                    final String strread = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
                    System.out.println("Response 6B data(hex):" + strread);
                } else if (psData.Length == 0 && psData.Type == 0) {
                    System.out.println("Write 6B succeed!");
                } else {
                    System.out.println("Action 6B fail!");
                }
                break;
            case PassiveRcp.RCP_CMD_EPC_IDEN:
                if (psData.Length > 0 && psData.Type == 0x32) {
                    String epcStr = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
                    System.out.println("Auto Send Epc No.(hex):" + epcStr);
                } else if (psData.Length > 0 && psData.Type == 0x00) {
                    String epcStr = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
                    System.out.println("Response Epc No.(hex):" + epcStr);
                    try {
                        System.out.println("******* posting rfid tag TID to server");
                        HttpWebUtil.GET(new URI(httpEndPoint.concat(epcStr)));
                        System.out.println("******* posting rfid tag TID done");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Unidentified Epc tag!");
                }
                break;
            case PassiveRcp.RCP_CMD_EPC_RW:
                if (psData.Length > 0 && psData.Type == 0) {
                    final String strread = ConverterUtil.ByteArrayToHexString(psData.Payload, 1, psData.Length - 1);
                    System.out.println("Response Epc data(hex):" + strread);

                } else if (psData.Length == 0 && psData.Type == 0) {
                    System.out.println("Write Epc succeed!");
                } else {
                    System.out.println("Action Epc fail!");
                }
                break;
            default:
                System.out.println("set commands error");
                break;
        }
    }

    @Override
    public void status(StatusEventArg eventArg) {
        switch (eventArg.getStatus()) {
            case CommStatus.CONNECTING:
                System.out.println(
                        " status: " + CommStatus.format(eventArg.getStatus()) +
                                " msg: " + eventArg.getMsg());
                break;
            case CommStatus.CONNECT_OK:
                System.out.println(
                        " status: " + CommStatus.format(eventArg.getStatus()) +
                                " msg: " + eventArg.getMsg());
                break;
            case CommStatus.CONNECT_EXCEPT:
                System.out.println(
                        " status: " + CommStatus.format(eventArg.getStatus()) +
                                " msg: " + eventArg.getMsg());
                break;
            case CommStatus.DISCONNECT_OK:
                System.out.println(
                        " status: " + CommStatus.format(eventArg.getStatus()) +
                                " msg: " + eventArg.getMsg());
                break;
            case CommStatus.DISCONNECT_EXCEPT:
                System.out.println(
                        " status: " + CommStatus.format(eventArg.getStatus()) +
                                " msg: " + eventArg.getMsg());
                break;
            case CommStatus.SEND_EXCEPT:
                System.out.println(
                        " status: " + CommStatus.format(eventArg.getStatus()) +
                                " msg: " + eventArg.getMsg());
                break;
            case CommStatus.RECEIVE_EXCEPT:
                System.out.println(
                        " status: " + CommStatus.format(eventArg.getStatus()) +
                                " msg: " + eventArg.getMsg());
                break;
            default:
                System.out.println("status error");
                break;
        }
    }

    @Override
    public void execute() throws InterruptedException {
        PassiveCommand.GetInformation(passiveRcp);
        Thread.sleep(1000);
        PassiveCommand.GetConfig(passiveRcp);
        Thread.sleep(1000);
        PassiveCommand.SetConfig(passiveRcp, passiveBaseParameters.ToArray());
        Thread.sleep(1000);
        PassiveCommand.Identify6C(passiveRcp);
        Thread.sleep(1000);
        //Target memory bank; 0 Reserved, 1 EPC, 2 TID, 3 User
        int iMem = 1;
        //Starting Address word pointer of memory bank
        int iStart = 2;
        //Data Length of read (byte Count)
        int iLength = 4;
        PassiveCommand.Read6C(passiveRcp, iMem, iStart, iLength);
        Thread.sleep(1000);
        iMem = 1;
        iStart = 2;
        iLength = 4;
        byte[] iData = new byte[]{0x01, 0x02, 0x03, 0x04};
        PassiveCommand.Write6C(passiveRcp, iMem, iStart, iLength, iData);
        Thread.sleep(1000);

        //8-7  	ʶ��6B��  ,������,�ڶ�����Χ���޿�������� ,��Ӧʱ��ϳ�
        //8-7  	Identify 6B
        PassiveCommand.Identify6B(passiveRcp);
        Thread.sleep(2000);

        //8-8  	��ȡ6B������,,6B���޷���,������,�ڶ�����Χ���޿�������� ,��Ӧʱ��ϳ�
        //8-8 	Read 6B
        iStart = 18;
        iLength = 4;
        PassiveCommand.Read6B(passiveRcp, iStart, iLength);
        Thread.sleep(2000);

        //8-9	д��6B������ ,,6B���޷��� ,������,�ڶ�����Χ���޿�������� ,��Ӧʱ��ϳ�
        //8-9	Write 6B
        iStart = 18;
        iLength = 4;
        iData = new byte[]{0x01, 0x02, 0x03, 0x04};
        PassiveCommand.Write6B(passiveRcp, iStart, iLength, iData);
        Thread.sleep(2000);
    }
}
