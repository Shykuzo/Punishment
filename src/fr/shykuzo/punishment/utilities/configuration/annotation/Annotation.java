package fr.shykuzo.punishment.utilities.configuration.annotation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.configuration.ConfigurationSection;

import fr.shykuzo.punishment.Main;

public class Annotation {
	
	private enum LogType {
		LOADED, NOT_FOUND, ERROR;
	}
	
	private static void writeLog(String message) {
		try {
			File logFile = new File(Main.getInstance().getDataFolder(), String.format(
					"Logs/Log-%s.txt",
					DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDateTime.now())
			));
			
			FileWriter writer = new FileWriter(logFile);
			writer.write(message);
			writer.close();
		} catch (IOException ignored) {}
	}
	
	private static void log(Field field, Configuration configValue, Object value, LogType type) {
		if(field == null || configValue == null || type == null) return;
		
		switch(type) {
			case LOADED:	
				writeLog(
						String.format(
								"[CONFIGURATION] [LOADED] ➜ %s ([%s] %s)",
								(value != null) ? value.toString() : "Unknown", field.getName(), configValue.path()
						)
				);
				
				break;
				
			case NOT_FOUND:				
				writeLog(
						String.format(
								"[CONFIGURATION] [NOT FOUND] ➜ %s ([%s] %s)",
								(value != null) ? value.toString() : "Unknown", field.getName(), configValue.path()
						)
				);
				
				break;
				
			case ERROR:				
				writeLog(
						String.format(
								"[CONFIGURATION] [ERROR] ➜ %s ([%s] %s)",
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
