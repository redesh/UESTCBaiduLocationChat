package mix;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 单个服务端与客户端连接程序
 * 用以维持服务器与单个客户端的连接线程，负责接收客户端发来的信息
 * @author monsieurchak
 *
 */
public class ClientThread extends Thread{
	
	//代表服务器端通过accept方法获得的客户端连接
	Socket clientSocket;
	
	//服务器端中存储的Socket对象的数据输入、输出流
	DataInputStream in = null;
	DataOutputStream out = null;
	
	//声明ServerThread对象
	ServerThread serverThread;
	String str;
	
	//当前连接到服务器端的客户端数量
	public static int ConnectNumber = 0;
	
	public ClientThread(Socket socket, ServerThread serverThread) {
		clientSocket = socket;
		this.serverThread = serverThread;
		try {
			
			//实例化客户端Socket的数据流
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.out.println("建立I/O通道失败：" + e.toString());
			System.exit(3);
		}
	}
	
	@Override
	public void run() {
		/**
		 * charles批注:
		 * 要不要加一个延迟轮询，要不然本线程不是忙死了？
		 */
		while (true) {
			try {
				
				//读入客户端发送来的信息
				String message = in.readUTF();
				synchronized (serverThread.messages) {
					if (message != null) {
						
						//将客户端发送来的信息存储于serverThread的messages数组中
						serverThread.messages.addElement(message);
						
						//服务器端文本框显示新消息
						Server.jTextArea1.append(message + '\n');
					}
				}
			} catch (Exception e) {
				break;
			}
		}
	}
}
