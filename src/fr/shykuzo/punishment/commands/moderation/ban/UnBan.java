package fr.shykuzo.punishment.commands.moderation.ban;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.shykuzo.punishment.Main;
import fr.shykuzo.punishment.utilities.enumerations.Query;

public class UnBan implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
		
		if(label.equalsIgnoreCase("unban")) {
			boolean hasAllPermission = sender.hasPermission(Main.getInstance().getConfigManager().getPermission("ALL"));
			boolean hasCommandPermission = sender.hasPermission(Main.getInstance().getConfigManager().getPermission("COMMANDS.UNBAN.USE"));
			
			if(hasAllPermission || hasCommandPermission) {
				if(Main.getInstance().getDatabaseManager().isConnected()) {
					if(arguments.length < 1) {
						sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.MISSING.ARGUMENT.TARGET"));
						sender.sendMessage("\n" + Main.getInstance().getLanguageManager().getHelpMessage("BAN"));
						return false;
					} else {
						if(Main.getInstance().getPlayerManager().exists(arguments[0])) {
							if(Main.getInstance().getBanManager().isBanned(arguments[0], getPlayerUUID(arguments[0]))) {
								Player moderator = (sender instanceof Player ? (Player) sender : null);
								
								Main.getInstance().getBanManager().unban(arguments[0], getPlayerUUID(arguments[0]));
								
								if(Main.getInstance().getConfigManager().isBroadcastModule()) {
									Bukkit.broadcastMessage(Main.getInstance().getLanguageManager().getBroadcastMessage(
											"UNBAN." + (Main.getInstance().getConfigManager().isBroadcastModule() ? "ACTIVE" : "NOT_ACTIVE"),
											arguments[0],
											(moderator != null ? moderator.getName() : "CONSOLE"),
											null,
											null
									));
								} else {
									sender.sendMessage(Main.getInstance().getLanguageManager().getBroadcastMessage(
											"UNBAN." + (Main.getInstance().getConfigManager().isBroadcastModule() ? "ACTIVE" : "NOT_ACTIVE"),
											arguments[0],
											(moderator != null ? moderator.getName() : "CONSOLE"),
											null,
											null
									));
								}
							} else {
								sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.STATE.NOT.BANNED"));
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
			
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return UUID.fromString(result.getString("player_uuid"));
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
}
