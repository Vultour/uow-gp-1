package perfmon.database;

import perfmon.database.DatabaseWrapper;
import perfmon.util.Log;

import java.util.Hashtable;
import java.util.ArrayList;
import java.sql.ResultSet;

public class DatabaseDashboard extends DatabaseWrapper{
	public DatabaseDashboard(String host, int port, String database, String username, String password){
		super(host, port, database, username, password);
	}

	public ArrayList<Hashtable<String, String>> getNodes(int limit){
		try{
			ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			ResultSet rs = this.select(
				new String[]{"node_id", "hostname"},
				new String[]{"nodes"},
				null,
				"LIMIT " + Integer.toString(limit)
			);
			while (rs.next()){
				Hashtable<String, String> tmp = new Hashtable<String, String>();
				tmp.put("id", Integer.toString(rs.getInt("node_id")));
				tmp.put("hostname", rs.getString("hostname"));
				result.add(tmp);
			}
			return result;
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseDashboard::getNodes() - " + e.toString());
			this.close();
			System.exit(1);
		}
		return null;
	}

	public Hashtable<String, String> getSysinfo(int nodeId){
		try{
			Hashtable<String, String> result = new Hashtable<String, String>();
			ResultSet rs = this.select(
				new String[]{"*"},
				new String[]{"system"},
				new String[]{"node_id = " + Integer.toString(nodeId)},
				"LIMIT 1"
			);
			if (rs.next()){
				result.put("at", Long.toString(rs.getTimestamp("at").getTime() / 1000));
				result.put("operating_system", rs.getString("operating_system"));
				return result;
			}
		} catch(Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseDashboard::getSystem() - " + e.toString());
			this.close();
			System.exit(1);
		}
		return null;
	}

	public ArrayList<Hashtable<String, String>> getNetadapters(int nodeId){
		try{
			ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			ResultSet rs = this.select(
				new String[]{"*"},
				new String[]{"net_adapters"},
				new String[]{"node_id = " + Integer.toString(nodeId)},
				""
			);
			while (rs.next()){
				Hashtable<String, String> tmp = new Hashtable<String, String>();
				tmp.put("id", Integer.toString(rs.getInt("adapter_id")));
				tmp.put("name", rs.getString("name"));
				tmp.put("ip", rs.getString("ip"));
				tmp.put("mac", rs.getString("mac"));
				result.add(tmp);
			}
			return result;
		} catch(Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception int DatabaseDashboard::getNetadapters() - " + e.toString());
			this.close();
			System.exit(1);
		}
		return null;
	}

	public Hashtable<String, ArrayList<ArrayList<Long>>> getNetusage(int adapterId){
		try{
			Hashtable<String, ArrayList<ArrayList<Long>>> result = new Hashtable<String, ArrayList<ArrayList<Long>>>();
			ArrayList<ArrayList<Long>> in = new ArrayList<ArrayList<Long>>();
			ArrayList<ArrayList<Long>> out = new ArrayList<ArrayList<Long>>();
			ResultSet rs = this.select(
				new String[]{"*"},
				new String[]{"net_usage"},
				new String[]{"adapter_id = " + Integer.toString(adapterId)},
				"ORDER BY at ASC"
			);
			while (rs.next()){
				ArrayList<Long> tmpIn = new ArrayList<Long>();
				ArrayList<Long> tmpOut = new ArrayList<Long>();
				tmpIn.add(rs.getTimestamp("at").getTime() / 1000);
				tmpOut.add(rs.getTimestamp("at").getTime() / 1000);
				tmpIn.add(rs.getLong("ingress_bytes") / 1000);
				tmpOut.add(rs.getLong("egress_bytes") / 1000);
				in.add(tmpIn);
				out.add(tmpOut);
			}
			result.put("ingress", in);
			result.put("egress", out);
			return result;
		} catch(Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception int DatabaseDashboard::getNetadapters() - " + e.toString());
			this.close();
			System.exit(1);
		}
		return null;
	}

	public ArrayList<Hashtable<String, String>> getHardDrives(int nodeId){
		try{
			ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			ResultSet rs = this.select(
				new String[]{"*"},
				new String[]{"hdds"},
				new String[]{"node_id = " + Integer.toString(nodeId)},
				""
			);
			while (rs.next()){
				Hashtable<String, String> tmp = new Hashtable<String, String>();
				tmp.put("id", Integer.toString(rs.getInt("hdd_id")));
				tmp.put("name", rs.getString("name"));
				result.add(tmp);
			}
			return result;
		} catch(Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception int DatabaseDashboard::getNetadapters() - " + e.toString());
			this.close();
			System.exit(1);
		}
		return null;
	}

	public Hashtable<String, ArrayList<ArrayList<Long>>> getHardDriveUsage(int hddId){
		try{
			Hashtable<String, ArrayList<ArrayList<Long>>> result = new Hashtable<String, ArrayList<ArrayList<Long>>>();
			ArrayList<ArrayList<Long>> total = new ArrayList<ArrayList<Long>>();
			ArrayList<ArrayList<Long>> used = new ArrayList<ArrayList<Long>>();
			ResultSet rs = this.select(
				new String[]{"*"},
				new String[]{"hdd_usage"},
				new String[]{"hdd_id = " + Integer.toString(hddId)},
				"ORDER BY at ASC"
			);
			while (rs.next()){
				ArrayList<Long> tmpTotal = new ArrayList<Long>();
				ArrayList<Long> tmpUsed = new ArrayList<Long>();
				tmpTotal.add(rs.getTimestamp("at").getTime() / 1000);
				tmpUsed.add(rs.getTimestamp("at").getTime() / 1000);
				tmpTotal.add(rs.getLong("total") / 1000);
				tmpUsed.add(rs.getLong("used") / 1000);
				total.add(tmpTotal);
				used.add(tmpUsed);
			}
			result.put("total", total);
			result.put("used", used);
			return result;
		} catch(Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception int DatabaseDashboard::getNetadapters() - " + e.toString());
			this.close();
			System.exit(1);
		}
		return null;
	}
}
