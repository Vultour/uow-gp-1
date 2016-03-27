package perfmon.agent.util;

import perfmon.agent.util.IPAddress;

public class NetworkInterfaceInfo{
	private String name;
	private IPAddress ip;
	private String mac;
	private long rxLast;
	private long txLast;
	private long rx;
	private long tx;


	public NetworkInterfaceInfo(String n, String ip, String mac, long rx, long tx){
		this.name = n;
		this.ip = new IPAddress(ip);
		this.mac = mac;
		this.rxLast = 0;
		this.txLast = 0;
		this.update(rx, tx);
	}

	public void update(long rx, long tx){
		//this.rx = rx - this.rxLast;
		//this.tx = tx - this.txLast;
		//this.rxLast = rx;
		//this.txLast = tx;
		this.rx = rx;
		this.tx = tx;
	}


	public String getName()	{ return this.name; }
	public String getIp()	{ return this.ip.toString(); }
	public String getMac()	{ return this.mac; }
	public long getRx()	{ return this.rx; }
	public long getTx()	{ return this.tx; }
}
