package fr.shykuzo.punishment;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.shykuzo.punishment.commands.administration.Punishment;
import fr.shykuzo.punishment.commands.moderation.ban.Ban;
import fr.shykuzo.punishment.commands.moderation.ban.UnBan;
import fr.shykuzo.punishment.listeners.connexion.PlayerJoin;
import fr.shykuzo.punishment.listeners.connexion.PlayerLogin;
import fr.shykuzo.punishment.utilities.Log;
import fr.shykuzo.punishment.utilities.configuration.ConfigManager;
import fr.shykuzo.punishment.utilities.configuration.LanguageManager;
import fr.shykuzo.punishment.utilities.configuration.annotation.Annotation;
import fr.shykuzo.punishment.utilities.database.DatabaseManager;
import fr.shykuzo.punishment.utilities.database.managers.BanManager;
import fr.shykuzo.punishment.utilities.database.managers.HistoryManager;
import fr.shykuzo.punishment.utilities.database.managers.MuteManager;
import fr.shykuzo.punishment.utilities.database.managers.PlayerManager;
import fr.shykuzo.punishment.utilities.database.managers.WarnManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private static JDA discordAPI;
	
	private DatabaseManager databaseManager;
	private PlayerManager playerManager;
	
	private BanManager banManager;
	private MuteManager muteManager;
	private WarnManager warnManager;
	private HistoryManager historyManager;
	
			// ---------- \\
	
	private ConfigManager configManager;
	private LanguageManager languageManager;
	
	private File configFile;
	private File languageConfigFile;
	
	private FileConfiguration config;
	private FileConfiguration languageConfig;
	
		// -------------------- \\
	
	@Override
	public void onEnable() {
		instance = this;
		
		databaseManager = new DatabaseManager();
		playerManager = new PlayerManager();
		
		banManager = new BanManager();
		muteManager = new MuteManager();
		warnManager = new WarnManager();
		historyManager = new HistoryManager();
		
		load();
		
		if(getConfigManager().isDiscordModule() && getConfigManager().getToken() != "-") {
			discordAPI = JDABuilder.createDefault(getConfigManager().getToken())
					.setStatus(getConfigManager().getDiscordOnlineStatus())
					.setActivity(getConfigManager().getDiscordActivity())
					.build();
		} else {
			new Log().consoleLog(
					Level.WARNING,
					String.format(
							"The 'Discord' module could not be activated !"
							+ "\n" + "Status : %s",
							
							(getConfigManager().isDatabaseModule() ? "Error (Missing Token)" : "Normal (Module Disabled)")
					)
			);
		}
		
		if(getConfigManager().isDatabaseModule()) {
			getDatabaseManager().connect();
			
			if(getDatabaseManager().isConnected()) {
				getDatabaseManager().createTables();
			}
		}
		
			// ---------- \\
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoin(), getInstance());
		Bukkit.getPluginManager().registerEvents(new PlayerLogin(), getInstance());
		
		getCommand("punishment").setExecutor(new Punishment());
		
		getCommand("ban").setExecutor(new Ban());
		getCommand("unban").setExecutor(new UnBan());
	}
	
	@Override
	public void onDisable() {
		if(getConfigManager().isDatabaseModule() && getDatabaseManager().isConnected()) {
			getDatabaseManager().disconnect();
		}
	}
	
		// -------------------- \\
	
	public void load() {
		configFile = new File(getDataFolder(), "Configuration.yml");
		languageConfigFile = new File(getDataFolder(), "Language.yml");
		
		if(!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			saveResource("Configuration.yml", false);
		}
		
		if(!languageConfigFile.exists()) {
			languageConfigFile.getParentFile().mkdirs();
			saveResource("Language.yml", false);
		}
		
		configManager = new ConfigManager();
		languageManager = new LanguageManager();
		
		config = YamlConfiguration.loadConfiguration(configFile);
		languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
		
		reload();
	}
	
	public void reload() {
		Annotation.loadClass(config, getConfigManager());
		Annotation.loadClass(languageConfig, getLanguageManager());
	}
	
	public void reset() {
		if(configFile.exists()) {
			configFile.delete();
		}
		
		if(languageConfigFile.exists()) {
			languageConfigFile.delete();
		}
		
		load();
	}
	
		// -------------------- \\
	
	public static Main getInstance() {
		return instance;
	}
	
	public static JDA getDiscordAPI() {
		return discordAPI;
	}
	
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
			// ---------- \\
	
	public BanManager getBanManager() {
		return banManager;
	}
	
	public MuteManager getMuteManager() {
		return muteManager;
	}
	
	public WarnManager getWarnManager() {
		return warnManager;
	}
	
	public HistoryManager getHistoryManager() {
		return historyManager;
	}
	
			// ---------- \\
	
	public ConfigManager getConfigManager() {
		return configManager;
	}
	
	public LanguageManager getLanguageManager() {
		return languageManager;
	}
	
	@Override
	public FileConfiguration getConfig() {
		return config;
	}
	
	public FileConfiguration getLanguageConfig() {
		return languageConfig;
	}

}
