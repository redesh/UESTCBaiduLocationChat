package mix;

import java.awt.BorderLayout;
import javax.swing.*;



import java.awt.event.*;

/**
 * 服务器端主程序
 * 负责界面显示，启动相关服务等
 * @author monsieurchak
 *
 */
public class Server extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();
	
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JButton jButton1 = new JButton();
	JButton jButton2 = new JButton();
	JScrollPane jScrollPane = new JScrollPane();
	
	//服务器端接受信息文本框
	static JTextArea jTextArea1 = new JTextArea();
	boolean bool = false, start = false;
	
	//声明
	ServerThread serverThread;
	Thread thread;
	
	public Server(){
		super("Server");
		
		//设置内容面板
		getContentPane().setLayout(borderLayout1);
		
		//初始化按钮
		jButton1.setText("启动服务器");
		jButton1.addActionListener(this);
		jButton2.setText("停止服务器");
		jButton2.addActionListener(this);

		//初始化jPanel1面板对象，并向其加入组件
		this.getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);
		jPanel1.add(jButton1);
		jPanel1.add(jButton2);
		
		//初始化jPanel2面板对象，并向其加入组件
		jTextArea1.setText("");
		jPanel2.setLayout(borderLayout2);
		jPanel2.add(jScrollPane, java.awt.BorderLayout.CENTER);
		jScrollPane.getViewport().add(jTextArea1);
		this.getContentPane().add(jPanel2,java.awt.BorderLayout.CENTER);
		
		this.setSize(400,400);
		this.setVisible(true);
	}
	
	public static void main(String [] args) {
		Server server = new Server();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jButton1) {
			serverThread = new ServerThread();
			serverThread.start();
		}
		else if (e.getSource() == jButton2) {
			bool = false;
			start = false;
			serverThread.finalize();
			this.setVisible(false);
		}
	}

}
