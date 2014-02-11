package com.monsieurchak.baidumapdemo.bean;

import java.io.Serializable;

public class RoomInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//聊天室ID
	private String ID = null;

	//聊天室名
	private String NAME = null;

	//聊天室秘钥
	private String ROOM_KEY = null;

	//聊天室公告
	private String ROOM_NOTICE = null;
	
	//聊天室具体位置
	private String ROOM_LOCATION = null;

//	//与本聊天室绑定的RoomThread
//	private RoomThread roomThread = null;

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getROOM_KEY() {
		return ROOM_KEY;
	}
	public void setROOM_KEY(String rOOM_KEY) {
		ROOM_KEY = rOOM_KEY;
	}
	public String getROOM_NOTICE() {
		return ROOM_NOTICE;
	}
	public void setROOM_NOTICE(String rOOM_NOTICE) {
		ROOM_NOTICE = rOOM_NOTICE;
	}
//	public RoomThread getRoomThread() {
//		return roomThread;
//	}
//	public void setRoomThread(RoomThread roomThread) {
//		this.roomThread = roomThread;
//	}
	public String getROOM_LOCATION() {
		return ROOM_LOCATION;
	}
	public void setROOM_LOCATION(String rOOM_LOCATION) {
		ROOM_LOCATION = rOOM_LOCATION;
	}



}
