package com.monsieurchak.baidumapdemo.logic;

import com.monsieurchak.baidumapdemo.bean.UserInfo;

/**
 * 本类代表一个用户
 * @author monsieurchak
 *
 */
public class CurrentUser {
	
	UserInfo userInfo = new UserInfo();
	
	public CurrentUser() {
		
	}
	
	public CurrentUser(UserInfo user){
		this.userInfo = user;
	}
	
	
}
