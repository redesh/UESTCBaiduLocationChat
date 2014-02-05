package com.monsieurchak.baidulbsdemo.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.monsieurchak.baidulbsdemo.R;
import com.monsieurchak.baidulbsdemo.bean.LBSMessage;
import com.monsieurchak.baidulbsdemo.bean.TAG;
import com.monsieurchak.baidulbsdemo.bean.Task;
import com.monsieurchak.baidulbsdemo.logic.MainService;
import com.monsieurchak.baidulbsdemo.logic.ServiceThread;

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
public class ChatRoom_Activity extends Activity implements OnClickListener, UIActivity{

	ListView dialogListView;
	Button sendButton;
	EditText sendText;
	ArrayAdapter<String> adapter;
	ArrayList<String> arrayList = new ArrayList<String>();

	//chat_text: 有待发送出去的消息
	//chat_in: 从服务器接收到的消息
	private String chat_text;

	//是否成功进入聊天室的标志
	boolean flag = false;

	LBSMessage message = null;
	
	ServiceThread.chatThread chatThread;
	
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
		MainService.addActivity(ChatRoom_Activity.this);
	}



	@Override
	public void onClick(View v) {
		chat_text = sendText.getText().toString();
		if (chat_text != null ) {
			if (!chat_text.equals("")) {

				//向服务器发送信息
				try {
					message = new LBSMessage(">>" + "说:\n" + chat_text);
					HashMap<String, Object> send = new HashMap<String, Object>();
					send.put("msg", message);
					MainService.addTask(new Task(Task.ROOM_CHAT, send));
				} catch (Exception e2) {
					Log.d(TAG.NET,"客户端发送信息错误！");
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
		String msg = (String)params[0];
		arrayList.add(msg);
		adapter.notifyDataSetChanged();
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
