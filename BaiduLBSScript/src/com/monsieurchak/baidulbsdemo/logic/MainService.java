package com.monsieurchak.baidulbsdemo.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.monsieurchak.baidulbsdemo.bean.APP_Constants;
import com.monsieurchak.baidulbsdemo.bean.Task;
import com.monsieurchak.baidulbsdemo.ui.UIActivity;

/**
 * 该类主要用以处理业务逻辑
 * 实现了Runnable，在新线程中执行
 * @author monsieurchak
 *
 */

public class MainService extends Service implements Runnable{

	//任务数组
	private static Queue<Task> tasks = new LinkedList<Task>();

	//Activity队列
	private static ArrayList<Activity> activities = new ArrayList<Activity>();

	boolean isRun;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void run() {
		while(isRun){
			Task task = null;
			//检查是否有任务
			if (!tasks.isEmpty()) {
				task = tasks.poll();
				doTask(task);
			}
			else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		isRun = true;
		Thread thread = new Thread(this);
		thread.start();
	}

	//核心业务逻辑添加到MainService中执行
	public static void addTask(Task task){
		tasks.add(task);
	}

	//需要更改UI的Activity将自身添加到MainService
	public static void addActivity(Activity activity){
		activities.add(activity);
	}

	/**
	 * 根据Activity名查找Activities队列
	 * @param name
	 * @return 若有相应Activity返回之，若无返回null
	 */
	private Activity findActivityByName(String name){
		for (Activity activity : activities) {
			if (null != activity) {
				if (activity.getClass().getName().indexOf(name) > 0) {
					Log.d("TAG", "Return activity:  " + name);
					return activity;
				}
			}
		}
		return null;
	}

	private void doTask(Task task){
		//通过HandleMessage处理UI更新
		Message msg = handler.obtainMessage();
		msg.what = -1;
		switch (task.TaskID) {
		case Task.LOGIN:
			String userName = (String)task.getParams().get(APP_Constants.UESERID);
			String passWord = (String)task.getParams().get(APP_Constants.USERPW);
			Log.d("TAG", "ID:" + userName + "..." + "PW" + passWord);

			boolean isLoginSuccessfully;
			//添加“登陆”要执行的相关业务
			//访问数据库，返回用户验证结果
			//手稿版本，模拟用户 『用户名：user 密码：pass』
			//模拟服务器验证模拟用户登陆
			if (userName.equals("user") && passWord.equals("pass")) {
				isLoginSuccessfully = true;
			}
			else {
				isLoginSuccessfully = false;
			}

			//验证成功
			if (isLoginSuccessfully) {
				msg.arg1 = APP_Constants.SUCCESSFUL;
			}
			//验证失败
			else {
				msg.arg1 = APP_Constants.FAILE;
			}

			msg.what = task.TaskID;		//提请Handler更新UI
			break;
		case Task.REGIST:
			//添加”注册帐号”要执行的相关业务

			//msg.what = task.TaskID;		//提请Handler更新UI
			break;
		case Task.FINDIDBACK:
			//添加”找回密码”要执行的相关业务

			//msg.what = task.TaskID;		//提请Handler更新UI
			break;
		case Task.JOIN_ROOM:

			String RoomID = (String)task.getParams().get(APP_Constants.ROOMID);
			boolean isAllowJoin;

			//访问服务器，返回是否允许加入
			isAllowJoin = askJoinRoom(RoomID);
			//something（）	//（可选）从服务器获取其余参数

			//允许加入
			if (isAllowJoin) {
				msg.arg1 = APP_Constants.SUCCESSFUL;
			}
			//拒绝加入
			else {
				msg.arg1 = APP_Constants.FAILE;
			}
			Log.d("TAG", "roomid:  " + RoomID);

			msg.what = task.TaskID;		//提请Handler更新UI
			break;

		default:
			break;
		}

		//如果有UI更新请求向Handler发送Message
		if (msg.what != -1) {
			handler.sendMessage(msg);
		}
	}

	private boolean askJoinRoom(String roomID) {
		//添加”加入聊天室”要执行的相关业务

		//手稿版本，模拟允许加入
		return true;

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			UIActivity uiActivity;	//UIActivity
			switch (msg.what) {
			case Task.LOGIN:	//登陆后更新UI
				uiActivity = (UIActivity)findActivityByName("Login_Activity");
				if (msg.arg1 == APP_Constants.SUCCESSFUL) {
					Log.d("TAG", "登录成功");
					uiActivity.refresh(APP_Constants.SUCCESSFUL);
				}
				else {
					Log.d("TAG", "登录失败");
					uiActivity.refresh(APP_Constants.FAILE);
				}
				break;

			case Task.JOIN_ROOM:	//加入聊天室更新UI
				uiActivity = (UIActivity)findActivityByName("ChatRoomList_Activity");
				if (msg.arg1 == APP_Constants.SUCCESSFUL) {
					Log.d("TAG", "加入聊天室成功");
					uiActivity.refresh(APP_Constants.SUCCESSFUL);
				}
				else {
					Log.d("TAG", "加入聊天室失败");
					uiActivity.refresh(APP_Constants.FAILE);
				}
				break;

			default:
				break;
			}
		};
	};

}
