package fr.shykuzo.punishment.utilities.configuration.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import fr.shykuzo.punishment.Main;

public class Annotation {
	
	private enum LogType {
		LOADED, NOT_FOUND, ERROR;
	}
	
	private static void log(Field field, Configuration configValue, Object value, LogType type) {
		if(Main.getInstance().getConfigManager().isDebugModule()) return;
		if(field == null || configValue == null || type == null) return;
		
		switch(type) {
			case LOADED:
				Bukkit.getConsoleSender().sendMessage(
						String.format(
								"§8[§eCONFIGURATION§8] §8[§aLOADED§8] §f➜ §7%s §8(§8[§8%s§8] §8%s§8)",
								(value != null) ? value.toString() : "Unknown", field.getName(), configValue.path()
						)
				);
				
				break;
				
			case NOT_FOUND:
				Bukkit.getConsoleSender().sendMessage(
						String.format(
								"§8[§eCONFIGURATION§8] §8[§6NOT FOUND§8] §f➜ §7%s §8(§8[§8%s§8] §8%s§8)",
								(value != null) ? value.toString() : "Unknown", field.getName(), configValue.path()
						)
				);
				
				break;
				
			case ERROR:
				Bukkit.getConsoleSender().sendMessage(
						String.format(
								"§8[§eCONFIGURATION§8] §8[§cERROR§8] §f➜ §7%s §8(§8[§8%s§8] §8%s§8)",
								(value != null) ? value.toString() : "Unknown", field.getName(), configValue.path()
						)
				);
				
				break;
		}
	}
	
	private static void loadField(ConfigurationSection configuration, Object object, Field field, Configuration configValue) {
		if(configuration == null || field == null || (!Modifier.isStatic(field.getModifiers()) && object == null) || configValue == null) return;
		if(!configuration.contains(configValue.path())) {
			log(field, configValue, null, LogType.NOT_FOUND);
			return;
		}
		
		field.setAccessible(true);
		Object value = configuration.get(configValue.path());
		
		if(configValue.color()) {
			value = ColoredValue.setColor(value, configValue.colorCharacter());
		}
		
		try {
			if(value != null) {
				field.set(object, value);
			}
			
			log(field, configValue, value, LogType.LOADED);
		} catch (Exception ignored) {
			log(field, configValue, value, LogType.ERROR);
		}
	}
	
	public static void loadClass(ConfigurationSection configuration, Object... objects) {
		if(configuration == null || objects == null) return;
		
		for(Object object : objects) {
			for(Field field : object.getClass().getDeclaredFields()) {
				loadField(configuration, object, field, field.<Configuration>getAnnotation(Configuration.class));
			}
		}
	}
	
	public static void loadClass(ConfigurationSection configuration, Class<?>... classes) {
		if(configuration == null || classes == null) return;
		
		for(Class<?> object : classes) {
			for(Field field : object.getDeclaredFields()) {
				loadField(configuration, null, field, field.<Configuration>getAnnotation(Configuration.class));
			}
		}
	}
	
}
