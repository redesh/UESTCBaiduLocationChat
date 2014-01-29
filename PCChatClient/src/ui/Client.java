package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import server.CurrentUser;

import bean.CONSTANT;
import bean.LBSMessage;

/**
 * PC客户端
 * 因为没学过Swing布局，也懒得学了~这个可以完成基本的发送接受消息功能
 * 中间的大框框输入IP，点Login即可连接
 * @author monsieurchak
 *
 */
public class Client extends JFrame implements Runnable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Socket通信端口号常量
	private static final int PORT = 8521;


	Socket socket;
	Thread thread;

	ObjectInputStream in;
	ObjectOutputStream out;

	//声明字符串. name用户名，chat_text发送的信息， chat_in从服务器接受到信息
	String name, chat_text, chat_in;

	//客户端IP地址
	String ip = "192.168.56.1";

	//当前用户对象
	private static CurrentUser currentUser = null;

	//界面生成
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();
	BorderLayout borderLayout3 = new BorderLayout();
	BorderLayout borderLayout4 = new BorderLayout();
	BorderLayout borderLayout5 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel jPanel4 = new JPanel();
	JPanel jPanel5 = new JPanel();
	JTextArea jTextArea = new JTextArea();
	JButton loginButton = new JButton();
	JButton sendMessageButton = new JButton();
	JButton leaveButton = new JButton();
	JTextField nameField = new JTextField();
	JTextField ipField = new JTextField();
	JTextField textSentField = new JTextField();

	//数据通信对象
	LBSMessage lbsMessage = null;

	public Client() {
		super("Client");

		//初始化内容面板
		getContentPane().setLayout(borderLayout1);

		//初始化部件
		loginButton.setText("Login");
		loginButton.addActionListener(this);
		sendMessageButton.setText("Send");
		sendMessageButton.addActionListener(this);
		leaveButton.setText("LogOut");
		leaveButton.addActionListener(this);


		//初始化面板对象，并加入组件
		this.getContentPane().add(jPanel1,java.awt.BorderLayout.NORTH);
		jPanel1.add(loginButton);
		jPanel1.add(sendMessageButton);
		jPanel1.add(leaveButton);

		jPanel2.setLayout(borderLayout2);
		nameField.setText("用户名");
		jPanel2.add(nameField);
		this.getContentPane().add(jPanel2,java.awt.BorderLayout.EAST);

		jPanel3.setLayout(borderLayout3);
		ipField.setText("192.168.1.102");
		jPanel3.add(ipField);
		this.getContentPane().add(jPanel3, java.awt.BorderLayout.WEST);

		jPanel4.setLayout(borderLayout4);
		textSentField.setText("发送信息");
		jPanel4.add(textSentField);
		this.getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

		jPanel5.setLayout(borderLayout5);
		jTextArea.setText("接受信息");
		jPanel5.add(jTextArea);
		this.getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

		this.setSize(400, 400);
		this.setVisible(true);
	}



	public static void main(String [] args) {
		Client client = new Client();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		currentUser = new CurrentUser();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			//获取用户名
			name = nameField.getText();

			//获取服务器IP
			ip = ipField.getText();

			//判断数据合法性
			if (true) {
				try {
					//创建Socket对象
					socket = new Socket(ip,PORT);
					out = new ObjectOutputStream(socket.getOutputStream());
					Date time = new Date(System.currentTimeMillis());
					SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
					String timestr= format.format(time);
					HashMap<String, String> user = new HashMap<String, String>();
					user.put(CONSTANT.MSG_USERNAME, "USERNAME");
					user.put(CONSTANT.MSG_PASSWORD, "PASSWORD");
					lbsMessage = new LBSMessage("$$" + name + "  " + timestr + "上线了！");
					lbsMessage.setHEAD(CONSTANT.MSG_HEAD_OPERATED);
					lbsMessage.setADDITION(CONSTANT.MSG_ADDITION_LOGIN);
					lbsMessage.setUSER("2011042020030");
					lbsMessage.setPASS("djzhu2213");
					out.writeObject(lbsMessage);
					out.flush();
				} catch (Exception e2) {
					jTextArea.setText("Socket连接错误: " + e2);
				}
			}
			thread = new Thread(this);
			thread.start();
		}
		else if (e.getSource() == sendMessageButton) {

			if (currentUser.isLogin()) {	//当前用户已经成功登录
				//获取需要发送的信息
				chat_text = textSentField.getText();
				if (chat_text != null) {
					Date time = new Date(System.currentTimeMillis());
					SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
					String timestr= format.format(time);

					//向服务器发送信息
					try {
						lbsMessage = new LBSMessage("--" + nameField.getText() + "  " + timestr + "说:\n" + chat_text);
						out.writeObject(lbsMessage);
					} catch (Exception e2) {
						System.out.println("客户端发送信息错误！");
					}
				}
				else {
					try {
						lbsMessage = new LBSMessage("快说话！");
						out.writeObject(lbsMessage);
					} catch (Exception e2) {
						System.out.println("客户端发送信息错误！");
					}
				}
			}
			else {
				jTextArea.setText("账户未成功登录!\n请检查用户名和密码!\n");
			}
		}
		else if (e.getSource() == leaveButton) {
			if (currentUser.isLogin()) {	//本账户已经成功登录
				try {
					lbsMessage = new LBSMessage();
					lbsMessage.setHEAD(CONSTANT.MSG_HEAD_OPERATED);
					lbsMessage.setADDITION(CONSTANT.MSG_ADDITION_LOGOUT);
					out.writeObject(lbsMessage);
					out.flush();
				} catch (Exception e2) {
					System.err.println("客户端退出错误: " + e2);
				}
				currentUser.setLogin(false);
			}
			try {
				out.close();
				in.close();
				socket.close();
			} catch (Exception e2) {
				System.err.println("客户端退出错误: " + e2);
			}
			this.setVisible(false);

		}
	}


	/**
	 * 在线程中处理从服务器接受消息
	 */
	@Override
	public void run() {

		try {

			//注意ObjectInputStream初始化之后不能立即使用(如ObjectInputStream.readObject()方法)
			//大概是因为ObjectInputStream初始化需要时间的原因
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e1) {
			System.err.println("ObjectInputStream初始化错误: " + e1);
		}
		while (true) {
			try {
				lbsMessage = (LBSMessage) in.readObject();

				switch (lbsMessage.getHEAD()) {
				case CONSTANT.MSG_HEAD_ORDINAl:	//收到普通广播消息
					System.out.print("消息接收");
					//读取服务器发来的数据BODY
					chat_in = lbsMessage.getBODY();
					if (chat_in != null && !(chat_in.equals(""))) {

						//显示消息
						jTextArea.append(chat_in + "\n\n");
					}
					break;

				case CONSTANT.MSG_HEAD_BROADCAST:	//收到事务性广播消息

					//读取数据的ADDITION
					String ADD = lbsMessage.getADDITION();

					//读取数据的USER，以判断广播接收对象
					String USER = lbsMessage.getUSER();
					if(USER.equals('\"' + "2011042020030" + '\"')){	//广播接收对象为本客户端
						if (ADD.equals(CONSTANT.MSG_ADDITION_LOGIN_SUCCEED)) {	//登录成功
							jTextArea.setText("登陆成功!\n");
							currentUser.setLogin(true);	//当前用户成功登录
							System.out.println("客户端登录成功，本客户端");
						}
						else {	//登录失败
							System.out.println("客户端登录失败，本客户端");
						}
					}
					else {	//广播接收对象不是本客户端

					}
					break;

				default:
					break;
				}

			} catch (Exception e) {
			}
		}
	}

}
