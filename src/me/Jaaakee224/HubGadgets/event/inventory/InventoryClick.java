package me.Jaaakee224.HubGadgets.event.inventory;

import me.Jaaakee224.HubGadgets.config.Options;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {
	
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!event.getWhoClicked().isOp()) {
            final ItemStack current = event.getCurrentItem();
            if (current != null && current.hasItemMeta() && current.isSimilar(Options.getItem().getItemStack())) {
                event.setCancelled(true);
            }
        }
    }
}