package mix;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;



import db.DBManager;
import bean.CONSTANT;
import bean.DBInfo;
import bean.LBSMessage;

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
	ObjectInputStream in = null;
	ObjectOutputStream out = null;

	//声明ServerThread对象
	ServerThread serverThread;

	String str;

	//当前连接到服务器端的客户端数量
	public static int ConnectNumber = 0;

	//通信数据对象
	private LBSMessage lbsMessage = null;

	public ClientThread(Socket socket, ServerThread serverThread) {
		clientSocket = socket;
		this.serverThread = serverThread;
		try {

			//实例化客户端Socket的数据流
			in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			out = new ObjectOutputStream(clientSocket.getOutputStream());
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
		while (true) {	//等待接收消息
			try {

				lbsMessage = (LBSMessage) in.readObject();

				switch(lbsMessage.getHEAD()){	//判定消息HEAD
				case CONSTANT.MSG_HEAD_ORDINAl:		//普通信息

					//读入客户端发送来的信息
					String message = lbsMessage.getBODY();
					if (message != null && !(message.equals(""))) {
						synchronized (serverThread.messages) {
							if (message != null) {

								LBSMessage sendMSG = new LBSMessage(message);

								//将客户端发送来的信息存储于serverThread的messages数组中
								serverThread.messages.addElement(sendMSG);

								//服务器端文本框显示新消息
								Server.jTextArea1.append(message + '\n');
							}
						}
					}
					break;

				case CONSTANT.MSG_HEAD_OPERATED:	//登录注销操作

					//String user用于MYSQL查询语句，需添加""
					String user = "\"" + lbsMessage.getUSER() + "\"";	
					String password = lbsMessage.getPASS();

					if (lbsMessage.getADDITION()
							.equals(CONSTANT.MSG_ADDITION_LOGIN)) {	//附加信息，登录状态
						DBManager dbManager = new DBManager(DBInfo.DB_URL, 
								DBInfo.DB_USER, DBInfo.DB_PASSWORD);
						dbManager.connectMYSQL();
						ResultSet resultSet = dbManager.selectSQL("SELECT * FROM " 
								+ DBInfo.TABLE_USER + " WHERE ID = " + user);
						ArrayList<HashMap<String, String>> list = dbManager.showResult(resultSet);
						
						//广播消息对象，通知指定对象登录成功
						LBSMessage loginResultMSG = new LBSMessage();

						//此为广播消息，头部设置为MSG_HEAD_BROADCAST
						loginResultMSG.setHEAD(CONSTANT.MSG_HEAD_BROADCAST);

						//附加消息，登录成功
						loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_SUCCEED);

						//发送给指定用户
						//注意此用户名带有两个"，需要注意匹配
						loginResultMSG.setUSER(user);
						if (!list.isEmpty()) {	//找到相应用户
							HashMap<String, String> dbUser = list.get(0);
							if (dbUser.get("STATUS") == null) {	//账户正处于默认(正常)状态
								if (dbUser.get("PASSWORD").equals(password)) {	//密码验证正确

									//附加消息，登录成功
									loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_SUCCEED);
									serverThread.messages.add(loginResultMSG);

								}
								else {	//密码不正确
									
									//附加消息，登录失败
									loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_FAILED);
									serverThread.messages.add(loginResultMSG);
									serverThread.clients.removeElement(this);
								}
							}
							else {//账户状态不正确
								
								//附加消息，登录失败
								loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_FAILED);
								serverThread.clients.removeElement(this);
							}
						}
						else {	//没找到相应用户，移除本客户端的连接
							
							//附加消息，登录失败
							loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_FAILED);
							serverThread.clients.removeElement(this);
						}									
						dbManager.disConnectMYSQL();
					}
					else if (lbsMessage.equals(CONSTANT.MSG_ADDITION_LOGOUT)) {
						
					}

					break;

				case CONSTANT.MSG_HEAD_COUNT:		//账户信息
					break;

				default:
					break;
				}

			} catch (Exception e) {
				break;
			}
		}
	}
}
