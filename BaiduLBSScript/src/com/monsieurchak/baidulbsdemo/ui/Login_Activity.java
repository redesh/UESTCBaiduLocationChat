package com.monsieurchak.baidulbsdemo.ui;

import java.util.HashMap;

import com.monsieurchak.baidulbsdemo.R;
import com.monsieurchak.baidulbsdemo.bean.APP_Constants;
import com.monsieurchak.baidulbsdemo.bean.Task;
import com.monsieurchak.baidulbsdemo.logic.MainService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login_Activity extends Activity implements UIActivity,OnClickListener{

	Button loginButton;
	EditText iDEditText, pwEditText;
	TextView findIDBackTextView, registIDTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//启动主服务
		Intent intent = new Intent(this, MainService.class);
		startService(intent);
		
		loginButton = (Button)findViewById(R.id.Login);
		loginButton.setOnClickListener(this);
		iDEditText = (EditText)findViewById(R.id.ID);
		pwEditText = (EditText)findViewById(R.id.passWord);
		findIDBackTextView = (TextView)findViewById(R.id.findIDBack);
		findIDBackTextView.setOnClickListener(this);
		registIDTextView = (TextView)findViewById(R.id.registID);
		registIDTextView.setOnClickListener(this);
	}

	@Override
	public void init() {

	}

	//根据MainService返回的结果刷新UI
	@Override
	public void refresh(Object... params) {
		if (params[0].equals(APP_Constants.SUCCESSFUL)) {
			Intent intent = new Intent(this,ChatRoomList_Activity.class);
			startActivity(intent);
			this.finish();
		}
		else if(params[0].equals(APP_Constants.FAILE)) {
			Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
		}
		else {
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Login:
			String userID = iDEditText.getText().toString();
			String passWord = pwEditText.getText().toString();
			if (userID == null || userID.equals("")) {
				Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
			}
			else if (passWord == null || passWord.equals("")) {
				Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			}
			else {
				HashMap<String, Object> userMap = new HashMap<String, Object>();
				userMap.put(APP_Constants.UESERID, userID);
				userMap.put(APP_Constants.USERPW, passWord);
				
				Log.d("TAG", "onClick");
				//将用户名和密码打包提交给MainService处理
				MainService.addTask(new Task(Task.LOGIN, userMap));
				
				//将本Activity一并提交到MainService等待回调处理
				MainService.addActivity(this);
			}
			break;
			
		case R.id.findIDBack:
			Toast.makeText(Login_Activity.this, "找回密码", Toast.LENGTH_SHORT).show();
			break;

		case R.id.registID:
			//注册界面
			Intent intent = new Intent(this, Register_Activity.class);
			startActivity(intent);
			
			break;

		default:
			break;
		}
	}

}
