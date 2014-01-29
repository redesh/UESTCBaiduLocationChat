package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {

	private Connection conn = null;
	String driver = "com.mysql.jdbc.Driver";	// 驱动程序名
	private String user;						// MySQL配置时的用户名
	private String password;					// MySQL配置时的密码
	private String url;							// URL指向要访问的数据库名，例如："jdbc:mysql://127.0.0.1:3306/test"
	PreparedStatement statement = null;  		//An object that represents a precompiled SQL statement.

	
	public DBManager(){
		
	}

	/**
	 * 连接到MYSQL数据库
	 * @param url URL指向要访问的数据库名，例如："jdbc:mysql://127.0.0.1:3306/test"
	 * @param user MySQL配置时的用户名
	 * @param password MySQL配置时的密码
	 */
	public DBManager(String url, String user, String password){

		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * 连接到MYSQL
	 */
	public void connectMYSQL(){
		try {   
			Class.forName(driver);   
			conn = DriverManager.getConnection(url,user, password );   
		}  
		//捕获加载驱动程序异常  
		catch ( ClassNotFoundException cnfex ) {  
			System.err.println( "装载 JDBC/ODBC 驱动程序失败: " + cnfex);  
		}   
		//捕获连接数据库异常  
		catch ( SQLException sqlex ) {  
			System.err.println( "无法连接数据库: " + sqlex);  
		} 
	}


	/**
	 *  断开MYSQL连接
	 */
	public void disConnectMYSQL() {  
		try {  
			if (conn != null){
				conn.close();
			}
		} catch (Exception e) {  
			System.err.println("关闭数据库问题: " + e);  
		}  
	}

	/**
	 * 执行SELECT语句
	 * @param sql //将要执行的SQL语句
	 * @return
	 */
	public ResultSet selectSQL(String sql) {  
		ResultSet rs = null;  
		try {  
			statement = conn.prepareStatement(sql);  
			rs = statement.executeQuery(sql);  
		} catch (SQLException e) {
			System.err.println("SELECT数据库问题 ：" + e);  
		}  
		return rs;  
	}

	/**
	 * 执行INSERT语句  
	 * @param sql 将要执行的SQL语句
	 * @return
	 */
	public boolean insertSQL(String sql) {  
		try {  
			statement = conn.prepareStatement(sql);  
			statement.executeUpdate();  
			return true;  
		} catch (SQLException e) {  
			System.err.println("插入数据库时出错: " + e);  
		} catch (Exception e) {  
			System.err.println("插入时出错: " + e);  
		}  
		return false;  
	} 

	/**
	 * 执行DELETE语句
	 * @param sql 将要执行的SQL语句
	 * @return
	 */
	public boolean deleteSQL(String sql) {  
		try {  
			statement = conn.prepareStatement(sql);  
			statement.executeUpdate();  
			return true;  
		} catch (SQLException e) {  
			System.err.println("插入数据库时出错: " + e);  
		} catch (Exception e) {  
			System.err.println("插入时出错: " + e);  
		}  
		return false;  
	}

	/**
	 * 执行UPDATE语句
	 * @param sql 将要执行的SQL语句
	 * @return
	 */
	public boolean updateSQL(String sql) {  
		try {  
			statement = conn.prepareStatement(sql);  
			statement.executeUpdate();  
			return true;  
		} catch (SQLException e) {  
			System.err.println("UPDATE数据库时出错: " + e);  
		} catch (Exception e) {  
			System.err.println("UPDATE时出错: " + e);  
		}  
		return false;  
	}

	/**
	 * 提取ResultSet信息
	 * @param res
	 */
	public ArrayList<HashMap<String, String>> showResult(ResultSet res){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = null;
		try {
			while(res.next()){
				map = new HashMap<String, String>();
				map.put("ID", res.getString("ID"));
				map.put("PASSWORD", res.getString("PASSWORD"));
				map.put("NICKNAME", res.getString("NICKNAME"));
				map.put("EMAIL", res.getString("EMAIL"));
				map.put("STATES", res.getString("STATES"));
				map.put("LASTLOGINTIME", res.getString("LASTLOGINTIME"));
				map.put("SIGN", res.getString("SIGN"));
				list.add(map);
			}
		} catch (SQLException e) {
			System.err.println("解析ResultSet时错误: " + e);
		}
		return list;
	}
}
