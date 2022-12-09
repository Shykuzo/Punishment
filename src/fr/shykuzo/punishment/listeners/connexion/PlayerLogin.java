package fr.shykuzo.punishment.listeners.connexion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import fr.shykuzo.punishment.Main;

public class PlayerLogin implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if(Main.getInstance().getConfigManager().isDatabaseModule() && Main.getInstance().getDatabaseManager().isConnected()) {
			Main.getInstance().getBanManager().checkDuration(event.getPlayer().getName(), event.getPlayer().getUniqueId());
			if(Main.getInstance().getBanManager().isBanned(event.getPlayer().getName(), event.getPlayer().getUniqueId())) {
				event.setResult(Result.KICK_BANNED);
				event.setKickMessage(Main.getInstance().getLanguageManager().getPunishmentMessage(
						"BAN",
						Main.getInstance().getBanManager().getModerator(event.getPlayer().getName(), event.getPlayer().getUniqueId()),
						Main.getInstance().getBanManager().getReason(event.getPlayer().getName(), event.getPlayer().getUniqueId()),
						Main.getInstance().getBanManager().getRemainingTime(event.getPlayer().getName(), event.getPlayer().getUniqueId()),
						Main.getInstance().getBanManager().getDate(event.getPlayer().getName(), event.getPlayer().getUniqueId())
				));
			}
		}
	}
	
}
