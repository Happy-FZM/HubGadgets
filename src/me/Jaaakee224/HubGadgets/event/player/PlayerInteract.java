package me.Jaaakee224.HubGadgets.event.player;

import me.Jaaakee224.HubGadgets.HubGadgetsPlugin;
import me.Jaaakee224.HubGadgets.config.Options;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {
	
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (event.getAction().name().startsWith("RIGHT_CLICK") && event.hasItem() && item.hasItemMeta() && item.isSimilar(Options.getItem().getItemStack())) {
            event.setCancelled(true);
            HubGadgetsPlugin.i.menu.open(event.getPlayer(), 1);
        }
    }
}