package perfmon.agent.util;

public class SingleProcessInfo{
	private String user;
	private String name;
	private long pid;


	public SingleProcessInfo(String u, String n, long p){
		this.user = u;
		this.name = n;
		this.pid = p;
	}


	public String getUser()	{ return this.user; }
	public String getName()	{ return this.name; }
	public long getPid()	{ return this.pid; }
}
