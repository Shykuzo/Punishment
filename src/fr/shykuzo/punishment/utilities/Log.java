package fr.shykuzo.punishment.utilities;

import java.util.logging.Level;

import org.bukkit.Bukkit;

public class Log {

	public void consoleLog(Level severity, String message) {
		Bukkit.getConsoleSender().sendMessage(
				String.format(
						"\n" + "§8----------------------------------------"
						+ "\n\n" + "%s%s"
						+ "\n\n" + "§8----------------------------------------",
						
						setColor(severity),
						message
				)
		);
	}
	
		// -------------------- \\
	
	private String setColor(Level severity) {
		if(severity == Level.INFO) return "§f";
		if(severity == Level.FINE) return "§a";
		if(severity == Level.WARNING) return "§6";
		if(severity == Level.SEVERE) return "§c";
		
		return "§f";
	}
	
}
