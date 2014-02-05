package mix;

import java.io.IOException;

import com.monsieurchak.baidulbsdemo.bean.LBSMessage;

/**
 * 服务器端广播程序
 * @author monsieurchak
 *
 */
public class BroadCast extends Thread{

	ClientThread clientThread;
	ServerThread serverThread;

	LBSMessage lbsMessage = null;

	public BroadCast(ServerThread serverThread) {
		this.serverThread = serverThread;
	}

	@Override
	public void run() {
		System.out.println("Here is BroadCast");
		while (true) {
			try {
				Thread.sleep(200);
			} catch (Exception e){
			}
			synchronized (serverThread.messages) {
				if (serverThread.messages.isEmpty()) {
					continue;
				}

				//获取服务器端信息数组存储的第一条信息
				lbsMessage = (LBSMessage)this.serverThread.messages.firstElement();

				synchronized (serverThread.clients) {
					for (int i = 0; i < serverThread.clients.size(); i++) {
						clientThread = (ClientThread)serverThread.clients.elementAt(i);
						try {
						
							//向有记录的每一个客户端发送数据信息
							clientThread.out.writeObject(lbsMessage);
							System.out.println("成功发送一条消息" + i);
						} catch (IOException e2) {
							System.out.println("普通广播I/O错误--》--：" + e2.toString());
							//serverThread.clients.removeElement(clientThread);
						}
					}

					//删除已经发送过的那条数据信息
					this.serverThread.messages.removeElement(lbsMessage);
				}
			}
		}

	}
}

