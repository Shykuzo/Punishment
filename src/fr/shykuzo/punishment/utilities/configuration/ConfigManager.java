package fr.shykuzo.punishment.utilities.configuration;

import java.util.logging.Level;

import fr.shykuzo.punishment.Main;
import fr.shykuzo.punishment.utilities.Log;
import fr.shykuzo.punishment.utilities.configuration.annotation.Configuration;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class ConfigManager {

	@Configuration(path = "GLOBAL.PREFIX.PREFIX", color = true)
	private String prefix = "&6&lPunishment &8&l×";
	
	@Configuration(path = "GLOBAL.PREFIX.ERROR_PREFIX", color = true)
	private String errorPrefix = "&4&l(!)";
	
					// ---------- \\
	
	@Configuration(path = "GLOBAL.MODULE.DATABASE", color = false)
	private boolean databaseModule = false;
	
	@Configuration(path = "GLOBAL.MODULE.DISCORD", color = false)
	private boolean discordModule = false;
	
				// -------------------- \\
	
	@Configuration(path = "DATABASE.NAME", color = false)
	private String databaseName = "Punishment";
	
	@Configuration(path = "DATABASE.HOST.IP", color = false)
	private String databaseHost = "";
	
	@Configuration(path = "DATABASE.HOST.PORT", color = false)
	private int databasePort = 3306;
	
	@Configuration(path = "DATABASE.USER.NAME", color = false)
	private String databaseUserName = "";
	
	@Configuration(path = "DATABASE.USER.PASSWORD", color = false)
	private String databaseUserPassword = "";
	
				// -------------------- \\
	
	@Configuration(path = "DISCORD.TOKEN", color = false)
	private String discordToken = "-";
	
	@Configuration(path = "DISCORD.STATUS", color = false)
	private String discordStatus = "DO_NOT_DISTURB";
	
	@Configuration(path = "DISCORD.ACTIVITY.TYPE", color = false)
	private String discordActivityType = "WATCHING";
	
	@Configuration(path = "DISCORD.ACTIVITY.VALUE", color = false)
	private String discordActivityValue = "Active Punishments";
	
		// ---------------------------------------- \\
	
	public String getPermission(String PATH) {
		return Main.getInstance().getConfig().getString("PERMISSION." + PATH);
	}
	
		// ---------------------------------------- \\
	
	public String getPrefix() {
		return prefix + " "; 
	}
	
	public String getErrorPrefix() {
		return errorPrefix + " ";
	}
	
					// ---------- \\
	
	public boolean isDatabaseModule() {
		return databaseModule;
	}
	
	public boolean isDiscordModule() {
		return discordModule;
	}
	
				// -------------------- \\
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public String getDatabaseHost() {
		return databaseHost;
	}
	
	public int getDatabasePort() {
		return databasePort;
	}
	
	public String getDatabaseUserName() {
		return databaseUserName;
	}
	
	public String getDatabaseUserPassword() {
		return databaseUserPassword;
	}
	
				// -------------------- \\
	
	public String getToken() {
		return discordToken;
	}
	
	public OnlineStatus getDiscordOnlineStatus() {
		switch(discordStatus) {
			case "ONLINE":
				return OnlineStatus.ONLINE;
				
			case "OFFLINE":
				return OnlineStatus.OFFLINE;
				
			case "DO_NOT_DISTURB":
				return OnlineStatus.DO_NOT_DISTURB;
				
			case "IDLE":
				return OnlineStatus.IDLE;
				
			case "INVISIBLE":
				return OnlineStatus.INVISIBLE;
				
			default:
				new Log().consoleLog(Level.WARNING, "The '" + discordStatus + "' status does not exist !");
				return OnlineStatus.ONLINE;
		}
	}
	
	public Activity getDiscordActivity() {
		switch(discordActivityType) {
			case "PLAYING":
				return Activity.playing(discordActivityValue);
				
			case "LISTENING":
				return Activity.listening(discordActivityValue);
				
			case "WATCHING":
				return Activity.watching(discordActivityValue);
				
			default:
				new Log().consoleLog(Level.WARNING, "The '" + discordActivityType + "' activity type does not exist !");
				return Activity.watching(discordActivityValue);
		}
	}
	
}
