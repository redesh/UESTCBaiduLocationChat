package com.monsieurchak.baidulbsdemo.bean;

import java.io.Serializable;

/**
 * 本类定义了Socket通信的数据包格式
 * @author monsieurchak
 *
 */
public class LBSMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String HEAD = null;		//消息头,APP_Constants类提供常量
	private String BODY = null;		//消息体
	private String SENDER = null;	//消息发送者，匿名发送表示为null
	private String RECEIVER = null;	//收信人,发送给所有用户表示为null
	private String ADDITION = null;	//附加信息
	
	public LBSMessage() {
		
	}
	
	/**
	 * 构建一条普通消息
	 * HEAD = MSG_HEAD_ORDINAl
	 * SENDER = null
	 * RECEIVER = null
	 * ADDITION = null
	 * @param BODY 消息体
	 */
	public LBSMessage(String BODY){
		this.HEAD = APP_Constants.MSG_HEAD_ORDINAl;
		this.BODY = BODY;
	}
	
	public String getHEAD() {
		return HEAD;
	}
	public void setHEAD(String hEAD) {
		HEAD = hEAD;
	}
	public String getBODY() {
		return BODY;
	}
	public void setBODY(String bODY) {
		BODY = bODY;
	}
	public String getSENDER() {
		return SENDER;
	}
	public void setSENDER(String sENDER) {
		SENDER = sENDER;
	}
	public String getRECEIVER() {
		return RECEIVER;
	}
	public void setRECEIVER(String rECEIVER) {
		RECEIVER = rECEIVER;
	}
	public String getADDITION() {
		return ADDITION;
	}
	public void setADDITION(String aDDITION) {
		ADDITION = aDDITION;
	}
	@Override
	public String toString() {
		return "Message [HEAD=" + HEAD + ", BODY=" + BODY + ", SENDER="
				+ SENDER + ", RECEIVER=" + RECEIVER + ", ADDITION=" + ADDITION
				+ "]";
	}
	
	
}