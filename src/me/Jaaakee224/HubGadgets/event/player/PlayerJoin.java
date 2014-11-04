package me.Jaaakee224.HubGadgets.event.player;

import me.Jaaakee224.HubGadgets.config.Options;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
	
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Options.Item item = Options.getItem();
        if (item.isEnable()) {
            event.getPlayer().getInventory().setItem(Options.getItem().getSlot(), Options.getItem().getItemStack());
        }
    }
}