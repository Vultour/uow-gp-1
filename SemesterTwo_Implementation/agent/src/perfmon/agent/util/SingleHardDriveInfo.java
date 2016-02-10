package perfmon.agent.util;

public class SingleHardDriveInfo{
	private String name;
	private long total;
	private long used;


	public SingleHardDriveInfo(String n, long t, long u){
		this.name = n;
		this.total = t;
		this.used = u;
	}


	public String getName()	{ return this.name; }
	public long getTotal()	{ return this.total; }
	public long getUsed()	{ return this.used; }
}
