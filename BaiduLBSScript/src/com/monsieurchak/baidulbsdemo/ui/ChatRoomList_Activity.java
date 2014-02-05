package com.monsieurchak.baidulbsdemo.ui;

import java.util.HashMap;
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
			String roomJoinID = chatRoomEditText.getText().toString();
			HashMap<String, Object> roomMap = new HashMap<String, Object>();
			roomMap.put(CONSTANT.ROOM_ID, roomJoinID);	//（可拓展）加入其他属性
			
			//向MainService发送请求
			MainService.addTask(new Task(Task.JOIN_ROOM, roomMap));
			MainService.addActivity(ChatRoomList_Activity.this);
			
			break;

		case R.id.test_baidu_map_create:	//创建聊天室
			String roomCreateID = chatRoomEditText.getText().toString();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(CONSTANT.ROOM_ID, roomCreateID);
			MainService.addTask(new Task(Task.CREATE_ROOM, map));
			MainService.addActivity(ChatRoomList_Activity.this);
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
		if (params[0].equals(CONSTANT.SUCCEED)) {
//			Toast.makeText(this, "加入聊天室成功", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this,ChatRoom_Activity.class);
			intent.putExtra("roomID", (String) params[1]);
			startActivity(intent);
		}
		else if(params[0].equals(CONSTANT.FAILED)) {
			Toast.makeText(this, "加入聊天室失败", Toast.LENGTH_SHORT).show();
		}
		else {
			
		}
	}
	
}
