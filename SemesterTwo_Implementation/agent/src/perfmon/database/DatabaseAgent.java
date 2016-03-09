package perfmon.database;

import perfmon.database.DatabaseWrapper;

import perfmon.util.Log;
import perfmon.agent.util.CpuInfo;
import perfmon.agent.util.MemoryInfo;
import perfmon.agent.util.SystemInfo;
import perfmon.agent.util.HardDriveInfo;
import perfmon.agent.util.NetworkInfo;
import perfmon.agent.util.ProcessInfo;

import java.sql.ResultSet;

import java.util.ArrayList;

public class DatabaseAgent extends DatabaseWrapper{
	private int node;

	private int cpu;

	public DatabaseAgent(String nodeHost, String host, int port, String database, String username, String password){
		super(host, port, database, username, password);

		this.node = -1;
		while (this.node == -1){
			this.node = this.getNodeId(nodeHost);
		}
		Log.$(Log.INFO, "Node ID detected as " + Integer.toString(this.node));
	}

	public void init(SystemInfo s, CpuInfo c){
		try{
			boolean failure = false;
			// System info
			ResultSet rs = this.select(
				new String[]{"*"},
				new String[]{"system"},
				new String[]{"node_id = " + Integer.toString(this.node)},
				"ORDER BY at DESC LIMIT 1"
			);
			if (rs.next()){
				if (!rs.getString("operating_system").equals(s.getHostname())){ failure = true; }
			} else{ failure = true; }

			if (failure){
				this.insert(
					"system",
					new String[]{
						"node_id",
						"at",
						"operating_system"
					},
					new String[]{
						Integer.toString(this.node),
						"NOW()",
						s.getOs()
					},
					new boolean[]{
						false,
						false,
						true
					},
					""
				);
			}
			rs.close();

			// CPU
			failure = false;
			rs = this.select(
				new String[]{"*"},
				new String[]{"cpus"},
				new String[]{"node_id = " + Integer.toString(this.node)},
				"ORDER BY at DESC LIMIT 1"
			);
			if (rs.next()){
				if (
					rs.getString("model").equals(c.getModel())
					&& rs.getString("manufacturer").equals(c.getManufacturer())
					&& (rs.getInt("cores") == c.getCores())
				){ this.cpu = rs.getInt("cpu_id"); }
			} else{ failure = true; }
			rs.close();

			if (failure){
				this.insert(
					"cpus",
					new String[]{
						"node_id",
						"at",
						"manufacturer",
						"model",
						"cores"
					},
					new String[]{
						Integer.toString(this.node),
						"NOW()",
						c.getManufacturer(),
						c.getModel(),
						Integer.toString(c.getCores())
					},
					new boolean[]{
						false,
						false,
						true,
						true,
						false
					},
					""
				);
				rs = this.select(
					new String[]{"*"},
					new String[]{"cpus"},
					new String[]{"node_id = " + Integer.toString(this.node)},
					"ORDER BY at DESC LIMIT 1"
				);
				this.cpu = rs.getInt("cpu_id");
				rs.close();
			}

		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseAgent::init() - " + e.toString());
			System.exit(1);
		}
	}

	public void putCpu(CpuInfo cpu){
		this.insert(
			"cpu_usage",
			new String[]{
				"cpu_id",
				"at",
				"utilization"
			},
			new String[]{
				Integer.toString(this.cpu),
				"NOW()",
				Double.toString(cpu.getUtilization())
			},
			new boolean[]{
				false,
				false,
				false
			},
			""
		);
	}

	public void putMemory(MemoryInfo mem){
		this.insert(
			"memory_usage",
			new String[]{
				"node_id",
				"at",
				"total",
				"used"
			},
			new String[]{
				Integer.toString(this.node),
				"NOW()",
				Long.toString(mem.getTotal()),
				Long.toString(mem.getUsed())
			},
			new boolean[]{
				false,
				false,
				false,
				false
			},
			""
		);
	}

	public void putSystem(SystemInfo sys){
		// TODO: System info
	}

