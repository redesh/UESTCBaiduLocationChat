package com.monsieurchak.baidumapdemo.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.mapapi.map.LocationData;
import com.monsieurchak.baidumapdemo.bean.CONSTANT;
import com.monsieurchak.baidumapdemo.bean.LBSMessage;
import com.monsieurchak.baidumapdemo.bean.RoomInfo;
import com.monsieurchak.baidumapdemo.bean.ServerDetail;
import com.monsieurchak.baidumapdemo.bean.TAG;
import com.monsieurchak.baidumapdemo.bean.Task;
import com.monsieurchak.baidumapdemo.bean.UserInfo;
import com.monsieurchak.baidumapdemo.ui.UIActivity;

/**
 * 该类主要用以处理业务逻辑
 * 实现了Runnable，在新线程中执行
 * @author monsieurchak
 *
 */

public class MainService extends Service implements Runnable{

	//任务数组
	private static Queue<Task> tasks = new LinkedList<Task>();

	//Activity队列
	private static ArrayList<Activity> activities = new ArrayList<Activity>();

	boolean isRun;

	Socket socket;

	//输入输出流
	ObjectInputStream in;
	ObjectOutputStream out;

	//客户端IP地址
	private static final String ip = ServerDetail.SERVER_IP;

	//Socket通信端口号常量
	private static final int PORT = ServerDetail.SERVER_PORT;

	//当前用户基本信息
	UserInfo userInfo = null;

	//当前用户登录标志
	boolean isLogin = false;
	
	//普通通话线程
	ServiceThread.chatThread chatThread = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void run() {
		while(isRun){
			Task task = null;
			//检查是否有任务
			if (!tasks.isEmpty()) {
				task = tasks.poll();
				doTask(task);
			}
			else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		isRun = true;
		Thread thread = new Thread(){
			public void run() {
				try {
					socket = new Socket(ip, PORT);
					out = new ObjectOutputStream(socket.getOutputStream());
					in = new ObjectInputStream(socket.getInputStream());
				} catch (Exception e) {
					Log.d(TAG.NET, "Socket连接失败: " + e);
				}
			};
		};
		thread.start();

		//接受信息线程
		recThread.start();

		Thread mainThread = new Thread(this);
		mainThread.start();

		userInfo = new UserInfo();
	}

	//核心业务逻辑添加到MainService中执行
	public static void addTask(Task task){
		tasks.add(task);
	}

	//需要更改UI的Activity将自身添加到MainService
	public static void addActivity(Activity activity){
		activities.add(activity);
	}

	/**
	 * 根据Activity名查找Activities队列
	 * @param name
	 * @return 若有相应Activity返回之，若无返回null
	 */
	private Activity findActivityByName(String name){
		for (Activity activity : activities) {
			if (null != activity) {
				if (activity.getClass().getName().indexOf(name) > 0) {
					Log.d("TAG", "Return activity:  " + name);
					return activity;
				}
			}
		}
		return null;
	}

	private void doTask(Task task){
		//通过HandleMessage处理UI更新
		Message msg = handler.obtainMessage();
		msg.what = -1;
		switch (task.TaskID) {
		case Task.LOGIN:
			String userName = (String)task.getParams().get(CONSTANT.USERNAME);
			String passWord = (String)task.getParams().get(CONSTANT.PASSWORD);
			userInfo.setID(userName);
			userInfo.setPASSWORD(passWord);
			Log.d("TAG", "ID:" + userName + "..." + "PW" + passWord);
			ServiceThread.loginThread loginThread = new ServiceThread.
					loginThread(out, userName, passWord);
			loginThread.start();
			break;
		case Task.REGIST:
			String regUserName = (String)task.getParams().get(CONSTANT.USERNAME);
			String regPassWord = (String)task.getParams().get(CONSTANT.PASSWORD);
			userInfo.setID(regUserName);
			userInfo.setPASSWORD(regPassWord);
			ServiceThread.registThread regThread = new ServiceThread.
					registThread(out, regUserName, regPassWord);
			regThread.start();
			break;
		case Task.FINDIDBACK:
			//添加”找回密码”要执行的相关业务

			//msg.what = task.TaskID;		//提请Handler更新UI
			break;
		case Task.JOIN_ROOM:
			HashMap<String, Object> roomToJoin = task.getParams();
			ServiceThread.joinThread joinRoomThread = new ServiceThread.
					joinThread(out, userInfo.getID(), roomToJoin);
			joinRoomThread.start();
			break;
		case Task.CREATE_ROOM:
			HashMap<String, Object> createRoomMSG = task.getParams();
			ServiceThread.createRoomThread createRoomThread = new ServiceThread.
					createRoomThread(out, createRoomMSG);
			createRoomThread.start();
			break;
		case Task.ROOM_CHAT:
			HashMap<String, Object> map = task.getParams();
			LBSMessage sendMSG = (LBSMessage)map.get("msg");
			sendMSG.setBODY(userInfo.getID() + sendMSG.getBODY());
			chatThread.add(sendMSG);
			break;
		case Task.ROOM_SEARCH:
			HashMap<String, Object> locationMap = task.getParams();
			LocationData locationData = (LocationData) locationMap.get(CONSTANT.SEARCH_ROOM);
			ServiceThread.searchRoomThread searchRoomThread = 
					new ServiceThread.searchRoomThread(locationData, out, userInfo.getID());
			searchRoomThread.start();
			break;
		case Task.QUIT_ROOM:
			HashMap<String, Object> quitMap = task.getParams();
			String roomName = (String) quitMap.get(CONSTANT.ROOM_NAME);
			ServiceThread.quitChatRoomThread quitThread = 
					new ServiceThread.quitChatRoomThread(roomName, userInfo.getID(), out);
			quitThread.start();
			break;
		default:
			break;
		}

		//如果有UI更新请求向Handler发送Message
		//		if (msg.what != -1) {
		//			handler.sendMessage(msg);
		//		}
	}

	/**
	 * 用于接收消息的线程
	 */
	Thread recThread = new Thread(){
		public void run() {
			while (true) {

				try {
					LBSMessage recMessage = (LBSMessage)in.readObject();
					Message msg = new Message();
					if ((!recMessage.getUSER().equals(userInfo.getID())) && (!recMessage.getUSER().equals(CONSTANT.MSG_ALLUSER))) {
						continue;
					}
					switch (recMessage.getHEAD()) {
					
					case CONSTANT.MSG_HEAD_ROOM_CHAT:
						String roomMessage = recMessage.getBODY();
						msg.what = Task.ROOM_CHAT;
						msg.obj = (String)roomMessage;
						handler.sendMessage(msg);
						break;
					
					case CONSTANT.MSG_HEAD_BROADCAST:

						//登录成功
						if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_LOGIN_SUCCEED)) {
							isLogin = true;
							msg.what = Task.LOGIN;
							msg.arg1 = CONSTANT.SUCCEED;
							handler.sendMessage(msg);
						}

						//登录失败
						else if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_LOGIN_FAILED)){
							isLogin = false;
							msg.what = Task.LOGIN;
							msg.arg1 = CONSTANT.FAILED;
							handler.sendMessage(msg);
						}

						//注册成功
						else if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_REGISTER_SUCCEED)) {
							isLogin = true;
							msg.what = Task.REGIST;
							msg.arg1 = CONSTANT.SUCCEED;
							handler.sendMessage(msg);
						}

