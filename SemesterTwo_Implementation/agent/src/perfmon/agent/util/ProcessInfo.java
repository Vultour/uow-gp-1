package perfmon.agent.util;

import java.util.ArrayList;

public class ProcessInfo{
	private ArrayList<SingleProcessInfo> ps;


	public ProcessInfo(){
		this.ps = new ArrayList<SingleProcessInfo>();
	}


	public ArrayList<SingleProcessInfo> getProcesses(){ return this.ps; }

	public void addProcess(String user, String name, int pid){
		this.ps.add(new SingleProcessInfo(user, name, pid));
	}
}

class SingleProcessInfo{
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
