package perfmon.database;

import java.util.ArrayList;
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
			System.out.println("FATAL: Exception in DatabaseWrapper::__construct");
			System.exit(1);
		}
	}

	public void close(){
		try{
			if (this.statement != null){ this.statement.close(); }
			if (this.connection != null){ this.connection.close(); }
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("FATAL: Exception in DatabaseWrapper::close()");
			System.exit(1);
		}
	}

	public ResultSet select(ArrayList<String> columns, ArrayList<String> tables, ArrayList<String> conditions, String append){
		String query = "SELECT ";
		query += String.join(",", columns);
		query += " FROM ";
		query += String.join(",", tables);
		if (conditions != null){
			query += " WHERE ";
			query += String.join(" AND ", conditions);
		}
		query += append;
		try{
			return this.statement.executeQuery(query);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("FATAL: Exception in DatabaseWrapper::select()");
			this.close();
			System.exit(1);
		}

		return null;
	}

	public void insert(){}
}
