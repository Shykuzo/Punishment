package fr.shykuzo.punishment.commands.administration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import fr.shykuzo.punishment.Main;

public class Punishment implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
		
		if(label.equalsIgnoreCase("punishment")) {
			boolean hasAllPermission = sender.hasPermission(Main.getInstance().getConfigManager().getPermission("ALL"));
			boolean hasCommandPermission = sender.hasPermission(Main.getInstance().getConfigManager().getPermission("COMMANDS.PUNISHMENT"));
			
			if(hasAllPermission || hasCommandPermission) {
				if(arguments.length == 0) {
					sender.sendMessage(Main.getInstance().getLanguageManager().getHelpMessage("PUNISHMENT"));
					return false;
				} else {
					switch(arguments[0].toUpperCase()) {
						case "RELOAD":
							reloadConfig(sender);
							break;
							
						case "RESET":
							resetConfig(sender);
							break;
							
						case "UPDATE":
							checkUpdate(sender);
							break;
							
						case "CONNECT":
							connect(sender);
							break;
							
						case "DISCONNECT":
							disconnect(sender);
							break;
							
						case "STATUS":
							status(sender);
							break;
							
						default:
							sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.INVALID.OPTION"));
							break;
					}
					
					return true;
				}
			} else {
				sender.sendMessage(Main.getInstance().getLanguageManager().getString("ERROR.MISSING.PERMISSION"));
				return false;
			}
		}
		
		return false;
	}
	
		// -------------------- \\
	
	private void reloadConfig(CommandSender sender) {
		sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.CONFIGURATION.RELOAD.IN_PROGRESS"));
		Main.getInstance().reload();
		sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.CONFIGURATION.RELOAD.FINISHED"));
	}
	
	private void resetConfig(CommandSender sender) {
		sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.CONFIGURATION.RESET.IN_PROGRESS"));
		Main.getInstance().reset();
		sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.CONFIGURATION.RESET.FINISHED"));
	}
	
	private void checkUpdate(CommandSender sender) {
		try {
			HttpResponse<JsonNode> response = Unirest.get("https://api.github.com/repos/shykuzo/punishment/releases/latest").asJson();
			JSONObject result = response.getBody().getObject();
			
			try {
				if(result.getString("tag_name") != Main.getInstance().getConfigManager().getVersion()) {
					sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.UPDATE.AVAILABLE"));
					return;
				} else {
					sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.UPDATE.LATEST"));
					return;
				}
			} catch(JSONException ignored) {
				sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.UPDATE.LATEST"));
				return;
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private void connect(CommandSender sender) {
		if(Main.getInstance().getDatabaseManager().isConnected()) {
			sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.DATABASE.ALREADY.CONNECTED"));
			return;
		}
		
		Main.getInstance().getDatabaseManager().connect();
		sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.DATABASE.CONNECT"));
	}
	
	private void disconnect(CommandSender sender) {
		if(!Main.getInstance().getDatabaseManager().isConnected()) {
			sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.DATABASE.ALREADY.DISCONNECTED"));
			return;
		}
		
		Main.getInstance().getDatabaseManager().disconnect();
		sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.DATABASE.DISCONNECT"));
	}
	
	private void status(CommandSender sender) {
		if(Main.getInstance().getDatabaseManager().isConnected()) {
			sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.DATABASE.STATUS.CONNECTED"));
			return;
		} else {
			sender.sendMessage(Main.getInstance().getLanguageManager().getString("MESSAGE.DATABASE.STATUS.DISCONNECTED"));
			return;
		}
	}

}
