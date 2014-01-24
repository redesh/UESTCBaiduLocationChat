package com.monsieurchak.baidulbsdemo.ui;

import java.util.ArrayList;

import com.monsieurchak.baidulbsdemo.R;

import android.app.Activity;
import android.os.Bundle;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		dialogListView = (ListView)findViewById(R.id.dialog_listview);
		arrayList.add("对话列表");
		adapter = new ArrayAdapter<String>(this, R.layout.dialog_listview,arrayList);
		dialogListView.setAdapter(adapter);
		sendButton = (Button)findViewById(R.id.dialog_send_btn);
		sendButton.setOnClickListener(this);
		sendText = (EditText)findViewById(R.id.dialog_send_edit);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String sendMsg = sendText.getText().toString();
		sendText.setText("");
		if (sendMsg != null ) {
			if (!sendMsg.equals("")) {
				arrayList.add(sendMsg);
				adapter.notifyDataSetChanged();
			}
			else {
				Toast.makeText(this, "不能发送空消息！", Toast.LENGTH_SHORT).show();
			}
		}
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}
}
