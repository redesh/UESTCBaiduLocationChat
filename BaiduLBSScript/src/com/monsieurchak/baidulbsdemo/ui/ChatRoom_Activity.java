package com.monsieurchak.baidulbsdemo.ui;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import com.monsieurchak.baidulbsdemo.R;
import com.monsieurchak.baidulbsdemo.bean.LBSMessage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 该类主要用于处理聊天室的任务
 * @author monsieurchak
 *
 */
public class ChatRoom_Activity extends Activity implements OnClickListener, UIActivity, Runnable{

	ListView dialogListView;
	Button sendButton;
	EditText sendText;
	ArrayAdapter<String> adapter;
	ArrayList<String> arrayList = new ArrayList<String>();

	//chat_text: 有待发送出去的消息
	//chat_in: 从服务器接收到的消息
	private String chat_text, chat_in;
	private String userName = "DJZhu";
	private String ip = "192.168.1.102";

	//Socket通信端口号
	public static final int PORT = 8521;

	//声明套接字对象和线程对象
	Socket socket;

	//声明客户端数据输入输出流
	ObjectInputStream in;
	ObjectOutputStream out;

	//是否成功进入聊天室的标志
	boolean flag = false;

	LBSMessage message = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		dialogListView = (ListView)findViewById(R.id.dialog_listview);
		arrayList.add("对话列表");
		adapter = new ArrayAdapter<String>(this, R.layout.dialog_listview,arrayList);
		dialogListView.setAdapter(adapter);
		sendButton = (Button)findViewById(R.id.dialog_send_btn);
		sendButton.setOnClickListener(this);
		sendText = (EditText)findViewById(R.id.dialog_send_edit);

		//在子线程中启动连接
		Thread threadInit = new Thread(){
			@Override
			public void run() {

				//判断数据合法性
				try {

					//创建Socket对象
					socket = new Socket(ip,PORT);
					in = new ObjectInputStream(socket.getInputStream());
					out = new ObjectOutputStream(socket.getOutputStream());
					message = new LBSMessage("$$" + userName + "  "  + getCurrentTime() + "上线了！");
					out.writeObject(message);
				} catch (Exception e2) {
					Log.d("TAG", "无法连接错误:" + e2.toString());
				}
			}
		};
		threadInit.start();
		
		new Thread(ChatRoom_Activity.this).start();
	}



	@Override
	public void onClick(View v) {
		chat_text = sendText.getText().toString();
		sendText.setText("");
		if (chat_text != null ) {
			if (!chat_text.equals("")) {

				//向服务器发送信息
				try {
					message = new LBSMessage("--" + userName + "  " + getCurrentTime() + "说:\n" + chat_text);
					out.writeObject(message);
				} catch (Exception e2) {
					System.out.println("客户端发送信息错误！");
				}
			}
			else {
				Toast.makeText(this, "不能发送空消息！", Toast.LENGTH_SHORT).show();
			}
		}
	}


	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {
	}



	@Override
	public void run() {
		try {
			socket = new Socket(ip,PORT);
			in = new ObjectInputStream(socket.getInputStream());
			while (true) {
				message = (LBSMessage) in.readObject();
				chat_in = message.getBODY();
				if (!"".equals(chat_in)) {
					Bundle bundle = new Bundle();
					bundle.putString("msg", chat_in);
					Message msg = new Message();
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}
		} catch (Exception e) {
			Log.d("TAG", "接受消息错误：" + e.toString());
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String content = bundle.getString("msg");
			arrayList.add(content);
			adapter.notifyDataSetChanged();
		};
	};
	
	private String getCurrentTime(){
		Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。

		t.setToNow(); // 取得系统时间。
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
		return "" + hour + ":" + minute + ":" + second + "  ";
	}
}
