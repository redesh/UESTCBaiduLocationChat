package com.monsieurchak.baidumapdemo.logic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;

import android.util.Log;

import com.baidu.mapapi.map.LocationData;
import com.monsieurchak.baidumapdemo.Util.Util;
import com.monsieurchak.baidumapdemo.bean.CONSTANT;
import com.monsieurchak.baidumapdemo.bean.LBSMessage;
import com.monsieurchak.baidumapdemo.bean.RoomInfo;
import com.monsieurchak.baidumapdemo.bean.TAG;

public class ServiceThread {

	public static class loginThread extends Thread{

		ObjectOutputStream out;
		String user;
		String pass;

		public loginThread(ObjectOutputStream out, String user, String pass) {
			this.out = out;
			this.user = user;
			this.pass = pass;
		}

		@Override
		public void run() {
			try {
				LBSMessage loginMSG = new LBSMessage();
				loginMSG.setHEAD(CONSTANT.MSG_HEAD_OPERATED);
				loginMSG.setADDITION(CONSTANT.MSG_ADDITION_LOGIN);
				loginMSG.setUSER(user);
				loginMSG.setPASS(pass);
				out.writeObject(loginMSG);
			} catch (IOException e) {
				Log.d(TAG.NET, "登录信息发送失败: " + e);
			}
		}
	}

	public static class registThread extends Thread{

		ObjectOutputStream out;
		String reg_user;
		String reg_pass;

		public registThread(ObjectOutputStream out, String reg_user, String reg_pass) {
			this.out = out;
			this.reg_user = reg_user;
			this.reg_pass = reg_pass;
		}

		@Override
		public void run() {
			try {
				LBSMessage regMSG = new LBSMessage();
				regMSG.setHEAD(CONSTANT.MSG_HEAD_COUNT);
				regMSG.setADDITION(CONSTANT.MSG_ADDITION_REGISTER);
				regMSG.setUSER(reg_user);
				regMSG.setPASS(reg_pass);
				out.writeObject(regMSG);
			} catch (Exception e) {
				Log.d(TAG.NET, "注册信息发送失败: " + e);
			}
		}

	}

	public static class joinThread extends Thread{
		ObjectOutputStream out;
		String user;
		String roomName;
		String roomPosition;
		
		public joinThread(ObjectOutputStream out, String user, HashMap<String, Object> roomInfo) {
			this.out = out;
			this.user = user;
			this.roomName = (String) roomInfo.get(CONSTANT.ROOM_NAME);
			this.roomPosition = (String)roomInfo.get(CONSTANT.ROOM_LOCATION);
		}

		@Override
		public void run() {
			try {
				LBSMessage joinRoomMSG = new LBSMessage();
				
				//将要加入的聊天室的信息
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setNAME(roomName);
				roomInfo.setROOM_LOCATION(roomPosition);
				
				//申请加入聊天室的消息
				joinRoomMSG.setHEAD(CONSTANT.MSG_HEAD_ROOM);
				joinRoomMSG.setADDITION(CONSTANT.MSG_ADDITION_JOINROOM);
				joinRoomMSG.setUSER(user);
				joinRoomMSG.setRoomInfo(roomInfo);
				out.writeObject(joinRoomMSG);
			} catch (Exception e) {
				Log.d(TAG.NET, "申请加入聊天室请求发送错误！" + e);
			}
		}
	}

	public static class createRoomThread extends Thread{
		ObjectOutputStream out;
		String user;
		String roomIDLocal;
		String roomName;
		String roomLocation;

		public createRoomThread(ObjectOutputStream out, HashMap<String, Object> createRoom) {
			this.out = out;
			this.roomIDLocal = (String) createRoom.get(CONSTANT.ROOM_ID_LOCAL);	//获取当前大致位置
			this.roomName = (String)createRoom.get(CONSTANT.ROOM_NAME);			//获取欲创建的聊天室的名称
			this.roomLocation = (String)createRoom.get(CONSTANT.ROOM_LOCATION);	//获取聊天室的具体位置
		}

