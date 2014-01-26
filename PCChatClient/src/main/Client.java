package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
	
	DataInputStream in;
	DataOutputStream out;
	
	//是否登录的标记
	boolean flag = false;
	
	//声明字符串. name用户名，chat_text发送的信息， chat_in从服务器接受到信息
	String name, chat_text, chat_in;
	
	//客户端IP地址
	String ip = "192.168.1.102";
	
	//界面生成
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();
	BorderLayout borderLayout3 = new BorderLayout();
	BorderLayout borderLayout4 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel jPanel4 = new JPanel();
	JTextArea jTextArea = new JTextArea();
	JButton loginButton = new JButton();
	JButton sendMessageButton = new JButton();
	JButton leaveButton = new JButton();
	JTextField nameField = new JTextField();
	JTextField ipField = new JTextField();
	JTextField textSentField = new JTextField();
	
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
		ipField.setText("服务器IP地址");
		jPanel3.add(ipField);
		this.getContentPane().add(jPanel3, java.awt.BorderLayout.WEST);
		
		jPanel4.setLayout(borderLayout4);
		ipField.setText("发送信息");
		jPanel4.add(jTextArea);
		this.getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);
		
		this.setSize(400, 400);
		this.setVisible(true);
	}
	
	
	
	public static void main(String [] args) {
		Client client = new Client();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			
			//获取用户名
			name = nameField.getText();
			
			//获取服务器IP
			ip = "192.168.1.102";
			
			//判断数据合法性
			if (true) {
				try {
					
					//创建Socket对象
					socket = new Socket(ip,PORT);
					in = new DataInputStream(socket.getInputStream());
					out = new DataOutputStream(socket.getOutputStream());
					Date time = new Date(System.currentTimeMillis());
					SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
					String timestr= format.format(time);
					out.writeUTF("$$" + name + "  " + timestr + "上线了！");
				} catch (Exception e2) {
					System.out.println("无法建立连接");
				}
			}
			thread = new Thread(this);
			thread.start();
			flag = true;
		}
		else if (e.getSource() == sendMessageButton) {
			
			//获取需要发送的信息
			chat_text = textSentField.getText();
			if (chat_text != null) {
				Date time = new Date(System.currentTimeMillis());
				SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
				String timestr= format.format(time);
				
				//向服务器发送信息
				try {
					out.writeUTF("--" + nameField.getText() + "  " + timestr + "说:\n" + chat_text);
				} catch (Exception e2) {
					System.out.println("客户端发送信息错误！");
				}
			}
			else {
				try {
					out.writeUTF("我勒个擦，快说话！");
				} catch (Exception e2) {
					System.out.println("客户端发送信息错误！");
				}
			}
		}
		else {
			if (e.getSource() == leaveButton) {
				if (flag == true) {
					try {
						out.writeUTF("##" + name + "下线了！");
						
						out.close();
						in.close();
						socket.close();
					} catch (Exception e2) {
						System.out.println("客户端退出错误！");
					}
				}
				flag = false;
				this.setVisible(false);
			}
		}
	}

	/**
	 * 在线程中处理从服务器接受消息
	 */
	@Override
	public void run() {
		while (true) {
			try {
				
				//读取服务器发来的数据信息
				chat_in = in.readUTF();
				if (!chat_in.equals("")) {
					System.out.println("Message: " + chat_in);
				}
				//显示消息
				jTextArea.append(chat_in + "\n\n");
			} catch (Exception e) {
			}
		}
	}

}
