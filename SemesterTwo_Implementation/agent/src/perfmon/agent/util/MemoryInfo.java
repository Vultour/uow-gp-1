package perfmon.agent.util;

public class MemoryInfo{
	private int total;
	private int used;
	private int cached;


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


	public int getTotal()	{ return this.total; }
	public int getUsed()	{ return this.used; }
	public int getCached()	{ return this.cached; }

	public void setTotal(int t)	{ this.total = t; }
	public void setUsed(int u)	{ this.used = ((u > this.total) && (this.total > 0))?(this.total):(u); }
	public void setCached(int c)	{ this.cached = ((c > this.total) && (this.total > 0))?(this.total):(c); }
}
