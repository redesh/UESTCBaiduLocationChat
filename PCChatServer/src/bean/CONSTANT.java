package bean;

public class CONSTANT {

	/**
	 * 以下为Message的HEAD标志符常量
	 */
	
	//普通消息头部标志
	public static final int MSG_HEAD_ORDINAl = 1;
	
	//逻辑操作消息头部标志，包括登陆、注销等
	public static final int MSG_HEAD_OPERATED = 2;
	
	//账户操作消息头部，包括注册，查询等
	public static final int MSG_HEAD_COUNT = 3;
	
	//服务器事务性广播头部
	public static final int MSG_HEAD_BROADCAST = 4;
	
	//聊天室相关消息头部
	public static final int MSG_HEAD_ROOM = 5;
	
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
	
	
	/**
	 * 以下为Message的ADDITION中用户信息相关的标示符常量
	 */
	
	//用户名
	public static final String MSG_USERNAME = "MSG_USERNAME";
	
	//密码
	public static final String MSG_PASSWORD = "MSG_PASSWORD";
}
