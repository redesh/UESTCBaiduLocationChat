package mix;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;



import bean.LBSMessage;

/**
 * 服务器端监听程序
 * 负责创建ServerSocket以监听是否有新的客户端连接到服务器端
 * @author monsieurchak
 *
 */
public class ServerThread extends Thread{
	
	ServerSocket serverSocket;
	public static final int PORT = 8521;
	
	//创建一个Vector对象用以存储客户连接时的ClientThread对象
	//ClientThread类维持服务器与单个客户端的链接线程，负责接收客户端发来的信息
	Vector<ClientThread> clients;
	
	//用以存储客户端发来的，服务器接收到但未发送出去的给另客户端信息
	Vector<LBSMessage> messages;
	
	//用以存储服务器上存在的聊天室
	Vector<RoomThread> rooms;
	
	//BroadCast类负责服务器向客户端广播信息
	BroadCast broadCast;
	
	String ip;
	InetAddress myIPAddress = null;
	
	public ServerThread() {
		clients = new Vector<ClientThread>();
		messages = new Vector<LBSMessage>();
		rooms = new Vector<RoomThread>();
		
		try {
			
			//创建ServerSocket类对象
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("ServerSocket连接错误: " + e.toString());
		}
		
		//获取本地服务器地址信息
		try {
			myIPAddress = InetAddress.getLocalHost();
		} catch (Exception e) {
			System.out.println("服务器获取本地地址信息错误:" + e.toString());
		}
		ip = myIPAddress.getHostAddress();
		Server.jTextArea1.append("服务器地址：" + ip + "端口号：" + 
				String.valueOf(serverSocket.getLocalPort()) + "\n");
		
		//创建广播信息线程并启动
		broadCast = new BroadCast(this);
		broadCast.start();
	}
	
	@Override
	public void run() {
		while(true){
			try {
				
				//获取客户端连接，并返回一个新的socket对象
				Socket socket = serverSocket.accept();
				System.out.println(socket.getInetAddress().getHostAddress());
				
				//创建ClientThread线程并启动
				ClientThread clientThread = new ClientThread(socket,this);
				clientThread.start();
				if (socket != null) {
					synchronized (clients) {
						
						//将客户端连接加入到Vector数组中保存
						clients.addElement(clientThread);
					}
				}
			} catch (Exception e) {
				System.out.println("发生异常：" + e.toString());
				System.out.println("建立客户端联机失败！");
				
				//终止虚拟机，按照惯例：
				//参数0表示正常退出；参数非0为异常退出
				System.exit(2);
			}
		}
	}
	
	@Override
	protected void finalize(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("终止ServerSocket失败：" + e.toString());
		}
		serverSocket = null;
	}
}
