package mix;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import db.DBManager;
import bean.CONSTANT;
import bean.DBInfo;
import bean.LBSMessage;
import bean.RoomInfo;

/**
 * 单个服务端与客户端连接程序
 * 用以维持服务器与单个客户端的连接线程，负责接收客户端发来的信息
 * @author monsieurchak
 *
 */
public class ClientThread extends Thread{

	//表征使用此线程的一个用户
	private String userName = null;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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

				case CONSTANT.MSG_HEAD_ROOM_CHAT:	//聊天室普通聊天信息

					//读入客户端发送来的聊天信息文本
					String roomMessage = lbsMessage.getBODY();

					//读入客户端发来的聊天室信息
					RoomInfo roomInfo = lbsMessage.getRoomInfo();

					//检查聊天信息合法性
					if (roomMessage != null && !(roomMessage.equals(""))){

						//遍历所有聊天室
						for (int i = 0; i < serverThread.rooms.size(); i++) {

							//如果存在目标聊天室
							RoomThread room = serverThread.rooms.get(i);
							if (room.roomInfo.getID().equals(roomInfo.getID())) {
								synchronized (room.roomMessages) {
									System.out.println("发送消息、、、");
									Date time = new Date(System.currentTimeMillis());
									SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
									String timestr= format.format(time);
									roomMessage = timestr + ">>" + roomMessage;
									room.roomMessages.addElement(new LBSMessage(roomMessage));

									//服务器端文本框显示新消息
									Server.jTextArea1.append(roomMessage + '\n');
								}
							}
						}
					}

					break;

				case CONSTANT.MSG_HEAD_ORDINAl:		//普通广播信息

