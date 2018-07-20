package com.safesat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;

import com.ad.comm.StatusEventArg;
import com.ad.rfid.OnProtocolListener;
import com.ad.rfid.ProtocolEventArg;
import com.safesat.http.HttpWebUtil;
import com.safesat.posting.PostManager;
import com.safesat.rcp.model.ConnectionType;
import com.safesat.rcp.command.Rcp6cCommand;
import com.safesat.rcp.model.RcpStatus;
import com.safesat.rcp.model.Rf6cTag;

public class AppMain {
	private static PostManager postManager = new PostManager();
	private static final String HOST = "localhost";
	private static final int PORT = 49152;
	private static final int COMM_TYPE = 1;
	private static String HTTP_END_POINT = "http://5c3e0e1e.ngrok.io/safesat/api/rfid/read?code=";
	private enum CType{SERIAL(0),NET(1),USB(2),BLUETOOTH(3),BLE(4);
		private int cmd;
		CType(int cmd){
			this.cmd=cmd;
		} 
		public int getCmd() {
			return cmd;
		}
          
	};
	static Rcp6cCommand manager;
	private static String httpEndPoint;

	public static void main(String[] args) throws InterruptedException {
		OnProtocolListener listener = createListener();
		manager = new Rcp6cCommand(listener, getConnection(args));
		manager.connect();
		while (true) {
			manager.identify6c();
			Thread.sleep(100);
		}
	}

	private static OnProtocolListener createListener() {
		return new OnProtocolListener() {
			@Override
			public void onReceived(Object o, ProtocolEventArg protocolEventArg) {
				Rf6cTag tag =(Rf6cTag)manager.parse(protocolEventArg);
					if(tag.getTid()!=null)	{

						System.out.println("tag id:"+ tag.getTid());
				try {
					System.out.println(new URI(httpEndPoint.concat(tag.getTid())));
					HttpResponse get = HttpWebUtil.GET(new URI(httpEndPoint.concat(tag.getTid())));
					System.out.println(get.getStatusLine().getStatusCode()+ "###########ss");
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				}
			}

			@Override
			public void onStatus(Object o, StatusEventArg statusEventArg) {
				RcpStatus status = manager.getStatus(statusEventArg);
				System.out.println("Object1: " + statusEventArg.toString() + " status: " + status.getStatus() + " msg: "
						+ status.getMsg());

			}
		};
	}

	private static ConnectionType getConnection(String[] args) {
		final String host = args[0] == null ? HOST : args[0];
		final int port = args[1] == null ? PORT : Integer.valueOf(args[1]);
		final int type = args[2] == null ? COMM_TYPE : CType.valueOf(args[2]).getCmd();
		httpEndPoint = args[3] == null ? HTTP_END_POINT : args[3];
		postManager.setEndPoint(httpEndPoint);
		return new ConnectionType(type , host, port);
	}
}
