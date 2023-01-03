package fr.shykuzo.punishment.utilities.configuration;

import java.util.logging.Level;

import fr.shykuzo.punishment.Main;
import fr.shykuzo.punishment.utilities.Log;
import fr.shykuzo.punishment.utilities.configuration.annotation.Configuration;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class ConfigManager {

	@Configuration(path = "GLOBAL.VERSION", color = false)
	private String version = "ERROR";
	
	@Configuration(path = "GLOBAL.PREFIX.PREFIX", color = true)
	private String prefix = "&6&lPunishment &8&l×";
	
	@Configuration(path = "GLOBAL.PREFIX.ERROR", color = true)
	private String errorPrefix = "&4&l(!)";
	
					// ---------- \\
	
	@Configuration(path = "GLOBAL.MODULE.PREMIUM", color = false)
	private boolean premiumModule = false;
	
	@Configuration(path = "GLOBAL.MODULE.DATABASE", color = false)
	private boolean databaseModule = false;
	
	@Configuration(path = "GLOBAL.MODULE.DISCORD", color = false)
	private boolean discordModule = false;
	
	@Configuration(path = "GLOBAL.MODULE.BROADCAST", color = false)
	private boolean broadcastModule = true;
	
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
	
				// -------------------- \\
	
	@Configuration(path = "TIMEUNIT.PERMANENT", color = false)
	private String permanent = "Permanent";
	
	@Configuration(path = "TIMEUNIT.SECOND.LONG_NAME", color = false)
	private String secondLongName = "Second";
	
	@Configuration(path = "TIMEUNIT.SECOND.SHORT_NAME", color = false)
	private String secondShortName = "s";
	
	@Configuration(path = "TIMEUNIT.MINUTE.LONG_NAME", color = false)
	private String minuteLongName = "Minute";
	
	@Configuration(path = "TIMEUNIT.MINUTE.SHORT_NAME", color = false)
	private String minuteShortName = "m";
	
	@Configuration(path = "TIMEUNIT.HOUR.LONG_NAME", color = false)
	private String hourLongName = "Hour";
	
	@Configuration(path = "TIMEUNIT.HOUR.SHORT_NAME", color = false)
	private String hourShortName = "h";
	
	@Configuration(path = "TIMEUNIT.DAY.LONG_NAME", color = false)
	private String dayLongName = "Day";
	
	@Configuration(path = "TIMEUNIT.DAY.SHORT_NAME", color = false)
	private String dayShortName = "d";
	
		// ---------------------------------------- \\
	
	public String getPermission(String PATH) {
		return Main.getInstance().getConfig().getString("PERMISSION." + PATH);
	}
	
		// ---------------------------------------- \\
	
	public String getVersion() {
		return version; 
	}
	
	public String getPrefix() {
		return prefix + " "; 
	}
	
	public String getErrorPrefix() {
		return errorPrefix + " ";
	}
	
					// ---------- \\
	
	public boolean isPremiumModule() {
		return premiumModule;
	}
	
	public boolean isDatabaseModule() {
		return databaseModule;
	}
	
	public boolean isDiscordModule() {
		return discordModule;
	}
	
	public boolean isBroadcastModule() {
		return broadcastModule;
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
	
				// -------------------- \\
	
	public String getPermanent() {
		return permanent;
	}
	
	public String getSecondLongName() {
		return secondLongName;
	}
	
	public String getSecondShortName() {
		return secondShortName;
	}
	
	public String getMinuteLongName() {
		return minuteLongName;
	}
	
	public String getMinuteShortName() {
		return minuteShortName;
	}
	
	public String getHourLongName() {
		return hourLongName;
	}
	
	public String getHourShortName() {
		return hourShortName;
	}
	
	public String getDayLongName() {
		return dayLongName;
	}
	
	public String getDayShortName() {
		return dayShortName;
	}
	
}