					//读入客户端发送来的信息
					String message = lbsMessage.getBODY();
					if (message != null && !(message.equals(""))) {
						synchronized (serverThread.messages) {
							if (message != null) {
								Date time = new Date(System.currentTimeMillis());
								SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
								String timestr= format.format(time);
								message = timestr + ">>" + message;
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
					String user_login = lbsMessage.getUSER();	
					String password_login = lbsMessage.getPASS();

					if (lbsMessage.getADDITION()
							.equals(CONSTANT.MSG_ADDITION_LOGIN)) {	//附加信息，登录状态
						DBManager dbManager = new DBManager(DBInfo.DB_URL, 
								DBInfo.DB_USER, DBInfo.DB_PASSWORD);
						dbManager.connectMYSQL();
						ResultSet resultSet = dbManager.selectSQL("SELECT * FROM " 
								+ DBInfo.TABLE_USER + " WHERE ID = " + "'" + user_login + "'");
						ArrayList<HashMap<String, String>> list = dbManager.showResult(resultSet);

						//广播消息对象，通知指定对象登录结果
						LBSMessage loginResultMSG = new LBSMessage();

						//此为广播消息，头部设置为MSG_HEAD_BROADCAST
						loginResultMSG.setHEAD(CONSTANT.MSG_HEAD_BROADCAST);

						//发送给指定用户
						loginResultMSG.setUSER(user_login);
						if (!list.isEmpty()) {	//找到相应用户
							HashMap<String, String> dbUser = list.get(0);
							if (dbUser.get("STATUS") == null) {	//账户正处于默认(正常)状态
								if (dbUser.get("PASSWORD").equals(password_login)) {	//密码验证正确

									//附加消息，登录成功
									loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_SUCCEED);
									serverThread.messages.add(loginResultMSG);
									this.userName = user_login;

								}
								else {	//密码不正确

									//附加消息，登录失败
									loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_FAILED);
									serverThread.messages.add(loginResultMSG);
									while (!serverThread.messages.isEmpty());
									serverThread.clients.removeElement(this);
								}
							}
							else {//账户状态不正确

								//附加消息，登录失败
								loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_FAILED);
								serverThread.messages.add(loginResultMSG);
								while (!serverThread.messages.isEmpty());
								serverThread.clients.removeElement(this);
							}
						}
						else {	//没找到相应用户，移除本客户端的连接

							//附加消息，登录失败
							loginResultMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN_FAILED);
							serverThread.messages.add(loginResultMSG);
							while (!serverThread.messages.isEmpty());
							serverThread.clients.removeElement(this);
						}									
						dbManager.disConnectMYSQL();
					}
					else if (lbsMessage.equals(CONSTANT.MSG_ADDITION_LOGOUT)){	//注销登录
					}

					break;

				case CONSTANT.MSG_HEAD_COUNT:		//账户信息，包括注册操作

					//String user用于MYSQL查询语句，需添加""
					String user_reg = lbsMessage.getUSER();	
					String password_reg = lbsMessage.getPASS();
					if (lbsMessage.getADDITION().equals(CONSTANT	//注册消息
							.MSG_ADDITION_REGISTER)) {

						DBManager dbManager = new DBManager(DBInfo.DB_URL, 
								DBInfo.DB_USER, DBInfo.DB_PASSWORD);
						dbManager.connectMYSQL();
						ResultSet resultSet = dbManager.selectSQL("SELECT * FROM " 
								+ DBInfo.TABLE_USER + " WHERE ID = " + "'" + user_reg + "'");
						ArrayList<HashMap<String, String>> list = dbManager.showResult(resultSet);

						//广播消息对象，通知指定对象注册结果
						LBSMessage registerResultMSG = new LBSMessage();

						//此为广播消息，头部设置为MSG_HEAD_BROADCAST
						registerResultMSG.setHEAD(CONSTANT.MSG_HEAD_BROADCAST);

						//发送给指定用户
						//注意此用户名带有两个"，需要注意匹配
						registerResultMSG.setUSER(user_reg);
						if (list.isEmpty()) {	//数据库中此用户名没被注册
							Date time = new Date(System.currentTimeMillis());
							SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
							String timestr= format.format(time);
							boolean dbResult = dbManager.insertSQL("INSERT INTO users (_id ,ID ,PASSWORD ,NICKNAME ,EMAIL ,STATES ,LASTLOGINTIME ,LOGINCOUNTER ,SIGN)" +
									" VALUES (NULL , '" + user_reg + "','" + password_reg + "','','cheriship@163.com', '', '" +timestr+ "', 1, '')");
							if (dbResult) {	//添加至数据库,注册成功
								registerResultMSG.setADDITION(CONSTANT.MSG_ADDITION_REGISTER_SUCCEED);
								serverThread.messages.add(registerResultMSG);

								//广播通知新用户加入
								LBSMessage deClareMSG = new LBSMessage("欢迎新用户: " + user_reg);
								serverThread.messages.add(deClareMSG);
							}
							else {	//添加至数据库失败，注册失败
								registerResultMSG.setADDITION(CONSTANT.MSG_ADDITION_REGISTER_FAILED);
								serverThread.messages.add(registerResultMSG);
								while (!serverThread.messages.isEmpty());
								serverThread.clients.removeElement(this);
							}
						}
						else {	//此用户名已经被注册
							registerResultMSG.setADDITION(CONSTANT.MSG_ADDITION_REGISTER_FAILED);
							serverThread.messages.add(registerResultMSG);
							while (!serverThread.messages.isEmpty());
							serverThread.clients.removeElement(this);
						}
						dbManager.disConnectMYSQL();
					}
					break;

				case CONSTANT.MSG_HEAD_ROOM:
					if (this.userName == null) {	//用户必须先行登录
						break;
					}
					LBSMessage RoomResult = new LBSMessage();
					RoomResult.setHEAD(CONSTANT.MSG_HEAD_ROOM);
					RoomResult.setUSER(this.userName);
					if (lbsMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_JOINROOM)) {
						boolean isLogin = false;
						for (int i = 0; i < serverThread.rooms.size(); i++) {
							RoomThread room = serverThread.rooms.get(i);

							//如果服务器上存在指定的聊天室
							if (room.roomInfo.getID().equals(lbsMessage.getRoomInfo().getID())) {	
								room.roomClients.add(this);
								RoomResult.setADDITION(CONSTANT.MSG_ADDITION_JOINROOM_SUCCEED);
								serverThread.messages.add(RoomResult);
								isLogin = true;
							}
							else {

							}
						}
						if (!isLogin) {
							RoomResult.setADDITION(CONSTANT.MSG_ADDITION_JOINROOM_FAILED);
							serverThread.messages.add(RoomResult);
						}
					}
					else if (lbsMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_CREATEROOM)) {
						boolean isExist = false;
						RoomThread room = null;
						
						//遍历服务器中的聊天室
						for (int i = 0; i < serverThread.rooms.size(); i++) {	
							room = serverThread.rooms.get(i);

							//如果服务器上存在指定ID的聊天室
							if (room.roomInfo.getID().equals(lbsMessage.getRoomInfo().getID())){
								RoomResult.setADDITION(CONSTANT.MSG_ADDITION_CREATEROOM_FAILED);
								serverThread.messages.add(RoomResult);
								isExist = true;
							}
						}
						if (!isExist) {
							RoomInfo roomInfo1 = lbsMessage.getRoomInfo();
							RoomThread newRoom = new RoomThread(roomInfo1);
							newRoom.start();
							newRoom.roomClients.add(this);
							serverThread.rooms.add(newRoom);
							RoomResult.setADDITION(CONSTANT.MSG_ADDITION_CREATEROOM_SUCCEED);
							serverThread.messages.add(RoomResult);
						}
					}
					else if (lbsMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_QUITROOM)) {
						for (int i = 0; i < serverThread.rooms.size(); i++) {

							//找到指定ID对应的聊天室
							if (serverThread.rooms.get(i).roomInfo.getID().
									equals(lbsMessage.getRoomInfo().getID())) {
								RoomThread quitRoomThread = serverThread.rooms.get(i);
								quitRoomThread.roomClients.removeElement(this);
							}
						}
					}
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
