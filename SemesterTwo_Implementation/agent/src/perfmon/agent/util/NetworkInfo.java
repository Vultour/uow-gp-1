package perfmon.agent.util;

import perfmon.agent.util.NetworkInterfaceInfo;
import java.util.Hashtable;

public class NetworkInfo{
	private Hashtable<String, NetworkInterfaceInfo> net;


	public NetworkInfo(){
		this.net = new Hashtable<String, NetworkInterfaceInfo>();
	}


	public Hashtable<String, NetworkInterfaceInfo> getInterfaces(){ return this.net; }

	public void setInterface(String name, String ip, String mac, long rx, long tx){
		if (this.net.get(name) == null){
			this.net.put(name, new NetworkInterfaceInfo(name, ip, mac, rx, tx));
		} else{
			this.net.get(name).update(rx ,tx);
		}
	}
}
