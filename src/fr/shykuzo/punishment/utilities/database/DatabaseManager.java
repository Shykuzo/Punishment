package fr.shykuzo.punishment.utilities.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import fr.shykuzo.punishment.Main;
import fr.shykuzo.punishment.utilities.enumerations.Query;

public class DatabaseManager {

	Connection database;
	
		// -------------------- \\
	
	public void connect() {
		if(!isConnected()) {
			try {
				database = DriverManager.getConnection(
						String.format(
								"jdbc:mysql://%s:%s/%s",
								Main.getInstance().getConfigManager().getDatabaseHost(),
								Main.getInstance().getConfigManager().getDatabasePort(),
								Main.getInstance().getConfigManager().getDatabaseName()
						),
						
						Main.getInstance().getConfigManager().getDatabaseUserName(),
						Main.getInstance().getConfigManager().getDatabaseUserPassword()
				);
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public void disconnect() {
		if(isConnected()) {
			try {
				database.close();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
	}
	
		// -------------------- \\
	
	public void createTables() {
		try {
			Statement statement = database.createStatement();
			
			statement.executeUpdate(Query.CREATE_TABLE_PLAYER.getQuery());
			statement.executeUpdate(Query.CREATE_TABLE_PUNISHMENT.getQuery());
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
		// -------------------- \\
	
	public boolean isConnected() {
		try {
			if(database != null && !database.isClosed() && database.isValid(5)) {
				return true;
			}
			
			return false;
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return false;
	}
	
	public Connection getDatabase() {
		return database;
	}
	
}
