package bean;

public class DBInfo {
	
    public static final String DATABASE_NAME = "baidulbs";  
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_USER = "users";
    
    //DB参数
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/baidulbs";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "super";
    
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