						//注册失败
						else if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_REGISTER_FAILED)) {
							isLogin = false;
							msg.what = Task.REGIST;
							msg.arg1 = CONSTANT.FAILED;
							handler.sendMessage(msg);
						}
						break;

					case CONSTANT.MSG_HEAD_ROOM:
						if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_JOINROOM_SUCCEED)) {
							msg.what = Task.JOIN_ROOM;
							msg.arg1 = CONSTANT.SUCCEED;
							msg.obj = recMessage.getRoomInfo().getNAME();
							handler.sendMessage(msg);
						}
						else if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_JOINROOM_FAILED)) {
							msg.what = Task.JOIN_ROOM;
							msg.arg1 = CONSTANT.FAILED;
							handler.sendMessage(msg);
						}
						else if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_CREATEROOM_SUCCEED)) {
							msg.what = Task.CREATE_ROOM;
							msg.arg1 = CONSTANT.SUCCEED;
							msg.obj = (String)recMessage.getRoomInfo().getNAME();
							handler.sendMessage(msg);
						}
						else if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_CREATEROOM_FAILED)) {
							msg.what = Task.CREATE_ROOM;
							msg.arg1 = CONSTANT.FAILED;
							handler.sendMessage(msg);
						}
						break;
					
					case CONSTANT.MSG_HEAD_ROOM_SEARCH:
						if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_SEARCH_ROOM_SUCCED)) {
							ArrayList<Object> roomsFound = recMessage.getAddtionArrayList();
							msg.what = Task.ROOM_SEARCH;
							msg.arg1 = CONSTANT.SUCCEED;
							msg.obj = roomsFound;
							handler.sendMessage(msg);
						}
						else if (recMessage.getADDITION().equals(CONSTANT.MSG_ADDITION_SEARCH_ROOM_FAILED)) {
							msg.what = Task.ROOM_SEARCH;
							msg.arg1 = CONSTANT.FAILED;
							handler.sendMessage(msg);
						}
						break;
					default:
						break;
					}
				} catch (Exception e) {					
				}
			}
		};
	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			UIActivity uiActivity;	//UIActivity
			switch (msg.what) {
			case Task.LOGIN:	//登陆后更新UI
				uiActivity = (UIActivity)findActivityByName("Login_Activity");
				if (msg.arg1 == CONSTANT.SUCCEED) {
					Log.d("TAG", "登录成功");
					uiActivity.refresh(CONSTANT.LOGIN_SUCCEED);
				}
				else if(msg.arg1 == CONSTANT.FAILED){
					Log.d("TAG", "登录失败");
					uiActivity.refresh(CONSTANT.LOGIN_FAILED);
				}
				break;

			case Task.REGIST:
				uiActivity = (UIActivity)findActivityByName("Register_Activity");
				if (msg.arg1 == CONSTANT.SUCCEED) {
					Log.d("TAG", "注册成功");
					uiActivity.refresh(CONSTANT.REG_SUCCED);
				}
				else if(msg.arg1 == CONSTANT.FAILED){
					Log.d("TAG", "注册失败");
					uiActivity.refresh(CONSTANT.REG_FAILED);
				}
				break;

			case Task.JOIN_ROOM:	//加入聊天室更新UI
				uiActivity = (UIActivity)findActivityByName("ChatRoomList_Activity");
				if (msg.arg1 == CONSTANT.SUCCEED) {
					Log.d("TAG", "加入聊天室成功");
					Object [] objects = new Object[3];
					objects[0] = CONSTANT.JOIN_ROOM;
					objects[1] = CONSTANT.SUCCEED;
					objects[2] = msg.obj;	//聊天室NAME
					RoomInfo roomInfo = new RoomInfo();
					roomInfo.setNAME((String)msg.obj);
					chatThread = new ServiceThread.chatThread(out, roomInfo);	//建立消息发送线程
					chatThread.start();
					uiActivity.refresh(objects);
				}
				else if(msg.arg1 == CONSTANT.FAILED){
					Object [] objects = new Object[3];
					objects[0] = CONSTANT.JOIN_ROOM;
					objects[1] = CONSTANT.FAILED;
					objects[2] = null;
					uiActivity.refresh(objects);
					uiActivity.refresh(CONSTANT.FAILED);
				}
				break;
			case Task.CREATE_ROOM:
				uiActivity = (UIActivity)findActivityByName("ChatRoomList_Activity");
				if (msg.arg1 == CONSTANT.SUCCEED) {
					Object [] objects = new Object[3];
					objects[0] = CONSTANT.CREATE_ROOM;
					objects[1] = CONSTANT.SUCCEED;
					objects[2] = msg.obj;
					RoomInfo roomInfo = new RoomInfo();
					roomInfo.setNAME((String)msg.obj);
					chatThread = new ServiceThread.chatThread(out, roomInfo);
					chatThread.start();
					uiActivity.refresh(objects);
				} 
				else {
					Object [] objects = new Object[3];
					objects[0] = CONSTANT.CREATE_ROOM;
					objects[1] = CONSTANT.FAILED;
					objects[2] = null;
					uiActivity.refresh(objects);
				}
				break;
			case Task.ROOM_CHAT:
				uiActivity = (UIActivity)findActivityByName("ChatRoom_Activity");
				String recMSG = ">>" + (String)msg.obj;
				uiActivity.refresh(recMSG);
				break;
			case Task.ROOM_SEARCH:
				if (msg.arg1 == CONSTANT.SUCCEED) {
					uiActivity = (UIActivity)findActivityByName("ChatRoomList_Activity");
					Object [] objects = new Object[3];
					objects[0] = CONSTANT.SEARCH_ROOM;
					objects[1] = CONSTANT.SUCCEED;
					objects[2] = msg.obj;
					uiActivity.refresh(objects);
				}
				else {
					uiActivity = (UIActivity)findActivityByName("ChatRoomList_Activity");
					Object [] objects = new Object[3];
					objects[0] = CONSTANT.SEARCH_ROOM;
					objects[1] = CONSTANT.FAILED;
					objects[2] = null;
					uiActivity.refresh(objects);
				}
			break;
				
			default:
				break;
			}
		};
	};

}
