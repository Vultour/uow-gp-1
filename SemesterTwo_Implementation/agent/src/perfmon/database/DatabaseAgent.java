package perfmon.database;

import perfmon.database.DatabaseWrapper;

public class DatabaseAgent extends DatabaseWrapper{
	public DatabaseAgent(String host, int port, String database, String username, String password){
		super(host, port, database, username, password);
	}
}
