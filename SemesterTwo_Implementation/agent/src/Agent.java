import perfmon.agent.AgentMulti;
import perfmon.agent.util.Config;

class Agent{
	public static void main(String[] args){
		AgentMulti agent = new perfmon.agent.AgentMulti(0, Config.DATABASE_HOST, Config.DATABASE_USER, Config.DATABASE_PASS);
	}
}
