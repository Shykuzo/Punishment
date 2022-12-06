package fr.shykuzo.punishment;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.shykuzo.punishment.utilities.Log;
import fr.shykuzo.punishment.utilities.configuration.ConfigManager;
import fr.shykuzo.punishment.utilities.configuration.LanguageManager;
import fr.shykuzo.punishment.utilities.configuration.annotation.Annotation;
import fr.shykuzo.punishment.utilities.database.DatabaseManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private static JDA discordAPI;
	private DatabaseManager databaseManager;
	
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
		}
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
