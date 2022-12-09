package fr.shykuzo.punishment.utilities.configuration;

import fr.shykuzo.punishment.Main;

public class LanguageManager {

		// ---------------------------------------- \\
	
	public String getMessage(String value) {
		String message = value;
		
		if(message.contains("{PREFIX}")) message = message.replace("{PREFIX}", Main.getInstance().getConfigManager().getPrefix());
		if(message.contains("{ERROR_PREFIX}")) message = message.replace("{ERROR_PREFIX}", Main.getInstance().getConfigManager().getErrorPrefix());
		if(message.contains("&")) message = message.replace("&", "§");
		
		return message;
	}
	
				// -------------------- \\
	
	public String getPunishmentMessage(String PATH, String MODERATOR, String REASON, String REMAINING_TIME, String DATE) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for(String value : Main.getInstance().getLanguageConfig().getStringList("PUNISHMENT." + PATH)) {
			if(value.contains("{MODERATOR}")) value = value.replace("{MODERATOR}", MODERATOR);
			if(value.contains("{REASON}")) value = value.replace("{REASON}", REASON);
			if(value.contains("{REMAINING_TIME}")) value = value.replace("{REMAINING_TIME}", REMAINING_TIME);
			if(value.contains("{DATE}")) value = value.replace("{DATE}", DATE);
			
			stringBuilder.append(getMessage(value) + "\n");
		}
		
		return stringBuilder.toString();
	}
	
}
