package perfmon.agent.util;

import perfmon.agent.util.SingleProcessInfo;
import java.util.ArrayList;

public class ProcessInfo{
	private ArrayList<SingleProcessInfo> ps;


	public ProcessInfo(){
		this.ps = new ArrayList<SingleProcessInfo>();
	}


	public ArrayList<SingleProcessInfo> getProcesses(){ return this.ps; }

	public void addProcess(String user, String name, long pid){
		this.ps.add(new SingleProcessInfo(user, name, pid));
	}
}
