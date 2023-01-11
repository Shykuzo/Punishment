package fr.shykuzo.punishment.utilities.database.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.shykuzo.punishment.Main;
import fr.shykuzo.punishment.utilities.enumerations.Query;
import fr.shykuzo.punishment.utilities.enumerations.TimeUnit;

public class MuteManager {

	public void mute(String playerName, UUID playerUUID, String moderatorName, String moderatorUUID, long endInSeconds, String reason) {
		if(isMuted(playerName, playerUUID)) return;
		
		long endToMillis = endInSeconds * 1000L;
		long end = (endInSeconds == -1L ? -1L : endToMillis + System.currentTimeMillis());
		
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(Query.INSERT_PUNISHMENT.getQuery());
			
			statement.setString(1, playerName);
			statement.setString(2, playerUUID.toString());
			statement.setString(3, moderatorName);
			statement.setString(4, moderatorUUID);
			statement.setString(5, (end == -1L ? "PERMANENT_MUTE" : "TEMPORARY_MUTE"));
			statement.setLong(6, end);
			statement.setString(7, reason);
			statement.setString(8, DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(LocalDateTime.now()));
			
			statement.executeUpdate();
			addHistory(playerName, playerUUID, moderatorName, moderatorUUID, (end == -1L ? "PERMANENT_MUTE" : "TEMPORARY_MUTE"), reason);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
			// ---------- \\
		
		if(Bukkit.getPlayer(playerUUID) != null || Bukkit.getPlayer(playerName) != null) {
			Player target = (Main.getInstance().getConfigManager().isPremiumModule() ? Bukkit.getPlayer(playerUUID) : Bukkit.getPlayer(playerName));
			
			target.kickPlayer(
					Main.getInstance().getLanguageManager().getPunishmentMessage(
							"BAN",
							moderatorName,
							reason,
							getRemainingTime(playerName, playerUUID),
							DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(LocalDateTime.now())
					)
			);
		}
		
	}
	
	public void unmute(String playerName, UUID playerUUID) {
		if(!isMuted(playerName, playerUUID)) return;
		
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(
					(Main.getInstance().getConfigManager().isPremiumModule() ? Query.REMOVE_PUNISHMENT_BY_UUID.getQuery() : Query.REMOVE_PUNISHMENT_BY_NAME.getQuery())
			);
			
			statement.setString(1, (Main.getInstance().getConfigManager().isPremiumModule() ? playerUUID.toString() : playerName));
			statement.setString(2, "TEMPORARY_MUTE OR PERMANENT_MUTE");
			statement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	private void addHistory(String playerName, UUID playerUUID, String moderatorName, String moderatorUUID, String type, String reason) {		
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(Query.INSERT_HISTORY.getQuery());
			
			statement.setString(1, playerName);
			statement.setString(2, playerUUID.toString());
			statement.setString(3, moderatorName);
			statement.setString(4, moderatorUUID);
			statement.setString(5, type);
			statement.setString(6, reason);
			statement.setString(7, DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(LocalDateTime.now()));
			
			statement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
		// -------------------- \\
	
	public boolean isMuted(String playerName, UUID playerUUID) {
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(
					(Main.getInstance().getConfigManager().isPremiumModule() ? Query.SELECT_PUNISHMENT_BY_UUID.getQuery() : Query.SELECT_PUNISHMENT_BY_NAME.getQuery())
			);
			
			statement.setString(1, (Main.getInstance().getConfigManager().isPremiumModule() ? playerUUID.toString() : playerName));
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				if(result.getString("type") == "TEMPORARY_MUTE" || result.getString("type") == "PERMANENT_MUTE") {
					return result.getBoolean("state");
				}
				return false;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return false;
	}
	
	public void checkDuration(String playerName, UUID playerUUID) {
		if(!isMuted(playerName, playerUUID)) return;
		if(getEnd(playerName, playerUUID) == -1L) return;
		if(getEnd(playerName, playerUUID) < System.currentTimeMillis()) unmute(playerName, playerUUID);
	}
	
		// -------------------- \\
	
	public String getModerator(String playerName, UUID playerUUID) {
		if(!isMuted(playerName, playerUUID)) return "NOT_MUTED";
		
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(
					(Main.getInstance().getConfigManager().isPremiumModule() ? Query.SELECT_PUNISHMENT_BY_UUID.getQuery() : Query.SELECT_PUNISHMENT_BY_NAME.getQuery())
			);
			
			statement.setString(1, (Main.getInstance().getConfigManager().isPremiumModule() ? playerUUID.toString() : playerName));
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return result.getString("moderator_name");
			}
			
			return "UNKNOWN";
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return "UNKNOWN";
	}
	
	public long getEnd(String playerName, UUID playerUUID) {
		if(!isMuted(playerName, playerUUID)) return 0L;
		
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(
					(Main.getInstance().getConfigManager().isPremiumModule() ? Query.SELECT_PUNISHMENT_BY_UUID.getQuery() : Query.SELECT_PUNISHMENT_BY_NAME.getQuery())
			);
			
			statement.setString(1, (Main.getInstance().getConfigManager().isPremiumModule() ? playerUUID.toString() : playerName));
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return result.getLong("end");
			}
			
			return 0L;
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return 0L;
	}
	
