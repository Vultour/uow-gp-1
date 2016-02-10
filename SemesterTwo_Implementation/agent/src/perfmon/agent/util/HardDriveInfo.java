package perfmon.agent.util;

import perfmon.agent.util.SingleHardDriveInfo;
import java.util.Hashtable;

public class HardDriveInfo{
	private Hashtable<String, SingleHardDriveInfo> hdd;


	public HardDriveInfo(){
		this.hdd = new Hashtable<String, SingleHardDriveInfo>();
	}


	public Hashtable<String, SingleHardDriveInfo> getHardDrives(){ return this.hdd; }

	public void addHardDrive(String name, long total, long used){
		this.hdd.put(name, new SingleHardDriveInfo(name, total, used));
	}
}
