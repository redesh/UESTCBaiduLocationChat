package com.monsieurchak.baidumapdemo.db;

import java.util.ArrayList;
import java.util.List;

import com.monsieurchak.baidumapdemo.bean.DBInfo;
import com.monsieurchak.baidumapdemo.bean.UserInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {

	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public void dbAdd(UserInfo userInfo){
		db.beginTransaction();
		try {
			db.execSQL("INSERT INTO " + DBInfo.TABLE_NAME + " VALUES(null,?,?,?,?,?,?,?)", 
					new Object[]{
					userInfo.ID, 
					userInfo.PASSWORD, 
					userInfo.NICKNAME, 
					userInfo.STATUS, 
					userInfo.LAST_LOGIN_TIME, 
					userInfo.LOGIN_COUNTER, 
					userInfo.SIGN});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d("TAG", "数据插入错误:" + e.toString());
		} finally{
			db.endTransaction();
		}
	}
	
	public void dbDelete(String userID){
		db.delete(DBInfo.TABLE_NAME, DBInfo.id + " = ?", new String[]{userID});
	}
	
	public List<UserInfo> query(){
		ArrayList<UserInfo> users = new ArrayList<UserInfo>();
		Cursor c = db.rawQuery("SELECT * FROM " + DBInfo.TABLE_NAME, null);
		while (c.moveToNext()) {
			UserInfo userInfo = new UserInfo();
			userInfo.setID(c.getString(c.getColumnIndex(DBInfo.id)));
			userInfo.setPASSWORD(c.getString(c.getColumnIndex(DBInfo.passWord)));
			userInfo.setNICKNAME(c.getString(c.getColumnIndex(DBInfo.nickName)));
			userInfo.setSTATES(c.getString(c.getColumnIndex(DBInfo.states)));
			userInfo.setLAST_LOGIN_TIME(c.getString(c.getColumnIndex(DBInfo.lastLoginTime)));
			userInfo.setLOGIN_COUNTER(c.getString(c.getColumnIndex(DBInfo.loginCounter)));
			userInfo.setSign(c.getString(c.getColumnIndex(DBInfo.sign)));
			users.add(userInfo);
		}
		return users;
	}
	
	/**
	 * 查询数据库是否存在特定用户
	 * @param userID 将要查询的用户ID
	 * @return
	 */
	public boolean isExistUser(String userID){
		Cursor c = db.rawQuery("SELECT * FROM " + DBInfo.TABLE_NAME, null);
		if (c.getCount() >= 1) {
			return true;
		}
		return false;
	}
	
	public void closeDB(){
		db.close();
	}
}
