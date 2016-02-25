package perfmon.agent.base;

import perfmon.agent.util.*;
import perfmon.database.DatabaseAgent;
import java.time.Instant;
import org.hyperic.sigar.Sigar;

abstract public class AgentBase{
	private Sigar sigar;
	private DatabaseAgent database;
	private long lastMinute;
	private CpuInfo cpu;
	private MemoryInfo memory;
	private SystemInfo system;
	private HardDriveInfo hdd;
	private ProcessInfo process;

	public AgentBase(int nodeId, String dbAddress, String dbUser, String dbPassword){
		Log.$(Log.INFO, "Initializing");

		this.lastMinute	= 0;
		this.database	= new DatabaseAgent(dbAddress, Config.DATABASE_PORT, Config.DATABASE_NAME, dbUser, dbPassword);
		this.sigar	= new Sigar();
		this.cpu	= new CpuInfo();
		this.memory	= new MemoryInfo();
		this.system	= new SystemInfo();
		this.hdd	= new HardDriveInfo();
		this.process	= new ProcessInfo();

		Runtime.getRuntime().addShutdownHook(new AgentShutdownThread(this){
			public void run(){
				try{
					Log.$(Log.DEBUG, "Entered JVM shutdown hook thread");
					this.agent.shutdown();	
				} catch (Exception e){
					e.printStackTrace();
					Log.$(Log.FATAL, "Exception in JVM shutdown hook thread [AgentShutdownThread]");
					System.exit(1);
				}
			}
		});

		try{ Thread.sleep(1500); } catch (Exception e){ e.printStackTrace(); }

		Log.$(Log.INFO, "Init finished");
		this.run();
	}

	public void shutdown(){
		try{
			Log.$(Log.INFO, "Agent shutting down");
			this.database.close();
			this.sigar.close();
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in AgentBase::shutdown()");
			System.exit(1);
		}
	}

	private void run(){
		Log.$(Log.INFO, "Agent started");

		while(true){
			this.pause();

			this.getCpu();
			this.getMemory();
			this.getHdd();
			this.getSystem();

			if (Config.AGENT_DEBUG){ this.printMetrics(); }
		}
	}

	private void getCpu(){
		try{
			this.cpu.setUtilization(this.sigar.getCpuPerc().getCombined() * 100);
			this.cpu.setModel(this.sigar.getCpuInfoList()[0].getModel());
			this.cpu.setManufacturer(this.sigar.getCpuInfoList()[0].getVendor());
			this.cpu.setCores(this.sigar.getCpuInfoList()[0].getTotalCores());
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in AgentBase::getCpu() - " + e.toString());
			System.exit(1);
		}
	}

	private void getMemory(){
		try{
			this.memory.setTotal(this.sigar.getMem().getTotal());
			this.memory.setUsed(this.sigar.getMem().getUsed()); // TODO: Compare with .getActualUsed()!
			// this.memory.setCached(); // SIGAR does not gather this information - try JNI
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in AgentBase::getMemory() - " + e.toString());
			System.exit(1);
		}
	}

	private void getHdd(){
		try{
			org.hyperic.sigar.FileSystem[] fs = this.sigar.getFileSystemList();
			for (int i = 0; i < fs.length; i++){
				try{
					boolean next = false;
					for (int j = 0; j < Config.AGENT_FILTER_HDD_DIR.length; j++){
						if (fs[i].getDirName().contains(Config.AGENT_FILTER_HDD_DIR[j])){
							next = true;
							Log.$(Log.DEBUG, "HDD filter match (path): '" + fs[i].getDirName() + " - excluding from scan");
						}
					}
					for (int j = 0; j < Config.AGENT_FILTER_HDD_FS.length; j++){
						if (fs[i].getDevName().contains(Config.AGENT_FILTER_HDD_FS[j])){
							next = true;
							Log.$(Log.DEBUG, "HDD filter match (filesystem): '" + fs[i].getDirName() + " - excluding from scan");
						}
					}
					if (next){
						continue;
					}
					org.hyperic.sigar.FileSystemUsage fsu = this.sigar.getFileSystemUsage(fs[i].getDirName());
					if (fsu.getTotal() > 0){
						this.hdd.addHardDrive(
							fs[i].getDevName() + " [" + fs[i].getDirName() + "]",
							fsu.getTotal(),
							fsu.getUsed()
						);
					}
				} catch (org.hyperic.sigar.SigarException e){
					Log.$(Log.DEBUG, "Detected an inaccessible HDD (insufficient permissions) - " + fs[i].getDirName() + " - excluding from scan");
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in AgentBase::getHdd() - " + e.toString());
			System.exit(1);
		}
	}

	private void getSystem(){
		try{
			this.system.setHostname(this.sigar.getFQDN());
			this.system.setOs(System.getProperty("os.name"));
			this.system.setIp(this.sigar.getNetInterfaceConfig().getAddress());
			
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in AgentBase::getSystem() - " + e.toString());
			System.exit(1);
		}
	}

	private void getProcesses(){
		try{
			// TODO
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in AgentBase::getProcesses() - " + e.toString());
			System.exit(1);
		}
	}

	private void pause(){
		boolean debugMessage = true;
		while ((Instant.now().getEpochSecond() / 60) <= this.lastMinute){
			try{
				if (Config.AGENT_DEBUG && debugMessage){
					Log.$(Log.DEBUG, "Pausing until next cycle");
					debugMessage = false;
				}
				Thread.sleep(Config.AGENT_CYCLE_PAUSE);
			}
			catch (Exception e){
				e.printStackTrace();
				Log.$(Log.FATAL, "Interrupt exception at AgentBase::pause()");
				System.exit(1);
			}
		}
		
		this.lastMinute = (Instant.now().getEpochSecond() / 60);
	}

	private void printMetrics(){
		Log.$(Log.DEBUG, "CPU Metrics:");
		Log.$(Log.DEBUG, "  Utilization : " + Double.toString(this.cpu.getUtilization()));
		Log.$(Log.DEBUG, "  Cores       : " + Integer.toString(this.cpu.getCores()));
		Log.$(Log.DEBUG, "  Model       : " + this.cpu.getModel());
		Log.$(Log.DEBUG, "  Manufacturer: " + this.cpu.getManufacturer());

		Log.$(Log.DEBUG, "Memory Metrics:");
		Log.$(Log.DEBUG, "  Total: " + Long.toString(this.memory.getTotal()));
		Log.$(Log.DEBUG, "  Used : " + Long.toString(this.memory.getUsed()));

		Log.$(Log.DEBUG, "HDD Metrics:");
		Log.$(Log.DEBUG, "  Detected drives: " + Integer.toString(this.hdd.getHardDrives().size()));
		for (String key: this.hdd.getHardDrives().keySet()){
			Log.$(Log.DEBUG, "  Device name: " + this.hdd.getHardDrives().get(key).getName());
			Log.$(Log.DEBUG, "  Total KB   : " + Long.toString(this.hdd.getHardDrives().get(key).getTotal()));
			Log.$(Log.DEBUG, "  Used KB    : " + Long.toString(this.hdd.getHardDrives().get(key).getUsed()));
			Log.$(Log.DEBUG, "  ---");
		}
		
		Log.$(Log.DEBUG, "System info:");
		Log.$(Log.DEBUG, "  Hostname  : " + this.system.getHostname());
		Log.$(Log.DEBUG, "  OS        : " + this.system.getOs());
		Log.$(Log.DEBUG, "  IP Address: " + this.system.getIp().toString());

		Log.$(Log.DEBUG, "Process metrics:");
		Log.$(Log.DEBUG, "  Unavailable");
	}
}
