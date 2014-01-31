package bean;

public class UserInfo {

	
	public int _id;
	public String ID = null;
	public String PASSWORD = null;
	public String NICKNAME = null;
	public String EMAIL = null;
	public String STATUS = null;
	public String LAST_LOGIN_TIME = null;	//上次登录时间
	public String LOGIN_COUNTER = null;	//登陆总次数
	public String SIGN = null;	//签名
	public String ROOMS [] = null;	//当前用户已加入的聊天室
	
	public UserInfo() {
	}
	public UserInfo(String iD, String pASSWORD, String nICKNAME, String eMAIL,
			String sTATES, String lAST_LOGIN_TIME, String lOGIN_COUNTER,
			String sign) {
		ID = iD;
		PASSWORD = pASSWORD;
		NICKNAME = nICKNAME;
		EMAIL = eMAIL;
		STATUS = sTATES;
		LAST_LOGIN_TIME = lAST_LOGIN_TIME;
		LOGIN_COUNTER = lOGIN_COUNTER;
		SIGN = sign;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	public String getNICKNAME() {
		return NICKNAME;
	}
	public void setNICKNAME(String nICKNAME) {
		NICKNAME = nICKNAME;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getSTATES() {
		return STATUS;
	}
	public void setSTATES(String sTATES) {
		STATUS = sTATES;
	}
	public String getLAST_LOGIN_TIME() {
		return LAST_LOGIN_TIME;
	}
	public void setLAST_LOGIN_TIME(String lAST_LOGIN_TIME) {
		LAST_LOGIN_TIME = lAST_LOGIN_TIME;
	}
	public String getLOGIN_COUNTER() {
		return LOGIN_COUNTER;
	}
	public void setLOGIN_COUNTER(String lOGIN_COUNTER) {
		LOGIN_COUNTER = lOGIN_COUNTER;
	}
	public String getSign() {
		return SIGN;
	}
	public void setSign(String sign) {
		SIGN = sign;
	}
	@Override
	public String toString() {
		return "UserInfo [ID=" + ID + ", PASSWORD=" + PASSWORD + ", NICKNAME="
				+ NICKNAME + ", EMAIL=" + EMAIL + ", STATES=" + STATUS
				+ ", LAST_LOGIN_TIME=" + LAST_LOGIN_TIME + ", LOGIN_COUNTER="
				+ LOGIN_COUNTER + ", Sign=" + SIGN + "]";
	}
}
