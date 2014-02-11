package com.monsieurchak.baidumapdemo.bean;

import java.util.HashMap;

public class Task {

	public static final int LOGIN = 1;		//登录
	public static final int REGIST = 2;		//注册帐号
	public static final int FINDIDBACK = 3;	//找回密码
	public static final int JOIN_ROOM = 4;	//加入聊天室
	public static final int CREATE_ROOM = 5;//创建聊天室
	public static final int ROOM_CHAT = 6;	//聊天室消息任务
	public static final int ROOM_SEARCH = 7;//搜寻聊天室
	public static final int QUIT_ROOM = 8;	//退出聊天室
	
	//任务ID，全局唯一，指定MainService的Task类型
	public int TaskID;
	
	//任务附带属性
	public HashMap<String, Object> params;

	public Task(int taskID, HashMap<String, Object> params){
		this.TaskID = taskID;
		this.params = params;
	}
	
	public int getTaskID() {
		return TaskID;
	}

	public void setTaskID(int taskID) {
		TaskID = taskID;
	}

	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
	
	

}
