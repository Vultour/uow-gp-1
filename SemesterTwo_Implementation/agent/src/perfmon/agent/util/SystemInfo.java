package perfmon.agent.util;

public class SystemInfo{
	private String hostname;
	private IPAddress ip;
	private String os;


	public SystemInfo(){
		this.hostname = "Unknown";
		this.ip = null;
		this.os = "Unknown";
	}

	public SystemInfo(String h, String ip, String os){
		this.hostname = h;
		this.ip = new IPAddress(ip);
		this.os = os;
	}


	public String getHostname()	{ return this.hostname; }
	public IPAddress getIp()	{ return this.ip; }
	public String getOs()		{ return this.os; }

	public void setHostname(String h)	{ this.hostname = h; }
	public void setIp(String ip)		{ this.ip = new IPAddress(ip); }
	public void setIp(byte[] ip)		{ this.ip = new IPAddress(ip); }
	public void setIp(byte b1, byte b2, byte b3, byte b4){ this.ip = new IPAddress(b1, b2, b3, b4); }
	public void setOs(String os)		{ this.os = os; }
}
