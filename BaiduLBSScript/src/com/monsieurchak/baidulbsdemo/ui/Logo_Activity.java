package com.monsieurchak.baidulbsdemo.ui;

import com.monsieurchak.baidulbsdemo.R;
import com.monsieurchak.baidulbsdemo.bean.UserInfo;
import com.monsieurchak.baidulbsdemo.db.DBManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

/**
 * 该Acitivity主要用以现实欢迎信息
 * @author monsieurchak
 *
 */

public class Logo_Activity extends Activity implements AnimationListener{

	TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_logo);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(3000);
		alphaAnimation.setAnimationListener(this);
		textView = (TextView)findViewById(R.id.logo_Text);
		textView.setAnimation(alphaAnimation);
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		Intent intent = new Intent(Logo_Activity.this, Login_Activity.class);
		startActivity(intent);
		this.finish();
		
//		DBManager dbManager = new DBManager(this);
//		UserInfo userInfo = new UserInfo("2011042020030", "djzhu223013", "DJ23ZHU", "emai23l", "o2k", "233", "223", "noth2323ing is impossible!");
//		dbManager.dbAdd(userInfo);
//		Log.d("TAG", dbManager.query().toString());
//		dbManager.closeDB();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

}
