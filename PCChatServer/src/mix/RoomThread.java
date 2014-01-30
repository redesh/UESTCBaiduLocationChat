package mix;

import java.util.Vector;

import bean.LBSMessage;

public class RoomThread extends Thread{

	//创建一个Vector对象用以存储客户连接时的ClientThread对象
	//ClientThread类维持服务器与单个客户端的链接线程，负责接收客户端发来的信息
	Vector<ClientThread> roomClients;
	
	//用以存储客户端发来的，服务器接收到但未发送出去的给另客户端信息
	Vector<LBSMessage> roomMessages;
	
	public RoomThread() {
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
}
