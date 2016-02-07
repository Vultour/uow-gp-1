package perfmon.agent.util;

import java.util.Hashtable;

public class HardDriveInfo{
	private Hashtable<String, SingleHardDriveInfo> hdd;


	public HardDriveInfo(){
		this.hdd = new Hashtable<String, SingleHardDriveInfo>();
	}


	public Hashtable<String, SingleHardDriveInfo> getHardDrives(){ return this.hdd; }

	public void addHardDrive(String name, int total, int used){
		this.hdd.put(name, new SingleHardDriveInfo(name, total, used));
	}
}

class SingleHardDriveInfo{
	private String name;
	private int total;
	private int used;


	public SingleHardDriveInfo(String n, int t, int u){
		this.name = n;
		this.total = t;
		this.used = u;
	}


	public String getName()	{ return this.name; }
	public int getTotal()	{ return this.total; }
	public int getUsed()	{ return this.used; }
}