		@Override
		public void run() {
			try {
				LBSMessage createRoomMSG = new LBSMessage();
				createRoomMSG.setHEAD(CONSTANT.MSG_HEAD_ROOM);
				createRoomMSG.setADDITION(CONSTANT.MSG_ADDITION_CREATEROOM);
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setID(roomIDLocal);
				roomInfo.setNAME(roomName);
				roomInfo.setROOM_LOCATION(roomLocation);
				createRoomMSG.setRoomInfo(roomInfo);
				out.writeObject(createRoomMSG);
			} catch (Exception e) {
				Log.d(TAG.NET, "创建聊天室请求发送错误！" + e);
			}
		}
	}

	public static class chatThread extends Thread{
		Vector<LBSMessage> messages;
		ObjectOutputStream out;
		RoomInfo roomInfo;

		public chatThread(ObjectOutputStream out, RoomInfo roomInfo) {
			messages = new Vector<LBSMessage>();
			this.out = out;
			this.roomInfo = roomInfo;
		}

		public void add(LBSMessage message){
			messages.add(message);
		}

		@Override
		public void run() {
			LBSMessage message;
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				synchronized (messages) {
					if (messages.isEmpty()) {
						continue;
					}

					//获取服务器端信息数组存储的第一条信息
					message = (LBSMessage)this.messages.firstElement();
					message.setRoomInfo(roomInfo);
					try {
						out.writeObject(message);
					} catch (IOException e) {
					}

					//删除已经发送过的那条信息
					this.messages.removeElement(message);
				}
			}
		}
	}

	public static class searchRoomThread extends Thread{

		LocationData locationData;
		ObjectOutputStream out;
		String user;

		public searchRoomThread(LocationData locationData, ObjectOutputStream out, String user) {
			this.locationData = locationData;
			this.out = out;
			this.user = user;
		}

		@Override
		public void run() {
			try {
				LBSMessage searchMSG = new LBSMessage();
				searchMSG.setHEAD(CONSTANT.MSG_HEAD_ROOM_SEARCH);
				RoomInfo roomInfo = new RoomInfo();
				int latitude = (int) (locationData.latitude * CONSTANT.ACCURACY);
				int longitude = (int) (locationData.longitude * CONSTANT.ACCURACY);
				String latitudeString = Util.frontCompWithZore(latitude, 5);
				String longitudeString = Util.frontCompWithZore(longitude, 5);
				String IDLocal = latitudeString + longitudeString;
				//String specifiedPosition = locationData.latitude + locationData.longitude + "";

				//聊天室的大致位置，用以搜寻
				roomInfo.setID(IDLocal);

				//聊天室的具体位置，用以标记；在搜寻聊天室时没有用处
				//roomInfo.setROOM_LOCATION(specifiedPosition);

				searchMSG.setRoomInfo(roomInfo);
				searchMSG.setUSER(user);
				out.writeObject(searchMSG);
			} catch (Exception e) {
				Log.d(TAG.NET, "查找聊天室请求发送错误！" + e);
			}
		}

	}
	
	public static class quitChatRoomThread extends Thread{

		ObjectOutputStream out;
		String user;
		String roomName;
		
		public quitChatRoomThread(String roomName, String user, ObjectOutputStream out) {
			this.roomName = roomName;
			this.user = user;
			this.out = out;
		}
		
		@Override
		public void run() {
			try {
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setNAME(roomName);
				
				LBSMessage quitMSG = new LBSMessage();
				quitMSG.setHEAD(CONSTANT.MSG_HEAD_ROOM);
				quitMSG.setADDITION(CONSTANT.MSG_ADDITION_QUITROOM);
				quitMSG.setRoomInfo(roomInfo);
				quitMSG.setUSER(user);
				out.writeObject(quitMSG);
			} catch (Exception e) {
				Log.d(TAG.NET, "退出聊天室请求发送错误！" + e);
			}
		}
		
	}
}
