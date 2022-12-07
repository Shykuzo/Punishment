package fr.shykuzo.punishment.listeners.connexion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.shykuzo.punishment.Main;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(Main.getInstance().getConfigManager().isDatabaseModule() && Main.getInstance().getDatabaseManager().isConnected()) {
			if(Main.getInstance().getPlayerManager().exists(event.getPlayer())) {
				if(Main.getInstance().getConfigManager().isPremiumModule()) {
					Main.getInstance().getPlayerManager().update(event.getPlayer());
				}
			} else {
				Main.getInstance().getPlayerManager().insert(event.getPlayer());
			}
		}
	}
	
}
