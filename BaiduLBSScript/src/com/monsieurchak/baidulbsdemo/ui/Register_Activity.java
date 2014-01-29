package com.monsieurchak.baidulbsdemo.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.monsieurchak.baidulbsdemo.R;
import com.monsieurchak.baidulbsdemo.bean.DBInfo;
import com.monsieurchak.baidulbsdemo.bean.UserInfo;
import com.monsieurchak.baidulbsdemo.db.DBManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Register_Activity extends Activity implements Runnable{

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
	
	//Socket服务器IP地址
	private String ip = "192.168.1.102";

	//Socket通信端口号
	public static final int PORT = 8521;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//注册界面,手稿版本以空白代替，待完善
		setContentView(R.layout.layout_vain);

		//		//手稿版本，以Dialog代替
		//		View testRegistView = View.inflate(this, R.layout.layout_register, null);
		//		final Dialog registDialog = new Dialog(this, R.style.auth_dialog);
		//		registDialog.setContentView(testRegistView);
		//		registDialog.show();

		//		Button quitButton = (Button)testRegistView.findViewById(R.id.test_button_yeah);
		//		quitButton.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View v) {
		//				registDialog.cancel();
		//				finish();
		//			}
		//		});

		idEditText = (EditText)findViewById(R.id.editTextID);
		passwordEditText = (EditText)findViewById(R.id.editTextPassword);
		emailEditText = (EditText)findViewById(R.id.editTextEmail);
		registerButton = (Button)findViewById(R.id.buttonRegister);
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String id = idEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				String email = emailEditText.getText().toString();
				switch (checkRegist(id, password, email)) {
				case APPROPRIATE:
//					DBManager dbManager = new DBManager(this);
//					UserInfo userInfo = new UserInfo("2011042020030", "djzhu223013", "DJ23ZHU", "emai23l", "o2k", "233", "223", "noth2323ing is impossible!");
//					dbManager.dbAdd(userInfo);
//					Log.d("TAG", dbManager.query().toString());
//					dbManager.closeDB();

					thread = new Thread(){
						public void run() {
							try {

								//创建Socket对象
								socket = new Socket(ip,PORT);
								in = new DataInputStream(socket.getInputStream());
								out = new DataOutputStream(socket.getOutputStream());
								out.writeUTF(DBInfo.REGISTER_STRING);
								
							} catch (Exception e2) {
								Log.d("TAG", "无法连接错误:" + e2.toString());
							}
						};
					};
					thread.start();
					
					break;
					
				case ERROR_ID:

					break;
					
				case ERROR_PASSWORD:

					break;

				case ERROR_EMAIL:

					break;
					
				default:
					break;
				}
			}
		});
	}

	public int checkRegist(String id, String password, String email){
		return APPROPRIATE;
	}
	

	@Override
	public void run() {
		try {
			socket = new Socket(ip,PORT);
			in = new DataInputStream(socket.getInputStream());
			String ans;
			boolean done = false;
			while (!done) {
				ans = in.readUTF();
				if (!DBInfo.REGISTER_SUCCEED.equals(ans)) {
					//注册成功
					socket.close();
					in.close();
					out.close();
					done = true;
				}
				if (!DBInfo.LOGIN_SUCCED.equals(ans)) {
					//登陆成功
					socket.close();
					in.close();
					out.close();
					done = true;
				}
			}
		} catch (Exception e) {
		}
	}
}
