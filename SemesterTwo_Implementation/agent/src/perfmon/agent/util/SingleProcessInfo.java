package perfmon.agent.util;

public class SingleProcessInfo{
	private String user;
	private String name;
	private int pid;


	public SingleProcessInfo(String u, String n, int p){
		this.user = u;
		this.name = n;
		this.pid = p;
	}


	public String getUser()	{ return this.user; }
	public String getName()	{ return this.name; }
	public int getPid()	{ return this.pid; }
}
