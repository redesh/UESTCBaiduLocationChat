package com.monsieurchak.baidulbsdemo.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.monsieurchak.baidulbsdemo.R;
import com.monsieurchak.baidulbsdemo.bean.CONSTANT;
import com.monsieurchak.baidulbsdemo.bean.Task;
import com.monsieurchak.baidulbsdemo.logic.MainService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Register_Activity extends Activity implements Runnable, UIActivity{

	private static final int APPROPRIATE = 1;
	private static final int ERROR_ID = 2;
	private static final int ERROR_PASSWORD = 3;
	private static final int ERROR_EMAIL = 4;

	Button registerButton;
	EditText idEditText, passwordEditText, emailEditText;
	ImageButton iconImageButton;
	Thread thread = null;

	//声明套接字对象和线程对象
	Socket socket;

	//声明客户端数据输入输出流
	DataInputStream in;
	DataOutputStream out;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);

		idEditText = (EditText)findViewById(R.id.editTextID);
		passwordEditText = (EditText)findViewById(R.id.editTextPassword);
		emailEditText = (EditText)findViewById(R.id.editTextEmail);
		registerButton = (Button)findViewById(R.id.buttonRegister);
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String userID = idEditText.getText().toString();
				String passWord = passwordEditText.getText().toString();
				String email = emailEditText.getText().toString();
				switch (checkRegist(userID, passWord, email)) {
				case APPROPRIATE:
					HashMap<String, Object> regMap = new HashMap<String, Object>();
					
					regMap.put(CONSTANT.USERNAME, userID);
					regMap.put(CONSTANT.PASSWORD, passWord);
					regMap.put(CONSTANT.EMAIL, email);
					
					//将用户名和密码打包提交给MainService处理
					MainService.addTask(new Task(Task.REGIST, regMap));
					
					//将本Activity一并提交到MainService等待回调处理
					MainService.addActivity(Register_Activity.this);
					break;

				case ERROR_ID:

					break;

				case ERROR_PASSWORD:

					break;

				case ERROR_EMAIL:
					Toast.makeText(Register_Activity.this, "邮箱格式错误！", Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}
		});
	}

	public int checkRegist(String id, String password, String email){
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(email);
		boolean isRightEmail = m.matches();
		
		if (isRightEmail) {
			return APPROPRIATE;
		}
		else {
			return ERROR_EMAIL;
		}
	}

	@Override
	public void run() {
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void refresh(Object... params) {
		Integer MSG = (Integer) params[0];
		switch (MSG) {
		case CONSTANT.REG_SUCCED:
			Intent intent = new Intent(this,ChatRoomList_Activity.class);
			startActivity(intent);
			this.finish();
			break;
		case CONSTANT.REG_FAILED:
			Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}
