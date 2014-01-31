package mix;

import java.util.Vector;
import bean.LBSMessage;
import bean.RoomInfo;

public class RoomThread extends Thread{

	//聊天室信息
	RoomInfo roomInfo = null;

	//创建一个Vector对象用以存储客户连接时的RoomThread对象
	//RoomThread类维持服务器与单个客户端的链接线程，负责接收客户端发来的信息
	Vector<ClientThread> roomClients;

	//用以存储客户端发来的，服务器接收到但未发送出去的给另客户端信息
	Vector<LBSMessage> roomMessages;

	//单个clientThread
	private ClientThread clientThread = null;
	
	//通信数据对象
	private LBSMessage lbsMessage = null;

	public RoomThread(RoomInfo roomInfo) {
		this.roomClients = new Vector<ClientThread>();
		this.roomMessages = new Vector<LBSMessage>();
		this.roomInfo = roomInfo;
	}

	@Override
	public void run() {
		/**
		 * 等待消息线程
		 */
		System.out.println("Here is RoomBroadCast");
		while (true) {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
			}
			synchronized (roomMessages) {
				if (roomMessages.isEmpty()) {
					continue;
				}
				
				lbsMessage = (LBSMessage)roomMessages.firstElement();
				synchronized (roomClients) {
					System.out.println("当前聊天室" + roomInfo.getID() + "共有客户端" + roomClients.size() + "个");
					for (int i = 0; i < roomClients.size(); i++) {
						clientThread = (ClientThread)roomClients.elementAt(i);
						try {
							clientThread.out.writeObject(lbsMessage);
						} catch (Exception e) {
							System.out.println("聊天室广播错误--》--：" + e.toString());
						}
					}
					roomMessages.removeElement(lbsMessage);
				}
			}
		}
	}
}
