package com.monsieurchak.baidulbsdemo.ui;

import com.monsieurchak.baidulbsdemo.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Register_Activity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//注册界面,手稿版本以空白代替，待完善
		setContentView(R.layout.layout_vain);
		
		//手稿版本，以Dialog代替
		View testRegistView = View.inflate(this, R.layout.layout_register, null);
		final Dialog registDialog = new Dialog(this, R.style.auth_dialog);
		registDialog.setContentView(testRegistView);
		registDialog.show();
		
		Button quitButton = (Button)testRegistView.findViewById(R.id.test_button_yeah);
		quitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registDialog.cancel();
				finish();
			}
		});
	}
}
