package com.monsieurchak.baidulbsdemo.ui;

import java.util.HashMap;

import com.monsieurchak.baidulbsdemo.R;
import com.monsieurchak.baidulbsdemo.bean.APP_Constants;
import com.monsieurchak.baidulbsdemo.bean.Task;
import com.monsieurchak.baidulbsdemo.logic.MainService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChatRoomList_Activity extends Activity implements OnClickListener, UIActivity{

	Button joinButton,createButton;
	EditText chatRoomEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_rooms_list);
		joinButton = (Button)findViewById(R.id.test_baidu_map_yeah);
		joinButton.setOnClickListener(this);
		createButton = (Button)findViewById(R.id.test_baidu_map_create);
		createButton.setOnClickListener(this);
		chatRoomEditText = (EditText)findViewById(R.id.test_baidu_map_tip);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.test_baidu_map_yeah:	//加入指定聊天室
			HashMap<String, Object> roomMap = new HashMap<String, Object>();
			roomMap.put(APP_Constants.ROOMID, chatRoomEditText.getText().toString());
			//roomMap.put(key, value)	//（可拓展）加入其他属性
			
			//向MainService发送请求
			MainService.addTask(new Task(Task.JOIN_ROOM, roomMap));
			MainService.addActivity(this);
			
			break;

		case R.id.test_baidu_map_create:	//创建聊天室
			//向MainService发送请求
			//MainService.addTask(new Task(Task.JOIN_ROOM, roomMap));
			//MainService.addActivity(this);
			
			//这里仅作一个Demo用~
			Intent intent = new Intent(this, CreateChatRoom_Activity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		if (params[0].equals(APP_Constants.SUCCESSFUL)) {
			Intent intent = new Intent(this,ChatRoom_Activity.class);
			startActivity(intent);
		}
		else if(params[0].equals(APP_Constants.FAILE)) {
			Toast.makeText(this, "加入聊天室失败", Toast.LENGTH_SHORT).show();
		}
		else {
			
		}
	}
	
}
