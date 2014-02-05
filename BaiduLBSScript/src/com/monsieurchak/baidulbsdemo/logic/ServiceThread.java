package com.monsieurchak.baidulbsdemo.logic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Vector;

import android.os.AsyncTask;
import android.util.Log;

import com.monsieurchak.baidulbsdemo.bean.CONSTANT;
import com.monsieurchak.baidulbsdemo.bean.LBSMessage;
import com.monsieurchak.baidulbsdemo.bean.RoomInfo;
import com.monsieurchak.baidulbsdemo.bean.TAG;

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
		String roomID;
		
		public joinThread(ObjectOutputStream out, String user, String roomID) {
			this.out = out;
			this.user = user;
			this.roomID = roomID;
		}
		
		@Override
		public void run() {
			try {
				LBSMessage joinRoomMSG = new LBSMessage();
				joinRoomMSG.setHEAD(CONSTANT.MSG_HEAD_ROOM);
				joinRoomMSG.setADDITION(CONSTANT.MSG_ADDITION_JOINROOM);
				joinRoomMSG.setUSER(user);
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setID(roomID);
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
		String roomID;
		
		public createRoomThread(ObjectOutputStream out, String roomID) {
			this.out = out;
			this.roomID = roomID;
		}
		
		@Override
		public void run() {
			try {
				LBSMessage createRoomMSG = new LBSMessage();
				createRoomMSG.setHEAD(CONSTANT.MSG_HEAD_ROOM);
				createRoomMSG.setADDITION(CONSTANT.MSG_ADDITION_CREATEROOM);
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setID(roomID);
				createRoomMSG.setRoomInfo(roomInfo);
				out.writeObject(createRoomMSG);
			} catch (Exception e) {
				Log.d(TAG.NET, "创建聊天室请求发送错误！" + e);
			}
		}
	}
	
	public class AsyncJoinRoom extends AsyncTask<Map<String, Object>, Integer, String>{

		@Override
		protected String doInBackground(Map<String, Object>... params) {
			Map<String, Object> map = (Map<String, Object>)params[0];
			ObjectOutputStream out = (ObjectOutputStream) map.get("out");
			String user = (String)map.get("user");
			String roomID = (String)map.get("roomID");
			
			LBSMessage joinRoomMSG = new LBSMessage();
			joinRoomMSG.setHEAD(CONSTANT.MSG_HEAD_ROOM);
			joinRoomMSG.setADDITION(CONSTANT.MSG_ADDITION_JOINROOM);
			joinRoomMSG.setUSER(user);
			RoomInfo roomInfo = new RoomInfo();
			roomInfo.setID(roomID);
			joinRoomMSG.setRoomInfo(roomInfo);
			try {
				out.writeObject(joinRoomMSG);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
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
}
