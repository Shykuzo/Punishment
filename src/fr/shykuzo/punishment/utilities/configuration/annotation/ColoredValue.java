package fr.shykuzo.punishment.utilities.configuration.annotation;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

public class ColoredValue {

	@SuppressWarnings("unchecked")
	public static Object setColor(Object object, char colorCharacter) {
		if(object == null) return null;
		if(object instanceof String) return ChatColor.translateAlternateColorCodes(colorCharacter, (String) object);
		
		if(object instanceof String[]) {
			String[] values = (String[]) object;
			
			for (int index = 0; index < values.length; index++) {
				values[index] = ChatColor.translateAlternateColorCodes(colorCharacter, values[index]);
			}
		}
		
		if(object instanceof List) {
			List<Object> values = (List<Object>) object;
			
			for(int index = 0; index < values.size(); index++) {
				values.set(index, setColor(values.get(index), colorCharacter));
			}
			
			return values;
		}
		
		return object;
	}
	
}
