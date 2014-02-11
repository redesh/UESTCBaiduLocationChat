package com.monsieurchak.baidumapdemo.bean;

public class DBInfo {
	
    public static final String DATABASE_NAME = "BaiduLBS.db";  
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "localUsers";
    
    //注册&登录指令
    public static final String REGISTER_STRING = "__R_E_G__";
    public static final String LOGIN_STRING = "__L_O_G_I_N__";
    public static final String REGISTER_SUCCEED = "__L_O_G_I_N_S_U_C_C_E_E_D__";
    public static final String LOGIN_SUCCED = "__L_O_G_I_N_S_U_C_C_E_E_D__";
    
    //User表 列名
	public static final String id = "ID";
	public static final String passWord = "PASSWORD";
	public static final String nickName = "NICKNAME";
	public static final String E_mail = "EMAIL";
	public static final String states = "STATES";
	public static final String lastLoginTime = "LASTLOGINTIME";
	public static final String loginCounter = "LOGINCOUNTER";
	public static final String sign = "SIGN";
}
