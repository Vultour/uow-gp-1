package perfmon.agent.base;

import perfmon.agent.util.*;
import java.time.Instant;
import org.hyperic.sigar.Sigar;

abstract public class AgentBase{
	private Sigar sigar;
	//private DatabaseAgent database;
	private long lastMinute;
	private CpuInfo cpu;
	private MemoryInfo memory;
	private SystemInfo system;
	private HardDriveInfo hdd;
	private ProcessInfo process;

	public AgentBase(int nodeId, String dbAddress, String dbUser, String dbPassword){
		System.out.println("INF: Initializing");

		this.lastMinute = 0;
		//this.database = new DatabaseAgent(dbAddress, dbUser, dbPassword);
		this.sigar = new Sigar();
		this.cpu	= new CpuInfo();
		this.memory	= new MemoryInfo();
		this.system	= new SystemInfo();
		this.hdd	= new HardDriveInfo();
		this.process	= new ProcessInfo();

		try{ Thread.sleep(1500); } catch (Exception e){}

		System.out.println("INF: Init finished");
		this.run();
	}

	private void run(){
		System.out.println("INF: Agent started");

		while(true){
			this.pause();

			this.getCpu();

			System.out.println("CPU Metrics:");
			System.out.println("  Utilization : " + Double.toString(this.cpu.getUtilization()));
			System.out.println("  Cores       : " + Integer.toString(this.cpu.getCores()));
			System.out.println("  Model       : " + this.cpu.getModel());
			System.out.println("  Manufacturer: " + this.cpu.getManufacturer());
		}
	}

	private void getCpu(){
		try{
			this.cpu.setUtilization(this.sigar.getCpuPerc().getCombined() * 100);
			this.cpu.setModel(this.sigar.getCpuInfoList()[0].getModel());
			this.cpu.setManufacturer(this.sigar.getCpuInfoList()[0].getVendor());
			this.cpu.setCores(this.sigar.getCpuInfoList()[0].getTotalCores());
		} catch (Exception e){
			System.out.println("FTL: Exception in AgentBase::getCpu()");
			System.exit(0);
		}
	}

	private void getMemory(){
		// TODO
	}

	private void getHdd(){
		// TODO
	}

	private void getSystem(){
		// TODO
	}

	private void getProcesses(){
		// TODO
	}

	private void pause(){
		boolean debugMessage = true;
		while ((Instant.now().getEpochSecond() / 60) <= this.lastMinute){
			try{
				if (Config.AGENT_DEBUG && debugMessage){
					System.out.println("DBG: Pausing until next cycle");
					debugMessage = false;
				}
				Thread.sleep(Config.AGENT_CYCLE_PAUSE);
			}
			catch (Exception e){
				System.out.println("FATAL: Interrupt exception at AgentBase::pause()");
				System.exit(1);
			}
		}
		
		this.lastMinute = (Instant.now().getEpochSecond() / 60);
	}
}
