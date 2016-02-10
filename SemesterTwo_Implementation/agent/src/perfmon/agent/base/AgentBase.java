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

		this.lastMinute	= 0;
		//this.database	= new DatabaseAgent(dbAddress, dbUser, dbPassword);
		this.sigar	= new Sigar();
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
			this.getMemory();
			this.getHdd();
			this.getSystem();

			if (Config.AGENT_DEBUG){
				System.out.println("CPU Metrics:");
				System.out.println("  Utilization : " + Double.toString(this.cpu.getUtilization()));
				System.out.println("  Cores       : " + Integer.toString(this.cpu.getCores()));
				System.out.println("  Model       : " + this.cpu.getModel());
				System.out.println("  Manufacturer: " + this.cpu.getManufacturer());
				System.out.println("Memory Metrics:");
				System.out.println("  Total: " + Long.toString(this.memory.getTotal()));
				System.out.println("  Used : " + Long.toString(this.memory.getUsed()));
				System.out.println("HDD Metrics:");
				System.out.println("  Detected drives: " + Integer.toString(this.hdd.getHardDrives().size()));
				/*
				for (String key: this.hdd.getHardDrives().keySet()){
					System.out.println("  Device name: " + this.hdd.getHardDrives().get(key).getName());
					System.out.println("  Total KB   : " + Long.toString(this.hdd.getHardDrives().get(key).getTotal()));
					System.out.println("  Used KB    : " + Long.toString(this.hdd.getHardDrives().get(key).getUsed()));
					System.out.println("  ---");
				}
				*/
				System.out.println("System info:");
				System.out.println("  Hostname  : " + this.system.getHostname());
				System.out.println("  OS        : " + this.system.getOs());
				System.out.println("  IP Address: " + this.system.getIp().toString());
				System.out.println("Process metrics:");
				System.out.println("  Unavailable");
			}
		}
	}

	private void getCpu(){
		try{
			this.cpu.setUtilization(this.sigar.getCpuPerc().getCombined() * 100);
			this.cpu.setModel(this.sigar.getCpuInfoList()[0].getModel());
			this.cpu.setManufacturer(this.sigar.getCpuInfoList()[0].getVendor());
			this.cpu.setCores(this.sigar.getCpuInfoList()[0].getTotalCores());
		} catch (Exception e){
			System.out.println("FTL: Exception in AgentBase::getCpu() - " + e.toString());
			System.exit(1);
		}
	}

	private void getMemory(){
		try{
			this.memory.setTotal(this.sigar.getMem().getTotal());
			this.memory.setUsed(this.sigar.getMem().getUsed()); // TODO: Compare with .getActualUsed()!
			// this.memory.setCached(); // SIGAR does not gather this information - try JNI
		} catch (Exception e){
			System.out.println("FTL: Exception in AgentBase::getMemory() - " + e.toString());
			System.exit(1);
		}
	}

	private void getHdd(){
		try{
			org.hyperic.sigar.FileSystem[] fs = this.sigar.getFileSystemList();
			for (int i = 0; i < fs.length; i++){
				org.hyperic.sigar.FileSystemUsage fsu = this.sigar.getFileSystemUsage(fs[i].getDirName());
				if (fsu.getTotal() > 0){
					this.hdd.addHardDrive(
						fs[i].getDevName() + " [" + fs[i].getDirName() + "]",
						fsu.getTotal(),
						fsu.getUsed()
					);
				}
			}
		} catch (Exception e){
			System.out.println("FTL: Exception in AgentBase::getHdd() - " + e.toString());
			System.exit(1);
		}
	}

	private void getSystem(){
		try{
			this.system.setHostname(this.sigar.getFQDN());
			this.system.setOs(System.getProperty("os.name"));
			this.system.setIp(this.sigar.getNetInterfaceConfig().getAddress());
			
		} catch (Exception e){
			System.out.println("FTL: Exception in AgentBase::getSystem() - " + e.toString());
			System.exit(1);
		}
	}

	private void getProcesses(){
		try{
			// TODO
		} catch (Exception e){
			System.out.println("FTL: Exception in AgentBase::getProcesses() - " + e.toString());
			System.exit(1);
		}
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
