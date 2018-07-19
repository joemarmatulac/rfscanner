package com.safesat;

import com.ad.comm.CommStatus;
import com.ad.comm.StatusEventArg;
import com.ad.rfid.OnProtocolListener;
import com.ad.rfid.ProtocolEventArg;
import com.ad.rfid.ProtocolStandard;
import com.ad.rfid.passive.PassiveBaseParameters;
import com.ad.rfid.passive.PassiveCommand;
import com.ad.rfid.passive.PassiveRcp;
import com.ad.utils.ConverterUtil;

public class AppMain {
    private static PassiveBaseParameters mBaseParameter = new PassiveBaseParameters();
    private static PassiveRcp mADRcp = new PassiveRcp();

    public static void main(String[] args) {
        init();
        connect(args[0], Integer.valueOf(args[1]), comType(args[2]));
        try {
            runs();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disConnect();
    }

    private static int comType(String arg) {
        int commType;
        switch (arg) {
            case "SERIAL":
                commType = 0;
                break;
            case "NET":
                commType = 1;
                break;
            case "USB":
                commType = 2;
                break;
            case "BLUETOOTH":
                commType = 3;
                break;
            case "BLE":
                commType = 4;
                break;
            default:
                commType = 1;
                break;
        }
        return commType;
    }

    private static void init() {
        mADRcp.setOnProtocolListener(new OnProtocolListenerImpl());
    }


    private static void statuss(StatusEventArg arg1) {
        switch (arg1.getStatus()) {
            case CommStatus.CONNECTING:
                System.out.println(
                        " status: " + CommStatus.format(arg1.getStatus()) +
                                " msg: " + arg1.getMsg());
                break;
            case CommStatus.CONNECT_OK:
                System.out.println(
                        " status: " + CommStatus.format(arg1.getStatus()) +
                                " msg: " + arg1.getMsg());
                break;
            case CommStatus.CONNECT_EXCEPT:
                System.out.println(
                        " status: " + CommStatus.format(arg1.getStatus()) +
                                " msg: " + arg1.getMsg());
                break;
            case CommStatus.DISCONNECT_OK:
                System.out.println(
                        " status: " + CommStatus.format(arg1.getStatus()) +
                                " msg: " + arg1.getMsg());
                break;
            case CommStatus.DISCONNECT_EXCEPT:
                System.out.println(
                        " status: " + CommStatus.format(arg1.getStatus()) +
                                " msg: " + arg1.getMsg());
                break;
            case CommStatus.SEND_EXCEPT:
                System.out.println(
                        " status: " + CommStatus.format(arg1.getStatus()) +
                                " msg: " + arg1.getMsg());
                break;
            case CommStatus.RECEIVE_EXCEPT:
                System.out.println(
                        " status: " + CommStatus.format(arg1.getStatus()) +
                                " msg: " + arg1.getMsg());
                break;
        }
    }

    private static void func(ProtocolEventArg event) {
        ProtocolStandard psData = event.getProtocolStandard();

        switch (psData.Code) {
            case PassiveRcp.RCP_CMD_INFO:
                System.out.println("Response Information [Type: " + mADRcp.getType() + " " +
                        "Mode: " + mADRcp.getMode() + " " +
                        "Version: " + mADRcp.getVersion() + " " +
                        "Address: " + mADRcp.getAddress() + "]");
                break;
            case PassiveRcp.RCP_CMD_PARA:
                if (psData.Length > 0 && psData.Type == 0) {
                    mBaseParameter.AddRange(psData.Payload);
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
        }
    }

    private static void connect(String mHost, int mPort, int mCommType) {
        mADRcp.connect(mHost, mPort, mCommType);
    }

    private static void runs() throws InterruptedException {
        PassiveCommand.GetInformation(mADRcp);
        Thread.sleep(1000);
        PassiveCommand.GetConfig(mADRcp);
        Thread.sleep(1000);
        PassiveCommand.SetConfig(mADRcp, mBaseParameter.ToArray());
        Thread.sleep(1000);
        PassiveCommand.Identify6C(mADRcp);
        Thread.sleep(1000);
        //Target memory bank; 0 Reserved, 1 EPC, 2 TID, 3 User
        int iMem = 1;
        //Starting Address word pointer of memory bank
        int iStart = 2;
        //Data Length of read (byte Count)
        int iLength = 4;
        PassiveCommand.Read6C(mADRcp, iMem, iStart, iLength);
        Thread.sleep(1000);
        iMem = 1;
        iStart = 2;
        iLength = 4;
        byte[] iData = new byte[]{0x01, 0x02, 0x03, 0x04};
        PassiveCommand.Write6C(mADRcp, iMem, iStart, iLength, iData);
        Thread.sleep(1000);

        //8-7  	ʶ��6B��  ,������,�ڶ�����Χ���޿�������� ,��Ӧʱ��ϳ�
        //8-7  	Identify 6B
        PassiveCommand.Identify6B(mADRcp);
        Thread.sleep(2000);

        //8-8  	��ȡ6B������,,6B���޷���,������,�ڶ�����Χ���޿�������� ,��Ӧʱ��ϳ�
        //8-8 	Read 6B
        iStart = 18;
        iLength = 4;
        PassiveCommand.Read6B(mADRcp, iStart, iLength);
        Thread.sleep(2000);

        //8-9	д��6B������ ,,6B���޷��� ,������,�ڶ�����Χ���޿�������� ,��Ӧʱ��ϳ�
        //8-9	Write 6B
        iStart = 18;
        iLength = 4;
        iData = new byte[]{0x01, 0x02, 0x03, 0x04};
        PassiveCommand.Write6B(mADRcp, iStart, iLength, iData);
        Thread.sleep(2000);
    }

    private static void disConnect() {
        mADRcp.disConnect();
    }

    private static class OnProtocolListenerImpl implements OnProtocolListener {

        @Override
        public void onReceived(Object obj, ProtocolEventArg event) {
            byte[] receivedBytes = event.getData();
            System.out.println("recv data Len1: " + receivedBytes.length + " Recv data: " + ConverterUtil.ByteArrayToHexString(receivedBytes));
            func(event);
        }

        @Override
        public void onStatus(Object arg0, StatusEventArg arg1) {
            System.out.println("Object1: " + arg0.toString() + " status: " + CommStatus.format(arg1.getStatus()) + " msg: " + arg1.getMsg());
            statuss(arg1);
        }

    }
}
