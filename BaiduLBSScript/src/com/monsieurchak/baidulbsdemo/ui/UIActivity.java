package com.monsieurchak.baidulbsdemo.ui;

/**
 * 该接口由UIActivity继承，主要用于初始化和更新UI
 * @author monsieurchak
 *
 */

public interface UIActivity {

	//初始化
	void init();
	
	//更新UI
	void refresh(Object... params);
}
