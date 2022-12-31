package fr.shykuzo.punishment.commands.moderation.ban;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.shykuzo.punishment.Main;
import fr.shykuzo.punishment.utilities.enumerations.Query;
import fr.shykuzo.punishment.utilities.enumerations.TimeUnit;

public class Ban implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
		
		if(label.equalsIgnoreCase("ban")) {
			boolean hasAllPermission = sender.hasPermission(Main.getInstance().getConfigManager().getPermission("ALL"));
			boolean hasCommandPermission = sender.hasPermission(Main.getInstance().getConfigManager().getPermission("COMMANDS.BAN.USE"));
			
			if(hasAllPermission || hasCommandPermission) {
				if(Main.getInstance().getDatabaseManager().isConnected()) {
					if(arguments.length < 3) {
						switch(arguments.length) {
							case 0:
								sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.MISSING.ARGUMENT.TARGET"));
								break;
								
							case 1:
								sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.MISSING.ARGUMENT.DURATION"));
								break;
								
							case 2:
								sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.MISSING.ARGUMENT.REASON"));
								break;
						}
						
						sender.sendMessage("\n" + Main.getInstance().getLanguageManager().getHelpMessage("BAN"));
						return false;
					} else {
						if(Main.getInstance().getPlayerManager().exists(arguments[0])) {							
							if(!Main.getInstance().getBanManager().isBanned(arguments[0], getPlayerUUID(arguments[0]))) {
								Player moderator = (sender instanceof Player ? (Player) sender : null);
								long duration = (arguments[1].equalsIgnoreCase("PERMANENT") ? -1L : getDuration(sender, arguments[1]));
								if(duration == 0L) return false;
								
								Main.getInstance().getBanManager().ban(
										arguments[0],
										getPlayerUUID(arguments[0]),
										(moderator != null ? moderator.getName() : "CONSOLE"),
										(moderator != null ? moderator.getUniqueId().toString() : null),
										duration,
										getReason(arguments)
								);
								
								sender.sendMessage(Main.getInstance().getLanguageManager().getBroadcastMessage(
										"BAN." + (Main.getInstance().getConfigManager().isBroadcastModule() ? "ACTIVE" : "NOT_ACTIVE"),
										arguments[0],
										(moderator != null ? moderator.getName() : "CONSOLE"),
										(arguments[1].equalsIgnoreCase("PERMANENT")
												? Main.getInstance().getConfigManager().getPermanent()
												: arguments[1].split(":")[0] + " " + TimeUnit.getFromShortName(arguments[1].split(":")[1]).getLongName()),
										getReason(arguments)
								));
							} else {
								sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.ALREADY.BANNED"));
								return false;
							}
						} else {
							sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.INVALID.TARGET"));
							return false;
						}
					}
				} else {
					sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.MISSING.CONNECTION"));
					return false;
				}
			} else {
				sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.MISSING.PERMISSION"));
				return false;
			}
		}
		
		return false;
	}
	
		// -------------------- \\
	
	private UUID getPlayerUUID(String playerName) {
		try {
			PreparedStatement statement = Main.getInstance().getDatabaseManager().getDatabase()
				.prepareStatement(Query.SELECT_PLAYER_BY_NAME.getQuery());
			
			statement.setString(1, playerName);
			if(statement.executeQuery().next()) {
				return UUID.fromString(statement.executeQuery().getString("player_uuid"));
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	private long getDuration(CommandSender sender, String durationArgument) {
		if(!durationArgument.equalsIgnoreCase("PERMANENT")) {
			if(durationArgument.contains(":")) {
				try {
					int durationInt = Integer.parseInt(durationArgument.split(":")[0]);
					if(TimeUnit.existFromShortName(durationArgument.split(":")[1])) {
						return TimeUnit.getFromShortName(durationArgument.split(":")[1]).getToSecond() * durationInt;
					} else {
						sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.INVALID.DURATION"));
						sender.sendMessage("\n" + Main.getInstance().getLanguageManager().getHelpMessage("BAN"));
						
						return 0L;
					}
				} catch (NumberFormatException exception) {
					sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.INVALID.DURATION"));
					sender.sendMessage("\n" + Main.getInstance().getLanguageManager().getHelpMessage("BAN"));
					
					return 0L;
				}
			} else {										
				sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.INVALID.DURATION"));
				sender.sendMessage("\n" + Main.getInstance().getLanguageManager().getHelpMessage("BAN"));
				
				return 0L;
			}
		}
		
		return 0L;
	}
	
	private String getReason(String[] arguments) {
		String reason = "";
		
		for(int index = 2; index < arguments.length; index++) {
			reason = reason + arguments[index] + " ";
		}
		
		return reason;
	}

}
