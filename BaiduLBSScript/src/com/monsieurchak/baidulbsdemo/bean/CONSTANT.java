package com.monsieurchak.baidulbsdemo.bean;

public class CONSTANT {

	/**
	 * 以下为Message的HEAD标志符常量
	 */
	
	//普通消息头部标志
	public static final int MSG_HEAD_ORDINAl = 1;
	
	//逻辑操作消息头部标志，包括登陆、注销等
	public static final int MSG_HEAD_OPERATED = 2;
	
	//账户操作消息头部标志，包括注册，查询等
	public static final int MSG_HEAD_COUNT = 3;
	
	//服务器事务性广播头部标志
	public static final int MSG_HEAD_BROADCAST = 4;
	
	//聊天室相关事务性消息头部标志，包括加入，创建聊天室
	public static final int MSG_HEAD_ROOM = 5;
	
	//聊天室普通消息头部标志
	public static final int MSG_HEAD_ROOM_CHAT = 6;
	
	/**
	 * 以下为Message的ADDITION标识符常量
	 */
	
	//登录
	public static final String MSG_ADDITION_LOGIN = "LOGIN";
	
	//注销
	public static final String MSG_ADDITION_LOGOUT = "LOGOUT";
	
	//登录成功广播
	public static final String MSG_ADDITION_LOGIN_SUCCEED = "MSG_ADDITION_LOGIN_SUCCEED";
	
	//登录失败广播
	public static final String MSG_ADDITION_LOGIN_FAILED = "MSG_ADDITION_LOGIN_FAILED";
	
	//注册
	public static final String MSG_ADDITION_REGISTER = "MSG_ADDITION_REGISTER";
	
	//注册成功
	public static final String MSG_ADDITION_REGISTER_SUCCEED = "MSG_ADDITION_REGISTER_SUCCEED";
	
	//注册失败
	public static final String MSG_ADDITION_REGISTER_FAILED = "MSG_ADDITION_REGISTER_FAILED";
	
	//注销成功
	public static final String MSG_ADDITION_LOGOUT_SUCCEED = "MSG_ADDITION_LOGOUT_SUCCEED";
	
	//注销失败
	public static final String MSG_ADDITION_LOGOUT_FAILED = "MSG_ADDITION_LOGOUT_FAILED";
	
	//创建聊天室
	public static final String MSG_ADDITION_CREATEROOM = "MSG_ADDITION_CREATEROOM";
	
	//加入聊天室
	public static final String MSG_ADDITION_JOINROOM = "MSG_ADDITION_JOINROOM";
	
	//创建聊天室成功
	public static final String MSG_ADDITION_CREATEROOM_SUCCEED = "MSG_ADDITION_CREATEROOM_SUCCEED";
	
	//创建聊天室失败
	public static final String MSG_ADDITION_CREATEROOM_FAILED = "MSG_ADDITION_CREATEROOM_FAILED";
	
	//加入聊天室成功
	public static final String MSG_ADDITION_JOINROOM_SUCCEED = "MSG_ADDITION_JOINROOM_SUCCEED";
	
	//加入聊天室失败
	public static final String MSG_ADDITION_JOINROOM_FAILED = "MSG_ADDITION_JOINROOM_FAILED";
	
	//退出聊天室
	public static final String MSG_ADDITION_QUITROOM = "MSG_ADDITION_JOINROOM_QUITROOM";
	
	//所有用户
	public static final String MSG_ALLUSER = "MSG_ALLUSER";
	
	/**
	 * APP内部通信标志
	 */
	
	//成功
	public static final int SUCCEED = 1;
	
	//失败
	public static final int FAILED = 2;
	
	//用户名
	public static final String USERNAME = "USERNAME";
	
	//密码
	public static final String PASSWORD = "PASSWORD";
	
	public static final String EMAIL = "email";
	
	//登录成功
	public static final int LOGIN_SUCCEED = 3;
	
	//登录失败
	public static final int LOGIN_FAILED = 4;
	
	//注册成功
	public static final int REG_SUCCED = 5;
	
	//注册失败
	public static final int REG_FAILED = 6;
	
	//聊天室ID
	public static final String ROOM_ID = "ROOM_ID";
}
