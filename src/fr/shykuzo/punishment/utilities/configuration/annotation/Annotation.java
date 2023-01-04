package fr.shykuzo.punishment.utilities.configuration.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.ConfigurationSection;

public class Annotation {
	
	private static void loadField(ConfigurationSection configuration, Object object, Field field, Configuration configValue) {
		if(configuration == null || field == null || (!Modifier.isStatic(field.getModifiers()) && object == null) || configValue == null) return;
		if(!configuration.contains(configValue.path())) { return; }
		
		field.setAccessible(true);
		Object value = configuration.get(configValue.path());
		
		if(configValue.color()) {
			value = ColoredValue.setColor(value, configValue.colorCharacter());
		}
		
		try {
			if(value != null) {
				field.set(object, value);
			}
		} catch (Exception ignored) {}
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