	public void putHdd(HardDriveInfo hdd){
		try{
			for (String key: hdd.getHardDrives().keySet()){
				boolean failure = false;
				int hid = -1;
				ResultSet rs = this.select(
					new String[]{"*"},
					new String[]{"hdds"},
					new String[]{
						"node_id = " + Integer.toString(this.node),
						"name = '" + hdd.getHardDrives().get(key).getName() + "'"
					},
					"LIMIT 1"
				);
				if (rs.next()){
					hid = rs.getInt("hdd_id");
				} else{ failure = true; }
				rs.close();

				if (failure){
					this.insert(
						"hdds",
						new String[]{"node_id", "name"},
						new String[]{
							Integer.toString(this.node),
							hdd.getHardDrives().get(key).getName()
						},
						new boolean[]{false, true},
						""
					);
					rs = this.select(
						new String[]{"*"},
						new String[]{"hdds"},
						new String[]{
							"node_id = " + Integer.toString(this.node),
							"name = '" + hdd.getHardDrives().get(key).getName() + "'"
						},
						"LIMIT 1"
					);
					if (rs.next()){
						hid = rs.getInt("hdd_id");
					} else{
						Log.$(Log.FATAL, "Couldn't add new HDD entry");
						System.exit(1);
					}
					rs.close();
				}

				this.insert(
					"hdd_usage",
					new String[]{
						"hdd_id",
						"at",
						"total",
						"used"
					},
					new String[]{
						Integer.toString(hid),
						"NOW()",
						Long.toString(hdd.getHardDrives().get(key).getTotal()),
						Long.toString(hdd.getHardDrives().get(key).getUsed())
					},
					new boolean[]{
						false,
						false,
						false,
						false
					},
					""
				);
			}
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseAgent::putCpu() - " + e.toString());
			System.exit(1);
		}
	}

	public void putNetwork(NetworkInfo net){
		try{
			for (String key: net.getInterfaces().keySet()){
				int nid = -1;
				ResultSet rs = this.select(
					new String[]{"*"},
					new String[]{"net_adapters"},
					new String[]{
						"node_id = " + Integer.toString(this.node),
						"name = '" + net.getInterfaces().get(key).getName() + "'"
					},
					"LIMIT 1"
				);
				if (rs.next()){ nid = rs.getInt("adapter_id"); }
				rs.close();

				if (nid == -1){
					this.insert(
						"net_adapters",
						new String[]{
							"node_id",
							"name",
							"ip",
							"mac"
						},
						new String[]{
							Integer.toString(this.node),
							net.getInterfaces().get(key).getName(),
							net.getInterfaces().get(key).getIp(),
							net.getInterfaces().get(key).getMac()
						},
						new boolean[]{
							false,
							true,
							true,
							true
						},
						""
					);
				}

				rs = this.select(
					new String[]{"*"},
					new String[]{"net_adapters"},
					new String[]{
						"node_id = " + Integer.toString(this.node),
						"name = '" + net.getInterfaces().get(key).getName() + "'"
					},
					"LIMIT 1"
				);
				if (rs.next()){	nid = rs.getInt("adapter_id"); }
				rs.close();

				if (nid == -1){
					Log.$(Log.FATAL, "Couldn't insert new network interface in DatabaseAgent::putNetwork()");
					System.exit(1);
				}

				this.insert(
					"net_usage",
					new String[]{
						"adapter_id",
						"at",
						"ingress_bytes",
						"egress_bytes"
					},
					new String[]{
						Integer.toString(nid),
						"NOW()",
						Long.toString(net.getInterfaces().get(key).getRx()),
						Long.toString(net.getInterfaces().get(key).getTx())
					},
					new boolean[]{
						false,
						false,
						false,
						false,
					},
					""
				);

			}
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseAgent::putNetwork() - " + e.toString());
			System.exit(1);
		}
	}

	public void putProcess(ProcessInfo ps){
		// TODO: Process info
	}


	private int getNodeId(String h){
		int id = -1;
		try{
			ResultSet r = this.select(new String[]{"node_id"}, new String[]{"nodes"}, new String[]{"hostname = '" + h + "'"}, "");
			if (r.next()){
				id = Integer.parseInt(r.getString("node_id"));
			}
			r.close();

			if (id == -1){
				int x = this.insert("nodes", new String[]{"hostname"}, new String[]{ h }, new boolean[]{ true }, "");
				System.out.println(Integer.toString(x));
				r.close();
			}

			return id;
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseAgent::getNodeId() - " + e.toString());
			System.exit(1);
		}
		return -1;
	}
}
