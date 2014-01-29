package com.monsieurchak.baidulbsdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.monsieurchak.baidulbsdemo.bean.DBInfo;

public class DBHelper extends SQLiteOpenHelper{

	public DBHelper(Context context) {
		//CursorFactory设置为null,使用默认值  
		super(context, DBInfo.DATABASE_NAME, null, DBInfo.DATABASE_VERSION);  
	}
    //数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBInfo.TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		DBInfo.id +
                " VARCHAR, " +
                DBInfo.passWord +
                " VARCHAR, " +
                DBInfo.nickName +
                " VARCHAR, " +
                DBInfo.states +
                " VARCHAR, " +
                DBInfo.lastLoginTime +
                " VARCHAR, " +
                DBInfo.loginCounter +
                " VARCHAR, " +
                DBInfo.sign +
                " TEXT)");	
	}

	//如果DATABASE_VERSION值被改变(默认为1)，系统发现数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("TAG", "数据库升级，数据被重置！！");
		onCreate(db);
	}

}
