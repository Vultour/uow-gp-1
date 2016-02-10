package perfmon.agent.util;

public class MemoryInfo{
	private long total;
	private long used;
	private long cached;


	public MemoryInfo(){
		this.total = 0;
		this.used = 0;
		this.cached = 0;
	}

	public MemoryInfo(int t, int u, int c){
		this.setTotal(t);
		this.setUsed(u);
		this.setCached(c);
	}


	public long getTotal()	{ return this.total; }
	public long getUsed()	{ return this.used; }
	public long getCached()	{ return this.cached; }

	public void setTotal(long t)	{ this.total = t; }
	public void setUsed(long u)	{ this.used = ((u > this.total) && (this.total > 0))?(this.total):(u); }
	public void setCached(long c)	{ this.cached = ((c > this.total) && (this.total > 0))?(this.total):(c); }
}
