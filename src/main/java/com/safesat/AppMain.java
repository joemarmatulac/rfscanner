package com.safesat;

import com.ad.rfid.passive.PassiveBaseParameters;
import com.ad.rfid.passive.PassiveRcp;
import com.safesat.http.HttpWebUtil;
import com.safesat.rcp.PassiveRcpCommand;
import com.safesat.rcp.PassiveRcpCommandImpl;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AppMain {

    private static final String HOST = "localhost";
    private static final int PORT = 49152;
    private static final int COMM_TYPE = 1;
    private static String HTTP_END_POINT = "http://5c3e0e1e.ngrok.io/safesat/api/rfid/read?code=";

    public static void main(String[] args) {
        final String host = args[0] == null ? HOST : args[0];
        final int port = args[1] == null ? PORT : Integer.valueOf(args[1]);
        final int communicationType = args[2] == null ? COMM_TYPE : comType(args[2]);
        final String httpEndPoint = args[3] == null ? HTTP_END_POINT : args[3];
        final PassiveBaseParameters passiveBaseParameters = new PassiveBaseParameters();
        final PassiveRcp passiveRcp = new PassiveRcp();
        final PassiveRcpCommand passiveRcpCommand = new PassiveRcpCommandImpl(passiveBaseParameters, passiveRcp, httpEndPoint);
        passiveRcpCommand.initializePassiveRcp(host, port, communicationType);
        try {
            passiveRcpCommand.execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        passiveRcp.disConnect();
//        testHttpGet();
    }

    private static void testHttpGet() {
        try {
            HttpResponse get = HttpWebUtil.GET(new URI(HTTP_END_POINT.concat("0006178354")));
            System.out.println("******* " + get.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
}
