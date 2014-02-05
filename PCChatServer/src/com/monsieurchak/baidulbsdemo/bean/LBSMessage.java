package com.monsieurchak.baidulbsdemo.bean;

import java.io.Serializable;

/**
 * 本类定义了Socket通信的数据包格式
 * @author monsieurchak
 *
 */
public class LBSMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int Head = 0;		//消息头,CONSTANT类提供常量
	private String Body = null;		//消息体
	private String Sender = null;	//消息发送者，匿名发送表示为null
	private String Receiver = null;	//收信人,发送给所有用户表示为null
	private String Addition = null;		//附加信息标示符
	private String USER = null;		//附加消息：用户名
	private String PASS = null;		//附加消息：用户密码
	private RoomInfo roomInfo = null;	//附加消息: 聊天室消息
	
	public LBSMessage() {
		
	}
	
	/**
	 * 构建一条普通聊天室消息
	 * HEAD = MSG_HEAD_ROOM_CHAT;
	 * SENDER = null;
	 * RECEIVER = null;
	 * ADDITION = null;
	 * USER = null;
	 * PASS = null;
	 * roomInfo = null;
	 * @param BODY 消息体
	 */
	public LBSMessage(String BODY){
		this.Head = CONSTANT.MSG_HEAD_ROOM_CHAT;
		this.Body = BODY;
	}
	
	public int getHEAD() {
		return Head;
	}
	public void setHEAD(int hEAD) {
		Head = hEAD;
	}
	public String getBODY() {
		return Body;
	}
	public void setBODY(String bODY) {
		Body = bODY;
	}
	public String getSENDER() {
		return Sender;
	}
	public void setSENDER(String sENDER) {
		Sender = sENDER;
	}
	public String getRECEIVER() {
		return Receiver;
	}
	public void setRECEIVER(String rECEIVER) {
		Receiver = rECEIVER;
	}
	public String getUSER() {
		return USER;
	}

	public void setUSER(String uSER) {
		USER = uSER;
	}

	public String getPASS() {
		return PASS;
	}
	public void setPASS(String pASS) {
		PASS = pASS;
	}
	public String getADDITION() {
		return Addition;
	}

	public void setADDITION(String aDDITION) {
		Addition = aDDITION;
	}

	public RoomInfo getRoomInfo() {
		return roomInfo;
	}

	public void setRoomInfo(RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}

	@Override
	public String toString() {
		return "LBSMessage [HEAD=" + Head + ", BODY=" + Body + ", SENDER="
				+ Sender + ", RECEIVER=" + Receiver + ", ADDITION=" + Addition
				+ ", USER=" + USER + ", PASS=" + PASS + "]";
	}

	
}
