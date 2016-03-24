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

	public ArrayList<Hashtable<String, Integer>> getNodes(int limit){
		try{
			ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			ResultSet rs = this.select(
				new String[]{"node_id", "hostname"},
				new String[]{"nodes"},
				null,
				"LIMIT " + Integer.toString(limit)
			);
			while (rs.next()){
				Hashtable<String, String> tmp = new Hastable<String, String>();
				tmp.put("id", Integer.toString(rs.getInt("node_id")));
				tmp.put("hostname", rs.getString("hostname"));
				result.add(tmp):
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
