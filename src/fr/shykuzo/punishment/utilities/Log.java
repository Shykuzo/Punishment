package fr.shykuzo.punishment.utilities;

import java.util.logging.Level;

import org.bukkit.Bukkit;

public class Log {

	public void consoleLog(Level severity, String message) {
		Bukkit.getLogger().log(
				severity,
				String.format(
						"\n" + "§8----------------------------------------"
						+ "\n\n" + "§f%s"
						+ "\n\n" + "§8----------------------------------------",
						
						message
				)
		);
	}
	
}
