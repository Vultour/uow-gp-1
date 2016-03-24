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
				result.put("at", rs.getString("at"));
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
				"LIMIT 1"
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
}
