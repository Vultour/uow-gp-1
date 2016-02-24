package perfmon.agent.util;

import perfmon.agent.base.AgentBase;

public class AgentShutdownThread extends Thread{
	protected AgentBase agent;

	public AgentShutdownThread(AgentBase a){
		super();
		this.agent = a;
	}
}
