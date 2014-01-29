package server;

import bean.UserInfo;

public class CurrentUser {

	//当前用户的基本信息
	UserInfo currentUser = null;
	
	//当前用户是否已经成功登录
	boolean isLogin = false;

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	//当前用户加入的聊天室
	//
	
	
}
