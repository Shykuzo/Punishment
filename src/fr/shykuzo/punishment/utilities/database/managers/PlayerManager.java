package fr.shykuzo.punishment.utilities.database.managers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.shykuzo.punishment.Main;
import fr.shykuzo.punishment.utilities.enumerations.Query;

public class PlayerManager {

	public void insert(Player player) {
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(Query.INSERT_PLAYER.getQuery());
			
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setString(3, player.getName());
			
			statement.executeUpdate();
			statement.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public void update(Player player) {
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(Query.UPDATE_PLAYER.getQuery());
			
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			
			statement.executeUpdate();
			statement.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
		// -------------------- \\
	
	public boolean exists(Player player) {
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(
					(Main.getInstance().getConfigManager().isPremiumModule() ? Query.SELECT_PLAYER_BY_UUID.getQuery() : Query.SELECT_PLAYER_BY_NAME.getQuery())
			);
			
			statement.setString(1, (Main.getInstance().getConfigManager().isPremiumModule() ? player.getUniqueId().toString() : player.getName()));
			return statement.executeQuery().next();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return false;
	}
	
		// -------------------- \\
	
	public UUID getUUID(String playerName) {
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(Query.SELECT_PLAYER_BY_NAME.getQuery());
			
			statement.setString(1, playerName);
			return (statement.executeQuery().next() ? UUID.fromString(statement.executeQuery().getString("player_uuid")) : null);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
}