	public String getRemainingTime(String playerName, UUID playerUUID) {
		if(!isMuted(playerName, playerUUID)) return "NOT_MUTED";
		if(getEnd(playerName, playerUUID) == -1L) return "PERMANENT";
		
		long remainingTime = (getEnd(playerName, playerUUID) - System.currentTimeMillis()) / 1000L;
		
		int second = 0;
		int minute = 0;
		int hour = 0;
		int day = 0;
		
		while(remainingTime >= TimeUnit.DAY.getToSecond()) {
			day++;
			remainingTime -= TimeUnit.DAY.getToSecond();
		}
		while(remainingTime >= TimeUnit.HOUR.getToSecond()) {
			hour++;
			remainingTime -= TimeUnit.HOUR.getToSecond();
		}
		while(remainingTime >= TimeUnit.MINUTE.getToSecond()) {
			minute++;
			remainingTime -= TimeUnit.MINUTE.getToSecond();
		}
		while(remainingTime >= TimeUnit.SECOND.getToSecond()) {
			second++;
			remainingTime -= TimeUnit.SECOND.getToSecond();
		}
		
		return String.format(
				"%s %s, %s %s, %s %s, %s %s",
				
				day, TimeUnit.DAY.getLongName(),
				hour, TimeUnit.HOUR.getLongName(),
				minute, TimeUnit.MINUTE.getLongName(),
				second, TimeUnit.SECOND.getLongName()
		);		
	}
	
	public String getReason(String playerName, UUID playerUUID) {
		if(!isMuted(playerName, playerUUID)) return "NOT_MUTED";
		
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(
					(Main.getInstance().getConfigManager().isPremiumModule() ? Query.SELECT_PUNISHMENT_BY_UUID.getQuery() : Query.SELECT_PUNISHMENT_BY_NAME.getQuery())
			);
			
			statement.setString(1, (Main.getInstance().getConfigManager().isPremiumModule() ? playerUUID.toString() : playerName));
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return result.getString("reason");
			}
			
			return "UNKNOWN";
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return "UNKNOWN";
	}
	
	public String getDate(String playerName, UUID playerUUID) {
		if(!isMuted(playerName, playerUUID)) return "NOT_MUTED";
		
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase().prepareStatement(
					(Main.getInstance().getConfigManager().isPremiumModule() ? Query.SELECT_PUNISHMENT_BY_UUID.getQuery() : Query.SELECT_PUNISHMENT_BY_NAME.getQuery())
			);
			
			statement.setString(1, (Main.getInstance().getConfigManager().isPremiumModule() ? playerUUID.toString() : playerName));
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return result.getString("date");
			}
			
			return "UNKNOWN";
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return "UNKNOWN";
	}
	
}
