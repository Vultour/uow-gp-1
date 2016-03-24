package perfmon.database;

import perfmon.database.DatabaseWrapper;
import perfmon.util.Log;

import java.util.Hashtable;
import java.sql.ResultSet;

public class DatabaseDashboard extends DatabaseWrapper{
	public DatabaseDashboard(String host, int port, String database, String username, String password){
		super(host, port, database, username, password);
	}

	public Hashtable<Integer, String> getNodes(int limit){
		try{
			Hashtable<String, Integer> result = new Hashtable<String, Integer>();
			ResultSet rs = this.select(
				new String[]{"node_id", "hostname"},
				new String[]{"nodes"},
				null,
				"LIMIT " + Integer.toString(limit)
			);
			while (rs.next()){
				result.put(rs.getString("hostname"), rs.getInt("node_id"));
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
}
