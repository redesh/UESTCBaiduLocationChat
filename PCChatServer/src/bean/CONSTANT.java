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
	
	/**
	 * 一下为Message的ADDITION标识符常量
	 */
	
	//登录
	public static final String MSG_ADDITION_LOGIN = "LOGIN";
	
	//注销
	public static final String MSG_ADDITION_LOGOUT = "LOGOUT";
	
	//登录成功广播
	public static final String MSG_ADDITION_LOGIN_SUCCEED = "MSG_ADDITION_LOGIN_SUCCEED";
	
	//登录失败广播
	public static final String MSG_ADDITION_LOGIN_FAILED = "MSG_ADDITION_LOGIN_FAILED";
	
	/**
	 * 以下为Message的ADDITION.LOGIN相关的标示符常量
	 */
	
	//用户名
	public static final String MSG_USERNAME = "MSG_USERNAME";
	
	//密码
	public static final String MSG_PASSWORD = "MSG_PASSWORD";
}
