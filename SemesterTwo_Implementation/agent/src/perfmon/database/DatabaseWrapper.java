package perfmon.database;

import perfmon.agent.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.sql.*;

public abstract class DatabaseWrapper{
	protected Connection connection;
	protected Statement statement;

	public DatabaseWrapper(String host, int port, String database, String username, String password){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(
				"jdbc:mysql://" + host + ":" + Integer.toString(port) + "/" + database,
				username,
				password
			);
			this.statement = this.connection.createStatement();
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseWrapper::__construct() - " + e.toString());
			System.exit(1);
		}
	}

	public void close(){
		try{
			if (this.statement != null){ this.statement.close(); }
			if (this.connection != null){ this.connection.close(); }
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseWrapper::close() - " + e.toString());
			System.exit(1);
		}
	}

	public ResultSet select(String[] columns, String[] tables, String[] conditions, String append){
		String query = "SELECT ";
		query += String.join(",", new ArrayList<String>(Arrays.asList(columns)));
		query += " FROM ";
		query += String.join(",", new ArrayList<String>(Arrays.asList(tables)));
		if (conditions != null){
			query += " WHERE ";
			query += String.join(" AND ", new ArrayList<String>(Arrays.asList(conditions)));
		}
		query += " " + append;
		try{
			return this.statement.executeQuery(query);
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseWrapper::select() - " + e.toString());
			this.close();
			System.exit(1);
		}

		return null;
	}

	public int insert(String table, String[] columns, String[] values, boolean[] asString, String append){
		if (values.length != asString.length){
			Log.$(Log.ERROR, "Error executing DatabaseWrapper::inset() - values[] and asString[] arrays have different lengths");
			System.exit(1);
		}
		ArrayList<String> v = new ArrayList<String>(Arrays.asList(values));
		for (int i = 0; i < v.size(); i ++){
			if (asString[i]){ v.set(i, "'" + v.get(i) + "'"); }
		}
		String query = "INSERT INTO ";
		query += table;
		query += " (";
	 	query += String.join(",", new ArrayList<String>(Arrays.asList(columns)));
		query += ") VALUES(";
		query += String.join(",", v);
		query += ") ";
		query += append;

		try{
			return this.statement.executeUpdate(query);
		} catch (Exception e){
			e.printStackTrace();
			Log.$(Log.FATAL, "Exception in DatabaseWrapper::insert() - " + e.toString());
			this.close();
			System.exit(1);
		}

		return -1;
	}
}
