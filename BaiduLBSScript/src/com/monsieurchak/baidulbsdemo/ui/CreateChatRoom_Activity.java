package com.monsieurchak.baidulbsdemo.ui;

import com.monsieurchak.baidulbsdemo.R;
import com.monsieurchak.baidulbsdemo.bean.CONSTANT;
import com.monsieurchak.baidulbsdemo.bean.LBSMessage;
import com.monsieurchak.baidulbsdemo.bean.RoomInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 创建新的聊天室
 * 功能待完善
 * @author monsieurchak
 *
 */
public class CreateChatRoom_Activity extends Activity{

	EditText roomIDEditText;
	EditText roomNameEditText;
	Button createRoomButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_chat_room);
		
		roomIDEditText = (EditText)findViewById(R.id.editTextRoomID);
		roomNameEditText = (EditText)findViewById(R.id.editTextRoomName);
		createRoomButton = (Button)findViewById(R.id.buttonCreateRoom);
		createRoomButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String roomID = roomIDEditText.getText().toString();
				String roomName = roomNameEditText.getText().toString();
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setID(roomID);
				roomInfo.setNAME(roomName);
				LBSMessage createRoomMSG = new LBSMessage();
				createRoomMSG.setHEAD(CONSTANT.MSG_HEAD_ROOM);
				createRoomMSG.setADDITION(CONSTANT.MSG_ADDITION_CREATEROOM);
				createRoomMSG.setRoomInfo(roomInfo);
				
			}
		});
	}
	
}
